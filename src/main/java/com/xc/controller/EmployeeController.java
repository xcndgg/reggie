package com.xc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.common.R;
import com.xc.entity.Employee;
import com.xc.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login (HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee em = employeeService.getOne(queryWrapper);
        if(em == null){
            return R.error("对不起，没有该用户");
        }
        if(!password.equals(em.getPassword())){
            return R.error("密码错误");
        }else{
            if(em.getStatus() == 0){
                return R.error("账号被禁用");
            } else{
                request.getSession().setAttribute("employee",em.getId());
                return R.success(em);
            }
        }

    }
    @PostMapping("logout")
    public R<String> loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    @PostMapping
    public R<String> addEmp(HttpServletRequest request,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        Long emId = (Long) request.getSession().getAttribute("employee");
        boolean isAdd = employeeService.save(employee);
        return R.success("添加成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getUsername,name);
        Page pageInfo = new Page(page,pageSize);
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @GetMapping("/{id}")
    public R<Employee> getEmById(@PathVariable Long id){
        Employee em = employeeService.getById(id);
        System.out.println(id);
        if(em == null){
            return R.error("没有该用户");
        }
        return R.success(em);

    }
    @PutMapping
    public R<String> editEm(HttpServletRequest request,@RequestBody Employee employee){
        Long emId = (Long)request.getSession().getAttribute("employee");
        if(employee != null){
            employee.setUpdateTime(LocalDateTime.now());
            employee.setUpdateUser(emId);
            employeeService.updateById(employee);
            return R.success("修改成功");
        }
        else return R.error("修改失败");
    }

}
