package com.xc.service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.dto.DishDto;
import com.xc.dto.SetmealDto;
import com.xc.entity.*;
import com.xc.mapper.SetmealMapper;
import com.xc.service.CategoryService;
import com.xc.service.SetmealDishService;
import com.xc.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    CategoryService categoryService;
    @Resource
    SetmealDishService setmealDishService;
    @Override
    public Page getPage(int page, int pageSize, String name) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), Setmeal::getName, name);
        Page<Setmeal> setmealPagePage = new Page(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        this.page(setmealPagePage, queryWrapper);
        BeanUtils.copyProperties(setmealPagePage, setmealDtoPage, "records");
        List<Setmeal> setmealRecodes = setmealPagePage.getRecords();
        List<SetmealDto> setmealDtoListList = new ArrayList<>();
        for (Setmeal setmeal : setmealRecodes) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Long id = setmeal.getCategoryId();
            Category category = categoryService.getById(id);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            setmealDtoListList.add(setmealDto);
        }
        setmealDtoPage.setRecords(setmealDtoListList);
        return setmealDtoPage;
    }

    @Override
    public void addSetmeal(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setDishId(id);
        }
        setmealDishService.saveBatch(setmealDishes);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editSetmeal(SetmealDto setmealDto) {
        boolean b = this.updateById(setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SetmealDish::getDishId,setmealDto.getId());
        boolean remove = setmealDishService.remove(queryWrapper);
        System.out.println(remove);
        List<SetmealDish> flavors = setmealDto.getSetmealDishes();
        for (SetmealDish flavor : flavors) {
            flavor.setDishId(setmealDto.getCategoryId());
        }
        boolean isSuccess = setmealDishService.saveBatch(flavors);
        return isSuccess && b;

    }
}
