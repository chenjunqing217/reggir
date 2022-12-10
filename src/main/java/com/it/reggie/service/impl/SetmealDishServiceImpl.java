package com.it.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.entity.SetmealDish;
import com.it.reggie.mapper.SetmealDishMapper;
import com.it.reggie.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public void updateBatchBySetmealId(List<Long> ids) {
        setmealDishMapper.updateBatchBySetmealId(ids);
    }

    @Override
    public void updateSetmealDishList(Long id) {
        setmealDishMapper.updateSetmealDishList(id);
    }
}
