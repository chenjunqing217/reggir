package com.it.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.common.BaseContext;
import com.it.reggie.common.R;
import com.it.reggie.entity.*;
import com.it.reggie.mapper.OrderMapper;
import com.it.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private ShoopingCartService shoopingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;
    
    /**
     * 新增订单
     * @param orders
     * @return
     */
    @Override
    @Transactional
    public R submit(Orders orders) {
//        获取用户信息
        Long currfentInd = BaseContext.getCurrfentInd();
//        根据用户信息查询购物车信息
        LambdaUpdateWrapper<ShoppingCart> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ShoppingCart::getUserId,currfentInd);
        List<ShoppingCart> list = shoopingCartService.list(lambdaUpdateWrapper);
//        查询地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        //查询用户数据
        User user = userService.getById(currfentInd);

        long orderId = IdWorker.getId();//订单号getNumber

        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = list.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());//???
            return orderDetail;
        }).collect(Collectors.toList());

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(currfentInd);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);
//         插入订单明细表
        orderDetailService.saveBatch(orderDetails);

//         清空购物车表
        shoopingCartService.remove(lambdaUpdateWrapper);
        return R.success("OK");
    }

    /**
     * 订单展示
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R orderPage(int page, int pageSize) {
        Page<Orders> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getCurrfentInd());
        Page<Orders> page2 = this.page(page1, lambdaQueryWrapper);
        return R.success(page2);
    }

    /**
     * 所有订单展示
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R allorderPage(int page, int pageSize) {
        Page<Orders> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Page<Orders> page2 = this.page(page1, lambdaQueryWrapper);
        return R.success(page2);
    }
}
