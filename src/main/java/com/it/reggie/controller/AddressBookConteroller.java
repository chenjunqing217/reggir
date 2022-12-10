package com.it.reggie.controller;

import com.it.reggie.common.R;
import com.it.reggie.entity.AddressBook;
import com.it.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookConteroller {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     */
    @PostMapping
    public R<String> saveAddressBook(@RequestBody AddressBook addressBook){
        R out = addressBookService.saveAddressBook(addressBook);
        return out;
    }

    /**
     * 展示地址
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> showList (){
        R out = addressBookService.showList();
        return out;
    }

    /**
     * 设置默认地址
     * @return
     */
    @PutMapping("/default")
    public R<String> defaultaddress(@RequestBody AddressBook addressBook){
        R out = addressBookService.defaultaddress(addressBook);
        return out;
    }

    /**
     * 修改页面数据反显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> showForUpdate (@PathVariable Long id){
        R out = addressBookService.showForUpdate(id);
        return out;
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> updateAddress(@RequestBody AddressBook addressBook){
        R out = addressBookService.updateAddress(addressBook);
        return out;
    }

    /**
     * 获取默认地址
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefaultaddress(){
        R out = addressBookService.getDefaultaddress();
        return out;
    }
}
