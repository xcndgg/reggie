package com.xc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.entity.Category;

public interface CategoryService extends IService<Category> {
    public boolean removeCategory(Long ids);
}
