package com.example.demo.common.exception;

import com.example.demo.common.result.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fg
 * @date 2020/11/5
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultVO defaultErrorHandler(HttpServletRequest request, Exception ex) {
        log.error("系统异常：", ex);
        ResultVO vo = new ResultVO();
        //todo

        return vo;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO validateException(MethodArgumentNotValidException ex) {
        log.error("入参异常:{}", ex.getMessage());
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ResultVO vo = new ResultVO();
        //todo

        return vo;
    }
}
