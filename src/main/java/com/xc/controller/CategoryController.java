package com.xc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.common.R;
import com.xc.entity.Category;
import com.xc.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.PushbackInputStream;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> categoryPage(Long page,Long pageSize){
        Page pageInfo = new Page<>(page,pageSize);
        final Page<Category> page1 = categoryService.page(pageInfo);
        System.out.println(page1);
        return R.success(pageInfo);

    }

    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        if (category != null) {
            categoryService.save(category);
            return R.success("添加菜品分类成功");
        }
        return R.error("添加菜品分类失败");
    }
    @PutMapping
    public R<String> editCategory(@RequestBody Category category){
        if(category!=null){
            categoryService.updateById(category);
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }
    @DeleteMapping
    public R<String> deleteCategory(Long ids){
        boolean isFlag = categoryService.removeCategory(ids);
        if(isFlag){
            categoryService.removeById(ids);
            return R.success("删除分类成功");
        }
        return R.error("该分类关联了菜品或者套餐，不能删除");
    }


}
