package com.it.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.reggie.common.R;
import com.it.reggie.entity.ShoppingCart;

public interface ShoopingCartService extends IService<ShoppingCart> {
    R addShoppingCart(ShoppingCart shoppingCart);

    R showcartList();

    R deletecart();
}
