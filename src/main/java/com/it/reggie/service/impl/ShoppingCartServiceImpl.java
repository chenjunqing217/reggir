package com.it.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.common.BaseContext;
import com.it.reggie.common.R;
import com.it.reggie.entity.ShoppingCart;
import com.it.reggie.mapper.ShoppingCartMapper;
import com.it.reggie.service.ShoopingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoopingCartService {

    /**
     * 购物车新增
     * @param shoppingCart
     * @return
     */
    @Override
    public R addShoppingCart(ShoppingCart shoppingCart) {
        Long currfentInd = BaseContext.getCurrfentInd();
        shoppingCart.setUserId(currfentInd);
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,currfentInd);
        if (shoppingCart.getDishId() != null){
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = this.getOne(lambdaQueryWrapper);
        if (one == null){
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
        }else {
            Integer number = one.getNumber();
            one.setNumber(number+1);
            this.updateById(one);
        }
        return R.success("OK");
    }

    /**
     * 购物车展示
     * @return
     */
    @Override
    public R showcartList() {
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrfentInd());
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = this.list(lambdaQueryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @Override
    public R deletecart() {
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrfentInd());
        this.remove(lambdaQueryWrapper);
        return R.success("OK");
    }
}
