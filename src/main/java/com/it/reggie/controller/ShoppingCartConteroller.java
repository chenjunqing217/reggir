package com.it.reggie.controller;

import com.alibaba.fastjson.JSON;
import com.it.reggie.common.R;
import com.it.reggie.entity.ShoppingCart;
import com.it.reggie.service.ShoopingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartConteroller {

    @Autowired
    private ShoopingCartService shoopingCartService;

    /**
     * 购物车新增
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<String> addShoppingCart (@RequestBody ShoppingCart shoppingCart){
        R out = shoopingCartService.addShoppingCart(shoppingCart);
        return out;
    }

    /**
     * 购物车展示
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> showcartList (){
        R out = shoopingCartService.showcartList();
        log.info("购物车展示list:"+ JSON.toJSONString(out));
        return out;
    }

    @DeleteMapping("/clean")
    public R<String> deletecart (){
        R out = shoopingCartService.deletecart();
        return out;
    }
}
