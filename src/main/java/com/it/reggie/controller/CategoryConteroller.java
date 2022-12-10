package com.it.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.reggie.common.R;
import com.it.reggie.entity.Category;
import com.it.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryConteroller {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> saveCategroy(@RequestBody Category category){
        log.info("新增分类开始{}",category.toString());
        R out = categoryService.saveCategroy(category);
        return out;
    }

    /**
     * 分页展示
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page (int page,int pageSize){
        R out = categoryService.pagelist(page,pageSize);
        return out;
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteById (long ids){
        log.info("删除分类：{}",ids);
        R out = categoryService.deleteByIds(ids);
        return out;
    }

    @PutMapping
    public R<String> update (@RequestBody Category category){
        R out = categoryService.updateSetmeal(category);
        return out;
    }

    @GetMapping("/list")
    public R<List<Category>> getCategoryList(Category category){
        R out = categoryService.getCategoryList(category);
        return out;
    }

}
