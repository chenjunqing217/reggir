package com.it.reggie.controller;

import com.it.reggie.common.R;
import com.it.reggie.entity.Orders;
import com.it.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderServiceConteroller {

    @Autowired
    private OrderService orderService;

    /**
     * 新增订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit (@RequestBody Orders orders){
        R out = orderService.submit(orders);
        return out;
    }

    /**
     * 订单展示
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/orderPage")
    public R<List<Orders>> orderPage (int page,int pageSize){
        R out = orderService.orderPage(page,pageSize);
        return out;
    }
}
