package com.it.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.it.reggie.common.R;
import com.it.reggie.dto.DishDto;
import com.it.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void savedish (DishDto dishDto);

    Page showDish(int page, int pageSize, String name);

    R showForUpdate(Long id);

    R updateForDish(DishDto dishDto);

    R showList(Dish dish);

    R deleteDish(List<Long> ids);

    R stopShowDish(List<Long> ids);

    R startShowDish(List<Long> ids);
}
