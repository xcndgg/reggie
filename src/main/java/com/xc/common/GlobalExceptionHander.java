package com.xc.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@RestController
@Slf4j
public class GlobalExceptionHander {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHander(SQLIntegrityConstraintViolationException ex) {
        log.info((ex.getSQLState()));
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] spilt = ex.getMessage().split("");
            String msg = spilt[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
}
