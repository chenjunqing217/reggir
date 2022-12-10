package com.it.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.reggie.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    void updateBatchBySetmealId(List<Long> ids);

    void updateSetmealDishList(Long id);
}
