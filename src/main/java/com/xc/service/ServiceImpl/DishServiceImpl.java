package com.xc.service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.common.R;
import com.xc.dto.DishDto;
import com.xc.entity.Category;
import com.xc.entity.Dish;
import com.xc.entity.DishFlavor;
import com.xc.mapper.DishMapper;
import com.xc.service.CategoryService;
import com.xc.service.DishFlavorService;
import com.xc.service.DishService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sun.security.krb5.internal.PAForUserEnc;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    DishFlavorService dishFlavorService;
    @Resource
    DishService dishService;
    @Resource
    CategoryService categoryService;

    @Override
    @Transactional
    public void saveDish(DishDto dishDto) {
        this.save(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getCategoryId());
        }
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public Page pageDish(int page, int pageSize, String name) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), Dish::getName, name);
        Page<Dish> dishPage = new Page(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        dishService.page(dishPage, queryWrapper);
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<Dish> dishRecodes = dishPage.getRecords();
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish dishRecode : dishRecodes) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dishRecode, dishDto);
            Long id = dishRecode.getCategoryId();
            Category category = categoryService.getById(id);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            dishDtoList.add(dishDto);
        }
        dishDtoPage.setRecords(dishDtoList);
        return dishDtoPage;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editDish(DishDto dishDto) {
        boolean b = this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        boolean remove = dishFlavorService.removeById(dishDto.getId());
        System.out.println(remove);
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getCategoryId());
        }
        boolean isSuccess = dishFlavorService.saveBatch(flavors);
        return isSuccess && b;
    }
    public boolean updateStatus(DishDto dishDto){
        boolean b = this.updateById(dishDto);
        return b;
    }

}
