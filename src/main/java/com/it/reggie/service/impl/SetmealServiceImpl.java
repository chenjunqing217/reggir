package com.it.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.common.R;
import com.it.reggie.dto.SetmealDto;
import com.it.reggie.entity.Dish;
import com.it.reggie.entity.Setmeal;
import com.it.reggie.entity.SetmealDish;
import com.it.reggie.mapper.SetmealMapper;
import com.it.reggie.service.CategoryService;
import com.it.reggie.service.SetmealDishService;
import com.it.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @Override
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R saveWithDish(SetmealDto setmealDto) {
//        保存套餐
        this.save(setmealDto);
//        保存菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("新增套餐成功！");
    }

    /**
     * 套餐展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R showForSetmeal(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        setmealLambdaQueryWrapper.eq(Setmeal::getIsDeleted,"0");
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> page1 = this.page(setmealPage, setmealLambdaQueryWrapper);
        List<Setmeal> records = page1.getRecords();
        List<SetmealDto> records2 = new ArrayList<>();
        for (Setmeal setmeal : records) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            String name1 = categoryService.getById(setmeal.getCategoryId()).getName();
            setmealDto.setCategoryName(name1);
            records2.add(setmealDto);
        }
        setmealDtoPage.setRecords(records2);
        return R.success(setmealDtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @Override
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R deleteSetmeals(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId,ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus,"1");
        int count = this.count(lambdaQueryWrapper);
        if (count >0){
            throw new RuntimeException("在售，不能删除！");
        }
//        物理删除
//        this.removeByIds(ids);

//        逻辑删除
        List<Setmeal> list = new ArrayList<>();
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setIsDeleted(1);
            list.add(setmeal);
        }
        this.updateBatchById(list);

//        物理删除
//        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapperdish = new LambdaQueryWrapper();
//        lambdaQueryWrapperdish.in(SetmealDish::getSetmealId,ids);
//        setmealDishService.remove(lambdaQueryWrapperdish);

//        逻辑删除
        setmealDishService.updateBatchBySetmealId(ids);
        return R.success("OK");
    }

    /**
     * 修改返现数据
     * @param id
     * @return
     */
    @Override
    public R showSetmealForUpdate(long id) {
        Setmeal byId = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(byId,setmealDto);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        lambdaQueryWrapper.eq(SetmealDish::getIsDeleted,"0");
        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @Override
    public R updateSetmeal(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        Long id = setmealDto.getId();
        setmealDishService.updateSetmealDishList(id);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishes);
        return R.success("OK");
    }

    /**
     * 客户端展示套餐内容
     * 根据分类ID查询套餐内容。
     * @param setmeal
     * @return
     * @Cacheable:将结果放入redis 有直接返回，没有查询存入redis；
     */
    @Override
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R showForAppList(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> list = this.list(lambdaQueryWrapper);
        return R.success(list);
    }


}
