package xyz.playedu.api.controller;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.types.JsonResponse;

import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public JsonResponse exceptionHandler(Exception e) {
        return JsonResponse.error("系统错误", 500);
    }

    @ExceptionHandler(ServiceException.class)
    public JsonResponse serviceExceptionHandler(ServiceException e) {
        return JsonResponse.error(e.getMessage(), 1);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonResponse serviceExceptionHandler(HttpMessageNotReadableException e) {
        return JsonResponse.error("参数为空", 406);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResponse serviceExceptionHandler(MethodArgumentNotValidException e) {
        StringBuffer errorMsg = new StringBuffer();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        for (ObjectError tmpError : allErrors) {
            errorMsg.append(tmpError.getDefaultMessage()).append(",");
        }
        String msg = errorMsg.substring(0, errorMsg.length() - 1);
        return JsonResponse.error(msg, 406);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonResponse serviceExceptionHandler(HttpRequestMethodNotSupportedException e) {
        return JsonResponse.error("请求method错误", 400);
    }

    @ExceptionHandler(NotFoundException.class)
    public JsonResponse serviceExceptionHandler(NotFoundException e) {
        return JsonResponse.error(e.getMessage(), 404);
    }

}
