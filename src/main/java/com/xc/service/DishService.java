package com.xc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.dto.DishDto;
import com.xc.entity.Dish;

public interface DishService extends IService<Dish> {
    public void saveDish(DishDto dishDto);

    public Page pageDish(int page, int pageSize, String name);

    public boolean editDish(DishDto dishDto);
    public boolean updateStatus(DishDto dishDto);
}
