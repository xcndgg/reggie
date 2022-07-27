package com.xc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.dto.SetmealDto;
import com.xc.entity.Setmeal;

import java.util.Set;

public interface SetmealService extends IService<Setmeal> {
   public Page getPage(int page, int pageSize, String name);

   void addSetmeal(SetmealDto setmealDto);

   public boolean editSetmeal(SetmealDto setmealDto);
}
