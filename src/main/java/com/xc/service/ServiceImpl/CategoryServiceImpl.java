package com.xc.service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.entity.Category;
import com.xc.entity.Dish;
import com.xc.entity.Setmeal;
import com.xc.mapper.CategoryMapper;
import com.xc.service.CategoryService;
import com.xc.service.DishService;
import com.xc.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    DishService dishService;
    @Resource
    SetmealService setmealService;
    @Override
    public boolean removeCategory(Long ids) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapperDish = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapperSetmeal = new LambdaQueryWrapper<>();
        lambdaQueryWrapperSetmeal.eq(Setmeal::getCategoryId,ids);
        lambdaQueryWrapperDish.eq(Dish::getCategoryId,ids);
        int countDish = dishService.count(lambdaQueryWrapperDish);
        int coutSetmeal = setmealService.count(lambdaQueryWrapperSetmeal);
        if(countDish>0||coutSetmeal>0){
            return false;
        }
        return true;

    }
}
