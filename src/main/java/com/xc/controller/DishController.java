package com.xc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.sun.java.swing.plaf.windows.WindowsTextAreaUI;
import com.xc.common.R;
import com.xc.dto.DishDto;
import com.xc.entity.Category;
import com.xc.entity.Dish;
import com.xc.entity.DishFlavor;
import com.xc.entity.Employee;
import com.xc.service.CategoryService;
import com.xc.service.DishFlavorService;
import com.xc.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Resource
    DishService dishService;
    @Resource
    CategoryService categoryService;
    @Resource
    DishFlavorService dishFlavorService;
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page dishDtoPage = dishService.pageDish(page, pageSize, name);

        return R.success(dishDtoPage);
    }
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveDish(dishDto);
        return R.success("新增菜品成功");
    }
    @GetMapping("/{id}")
    public R<DishDto>getDishWithFlavor(@PathVariable long id){
        Dish dish = dishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getId,id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> editDish(@RequestBody DishDto dishDto){
        boolean b = dishService.editDish(dishDto);
        if(b){
        return R.success("修改成功");
        }
        return R.error("修改失败");
    }
    @PostMapping("/status/{status}")
    public R<String> updataStatus(@PathVariable Integer status ,@RequestParam List<Long> ids){
        Boolean b = false;
        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            b = dishService.updateById(dish);
        }
        if(b)
        return R.success("修改成功");
        return R.error("修改失败");
    }
    @DeleteMapping
    public R<String> updataStatus(@RequestParam List<Long> ids){
        boolean b = false;
        for (Long id : ids) {
            b = dishService.removeById(id);
        }
        if(b)
        return R.success("删除成功");
        return R.error("删除失败");
    }

}
