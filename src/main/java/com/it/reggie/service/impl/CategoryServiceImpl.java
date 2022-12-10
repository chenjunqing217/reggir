package com.it.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.common.R;
import com.it.reggie.entity.Category;
import com.it.reggie.entity.Dish;
import com.it.reggie.entity.Setmeal;
import com.it.reggie.mapper.CategoryMapper;
import com.it.reggie.service.CategoryService;
import com.it.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishServiceImpl dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 新增分类
     * @param category
     * @return
     */
    @Override
    public R saveCategroy(Category category) {
        boolean save = this.save(category);
        if (save){
            return R.success("新增分类成功！");
        }else {
            return R.error("新增分类失败！");
        }
    }

    @Override
    public R pagelist(int page, int pageSize) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        Page page1 = new Page(page,pageSize);
        lambdaQueryWrapper.orderByDesc(Category::getSort);
        this.page(page1,lambdaQueryWrapper);
        return R.success(page1);
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @Override
    public R deleteByIds(long ids) {

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0){
            throw new RuntimeException("已存在菜品，不能能删除！");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if (count1 > 0){
            throw new RuntimeException("已存在套餐，不能删除！");
        }
        boolean b = super.removeById(ids);
        if (b){
            return R.success("删除分类成功！");
        }else{
            return R.error("删除分类失败！");
        }
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @Override
    public R updateSetmeal(Category category) {
        boolean b = super.updateById(category);
        if (b){
            return R.success("修改分类成功！");
        }else{
            return R.error("修改分类失败！");
        }
    }

    /**
     * 新增菜品 - 菜品分类下拉框
     * @param category
     * @return
     */
    @Override
    public R getCategoryList(Category category) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        lambdaQueryWrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = super.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
