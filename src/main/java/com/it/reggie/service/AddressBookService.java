package com.it.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.reggie.common.R;
import com.it.reggie.entity.AddressBook;

public interface AddressBookService extends IService<AddressBook> {
    R saveAddressBook(AddressBook addressBook);

    R showList();

    R defaultaddress(AddressBook addressBook);

    R showForUpdate(Long id);

    R updateAddress(AddressBook addressBook);

    R getDefaultaddress();
}
