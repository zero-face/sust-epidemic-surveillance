package com.example.epidemicsurveillance.exception;


import com.example.epidemicsurveillance.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName GlobalExceptionHandler
 * @Author 朱云飞
 * @Date 2021/8/30 10:42
 * @Version 1.0
 **/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseBody
    public ResponseResult epidemicException(EpidemicException e){
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));//将错误日志堆栈信息写入文件中
        return ResponseResult.error().message(e.getMessage()).code(e.getCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult error(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());//将错误日志堆栈信息写入文件中
        return ResponseResult.error();
    }
}
