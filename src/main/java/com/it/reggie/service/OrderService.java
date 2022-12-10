package com.it.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.reggie.common.R;
import com.it.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {
    R submit(Orders orders);

    R orderPage(int page, int pageSize);
}
