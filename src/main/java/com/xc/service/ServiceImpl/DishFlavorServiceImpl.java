package com.xc.service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.entity.Dish;
import com.xc.entity.DishFlavor;
import com.xc.mapper.DishFlavorMapper;
import com.xc.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
