package xyz.playedu.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import xyz.playedu.api.exception.LimitException;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.types.JsonResponse;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

//    @ExceptionHandler(Exception.class)
//    public JsonResponse exceptionHandler(Exception e) {
//        log.error(e.getMessage());
//        return JsonResponse.error("系统错误", 500);
//    }

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
        StringBuilder errorMsg = new StringBuilder();
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public JsonResponse serviceExceptionHandler(MethodArgumentTypeMismatchException e) {
        return JsonResponse.error("请求错误", 400);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResponse serviceExceptionHandler(MissingServletRequestParameterException e) {
        return JsonResponse.error("参数错误", 406);
    }

    @ExceptionHandler(NotFoundException.class)
    public JsonResponse serviceExceptionHandler(NotFoundException e) {
        return JsonResponse.error(e.getMessage(), 404);
    }

    @ExceptionHandler(LimitException.class)
    public JsonResponse serviceExceptionHandler(LimitException e) {
        return JsonResponse.error("请稍后再试", 429);
    }

}
