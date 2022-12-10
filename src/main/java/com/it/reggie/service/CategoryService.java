package com.it.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.reggie.common.R;
import com.it.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    R saveCategroy(Category category);

    R pagelist(int page, int pageSize);

    R deleteByIds(long ids);

    R updateSetmeal(Category category);

    R getCategoryList(Category category);
}
