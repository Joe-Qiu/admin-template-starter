package com.treeview.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException e, Model model) {
        log.error("Exception,", e);
        model.addAttribute("error", "不支持当前媒体类型," + e.getMessage());
        return "error/500";
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, Model model) {
        log.error("Exception,", e);
        model.addAttribute("error", "不支持当前媒体类型," + e.getMessage());
        return "error/500";
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public String handleHttpMediaTypeNotSupportedException(Exception e, Model model) {
        log.error("Exception,", e);
        model.addAttribute("error", "不支持当前媒体类型," + e.getMessage());
        return "error/500";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFoundException(NoHandlerFoundException e, Model model) {
        log.error("Exception,", e);
        model.addAttribute("error", "资源不存在," + e.getMessage());
        return "error/500";

    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException e, Model model) {
        log.error("Exception,", e);
        model.addAttribute("error", "空指针异常," + e.getMessage());
        return "error/500";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("Exception,", e);
        model.addAttribute("error", "服务运行异常," + e.getMessage());
        return "error/500";
    }

    @ExceptionHandler(BindException.class)
    public ModelAndView exceptionHandler(Exception e) {
        ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
        String errMsg = "处理数据失败";
        try {
            if (e instanceof BindException) {
                errMsg = ((BindException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage();
            }
        } catch (Exception ex) {
            log.error("【处理绑定错误异常】", ex);
        }
        mv.addObject("code", 500);
        mv.addObject("msg", errMsg);

        return mv;
    }
}