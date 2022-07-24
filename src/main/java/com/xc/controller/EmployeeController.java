package com.xc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xc.common.R;
import com.xc.entity.Employee;
import com.xc.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/employee")

public class EmployeeController {
    @Resource
    private EmployeeService employeeServiceImpl;
    @PostMapping("/login")
    public R<Employee> login (HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee em = employeeServiceImpl.getOne(queryWrapper);
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
}
