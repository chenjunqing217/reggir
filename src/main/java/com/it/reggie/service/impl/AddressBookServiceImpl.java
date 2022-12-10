package com.it.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.common.BaseContext;
import com.it.reggie.common.R;
import com.it.reggie.entity.AddressBook;
import com.it.reggie.mapper.AddressBookMapper;
import com.it.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @Override
    public R saveAddressBook(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrfentInd());
        this.save(addressBook);
        return R.success("OK");
    }

    /**
     * 查询该用户所有地址
     * @return
     */
    @Override
    public R showList() {
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrfentInd());
        lambdaQueryWrapper.eq(AddressBook::getIsDeleted,"0");
        List<AddressBook> list = this.list(lambdaQueryWrapper);
        return R.success(list);
    }

    /**
     * 设置默认地址
     * @return
     */
    @Override
    public R defaultaddress(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrfentInd());
        lambdaUpdateWrapper.set(AddressBook::getIsDefault,"0");
        this.update(lambdaUpdateWrapper);

        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper2 = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper2.eq(AddressBook::getId,addressBook.getId());
        lambdaUpdateWrapper2.set(AddressBook::getIsDefault,"1");
        this.update(lambdaUpdateWrapper2);
        return R.success("OK");
    }

    /**
     * 修改数据反显
     * @param id
     * @return
     */
    @Override
    public R showForUpdate(Long id) {
        AddressBook byId = this.getById(id);
        return R.success(byId);
    }

    /**
     * 修改数据
     * @param addressBook
     * @return
     */
    @Override
    public R updateAddress(AddressBook addressBook) {
        this.updateById(addressBook);
        return R.success("OK");
    }

    /**
     * 获取默认地址
     * @return
     */
    @Override
    public R getDefaultaddress() {
        Long currfentInd = BaseContext.getCurrfentInd();
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId,currfentInd);
        lambdaQueryWrapper.eq(AddressBook::getIsDefault,"1");
        AddressBook one = this.getOne(lambdaQueryWrapper);
        return R.success(one);
    }

}
