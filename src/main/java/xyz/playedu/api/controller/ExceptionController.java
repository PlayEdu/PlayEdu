package xyz.playedu.api.controller;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.types.JsonResponse;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public JsonResponse<String> exceptionHandler(Exception e) {
        return JsonResponse.error("系统错误", 500);
    }

    @ExceptionHandler(ServiceException.class)
    public JsonResponse<String> serviceExceptionHandler(ServiceException e) {
        return JsonResponse.error(e.getMessage(), 1);
    }

}
