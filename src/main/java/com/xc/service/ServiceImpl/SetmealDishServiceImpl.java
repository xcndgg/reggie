package com.xc.service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.entity.SetmealDish;
import com.xc.mapper.SetmealDishMapper;
import com.xc.service.SetmealDishService;
import com.xc.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
