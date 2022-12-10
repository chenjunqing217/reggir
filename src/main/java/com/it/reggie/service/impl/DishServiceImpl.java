package com.it.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.common.R;
import com.it.reggie.dto.DishDto;
import com.it.reggie.entity.Category;
import com.it.reggie.entity.Dish;
import com.it.reggie.entity.DishFlavor;
import com.it.reggie.mapper.DishMapper;
import com.it.reggie.service.CategoryService;
import com.it.reggie.service.DishFloverService;
import com.it.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFloverService dishFloverService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void savedish(DishDto dishDto) {
        super.save(dishDto);

        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        dishFloverService.saveBatch(flavors);
    }

    /**
     * 菜品分类分页展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page showDish(int page, int pageSize, String name) {
        Page<Dish> page1 = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(name != null,Dish::getName,name);
        lambdaQueryWrapper.eq(Dish::getIsDeleted,"0");
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        Page<Dish> page2 = this.page(page1, lambdaQueryWrapper);
//        分页属性
        BeanUtils.copyProperties(page2,dishDtoPage,"records");

//        copy对象只能cp dish属性，不会自动转为dishdto 对象 === 注意！
//        List<DishDto> dishDtoList = new ArrayList<>();
//        for (DishDto dish : dishDtoPage.getRecords()) {
//            DishDto dishDto = new DishDto();
//            Category byId = categoryService.getById(dish);
//            BeanUtils.copyProperties(dish,dishDto);
//            dish.setCategoryName(byId.getName());
//            dishDtoList.add(dishDto);
//        }
        List<DishDto> dishDtoList = new ArrayList<>();
        List<Dish> dishrecords = page2.getRecords();
        for (Dish dishrecord : dishrecords) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dishrecord,dishDto);
            Category byId = categoryService.getById(dishrecord.getCategoryId());//自动找id字段。
            dishDto.setCategoryName(byId.getName());
            dishDtoList.add(dishDto);
        }
        dishDtoPage.setRecords(dishDtoList);
        return dishDtoPage;
    }

    /**
     * 修改菜品数据反显
     * @param id
     * @return
     */
    @Override
    public R showForUpdate(Long id) {
        DishDto dishDto = new DishDto();
        Dish byId = this.getById(id);
        BeanUtils.copyProperties(byId,dishDto);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,byId.getId());
        List<DishFlavor> list = dishFloverService.list(lambdaQueryWrapper);
        dishDto.setFlavors(list);
        return R.success(dishDto);
    }

    /**
     * 修改菜品分类
     * @param dishDto
     * @return
     */
    @Override
    public R updateForDish(DishDto dishDto) {
//        修改菜品数据
        this.updateById(dishDto);
//        删除口味数据
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFloverService.remove(lambdaQueryWrapper);
//        新增口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
//            赋值菜品ID
            flavor.setDishId(dishDto.getId());
        }
        dishFloverService.saveBatch(flavors);
        return R.success("修改菜品分类成功！");
    }

    /**
     * 套餐管理--新增套餐--套餐菜品
     * @param dish
     * @return
     */
    @Override
    public R showList(Dish dish) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() !=null,Dish::getCategoryId,dish.getCategoryId());
        lambdaQueryWrapper.eq(Dish::getStatus,"1");
        lambdaQueryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = this.list(lambdaQueryWrapper);
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish dishrecord : list) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dishrecord,dishDto);
            Category byId = categoryService.getById(dishrecord.getCategoryId());//自动找id字段。
            dishDto.setCategoryName(byId.getName());
            //查询口味lambdaQueryWrapper
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(DishFlavor::getDishId,dishDto.getId());
            List<DishFlavor> list1 = dishFloverService.list(lambdaQueryWrapper1);
            dishDto.setFlavors(list1);
            dishDtoList.add(dishDto);
        }
        return R.success(dishDtoList);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @Override
    public R deleteDish(List<Long> ids) {
//        单个删除
//        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.set("is_deleted","1");
//        updateWrapper.eq("id",ids);
//        this.update(updateWrapper);

//        批量删除
        dishMapper.deleteDishList(ids);
        return R.success("OK");
    }

    /**
     * 批量（单条）停售
     * @param ids
     * @return
     */
    @Override
    public R stopShowDish(List<Long> ids) {
        dishMapper.stopShowDish(ids);
        return R.success("OK");
    }

    /**
     * 批量（单条）启售
     * @param ids
     * @return
     */
    @Override
    public R startShowDish(List<Long> ids) {
        dishMapper.startShowDish(ids);
        return R.success("OK");
    }


}
