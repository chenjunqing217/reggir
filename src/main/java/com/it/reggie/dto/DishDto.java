package com.it.reggie.dto;

import com.it.reggie.entity.Dish;
import com.it.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

//    其实这个参数放在dish里面也行
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
