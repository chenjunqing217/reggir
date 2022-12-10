package com.it.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.entity.DishFlavor;
import com.it.reggie.mapper.DishFlavorMapper;
import com.it.reggie.service.DishFloverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DishFloverServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFloverService {
}
