package com.it.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.reggie.common.R;
import com.it.reggie.dto.SetmealDto;
import com.it.reggie.entity.Setmeal;
import com.it.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealConteroller {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> saveWithDish(@RequestBody SetmealDto setmealDto){
        R out = setmealService.saveWithDish(setmealDto);
        return out;
    }

    /**
     * 套餐分页展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> showForSetmeal(int page,int pageSize,String name){
        R out = setmealService.showForSetmeal(page,pageSize,name);
        return out;
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetmeals (@RequestParam List<Long> ids){
        R out = setmealService.deleteSetmeals(ids);
        return out;
    }

    /**
     * 修改套餐数据反显
     */
    @GetMapping("/{id}")
    public R<SetmealDto> showSetmealForUpdate (@PathVariable Long id){
        R out = setmealService.showSetmealForUpdate(id);
        return out;
    }

    /**
     * 修改套餐
     * @return
     */
    @PutMapping
    public R<String> updateSetmeal (@RequestBody SetmealDto setmealDto){
        R out = setmealService.updateSetmeal(setmealDto);
        return out;
    }

    /**
     * 客户端展示套餐内容
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> showForAppList (Setmeal setmeal){
        R out = setmealService.showForAppList(setmeal);
        return out;
    }
}
