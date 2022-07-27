package com.xc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.common.R;
import com.xc.dto.DishDto;
import com.xc.dto.SetmealDto;
import com.xc.entity.Dish;
import com.xc.entity.DishFlavor;
import com.xc.entity.Setmeal;
import com.xc.entity.SetmealDish;
import com.xc.service.CategoryService;
import com.xc.service.SetmealDishService;
import com.xc.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    CategoryService categoryService;
    @Resource
    SetmealService setmealService;
    @Resource
    SetmealDishService setmealDishService;
    @GetMapping("/page")
    public R<Page> getPage(int page,int pageSize,String name){
        Page<SetmealDto> page1 = setmealService.getPage(page, pageSize, name);
        return R.success(page1);
    }
    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.addSetmeal(setmealDto);
        return R.success("保存成功");
    }
    @PostMapping("/status/{status}")
    public R<String> updataStatus(@PathVariable Integer status , @RequestParam List<Long> ids){
        Boolean b = false;
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            b = setmealService.updateById(setmeal);
        }
        if(b)
            return R.success("修改成功");
        return R.error("修改失败");
    }
    @DeleteMapping
    public R<String> updataStatus(@RequestParam List<Long> ids){
        boolean b = false;
        for (Long id : ids) {
            b = setmealService.removeById(id);
        }
        if(b)
            return R.success("删除成功");
        return R.error("删除失败");
    }
    @GetMapping("/{id}")
    public R<Setmeal>getSetmealWithFlavor(@PathVariable long id){
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        return R.success(setmealDto);
    }
    @PutMapping
    public R<String> editSetMeal(@RequestBody SetmealDto setmealDto){
        boolean b = setmealService.editSetmeal(setmealDto);
        if(b){
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }
}
