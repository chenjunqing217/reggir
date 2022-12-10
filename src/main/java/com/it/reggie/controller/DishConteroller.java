package com.it.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.reggie.common.R;
import com.it.reggie.dto.DishDto;
import com.it.reggie.entity.Dish;
import com.it.reggie.service.impl.DishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
@Transactional
public class DishConteroller {

    @Autowired
    private DishServiceImpl dishService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> saveDish (@RequestBody DishDto dishDto){
        dishService.savedish(dishDto);
        return R.success("新增菜品成功！");
    }

    /**
     * 菜品分页展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> showDish(int page,int pageSize,String name){
        Page page1 = dishService.showDish(page,pageSize,name);
        return R.success(page1);
    }


    /**
     * 修改页面数据反显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> showForUpdate(@PathVariable Long id){
        R out = dishService.showForUpdate(id);
        return out;
    }

    /**
     * 修改菜品分类
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> updateForDish(@RequestBody DishDto dishDto){
        R out = dishService.updateForDish(dishDto);
        return out;
    }

    /**
     * 套餐管理--新增套餐--套餐菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> showList(Dish dish){
        R out = dishService.showList(dish);
        return out;
    }

    /**
     * 批量（单条）删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<List<Dish>> deleteDish(@RequestParam List<Long> ids){
        R out = dishService.deleteDish(ids);
        return out;
    }

    /**
     * 批量（单条）停售
     * @param ids
     * @return
     */
    @PostMapping("status/0")
    public R<List<Dish>> stopShowDish(@RequestParam List<Long> ids){
        R out = dishService.stopShowDish(ids);
        return out;
    }

    /**
     * 批量（单条）启售
     * @param ids
     * @return
     */
    @PostMapping("status/1")
    public R<List<Dish>> startShowDish(@RequestParam List<Long> ids){
        R out = dishService.startShowDish(ids);
        return out;
    }

}
