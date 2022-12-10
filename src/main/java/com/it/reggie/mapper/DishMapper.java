package com.it.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    void deleteDishList(List<Long> ids);

    void stopShowDish(List<Long> ids);

    void startShowDish(List<Long> ids);
}
