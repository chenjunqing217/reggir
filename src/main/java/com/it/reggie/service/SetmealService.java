package com.it.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.reggie.common.R;
import com.it.reggie.dto.SetmealDto;
import com.it.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    R saveWithDish(SetmealDto setmealDto);

    R showForSetmeal(int page, int pageSize, String name);

    R deleteSetmeals(List<Long> ids);

    R showSetmealForUpdate(long id);

    R updateSetmeal(SetmealDto setmealDto);

    R showForAppList(Setmeal setmeal);
}
