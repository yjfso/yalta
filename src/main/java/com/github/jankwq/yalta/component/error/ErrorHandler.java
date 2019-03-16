package com.github.jankwq.yalta.component.error;


import com.github.jankwq.yalta.bean.response.DataResponse;
import com.github.jankwq.yalta.bean.response.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinjianfeng
 * @date 2018/12/19
 */
@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public StatusResponse exceptionHandler(Exception ex){
        String message;
        if(ex instanceof SQLException | ex instanceof DataAccessException) {
            message = "sql error";
        } else {
            message = ex.getMessage();
        }
        log.error("error", ex);
        return StatusResponse.fail(message);
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public StatusResponse methodArgumentNotValidHandler(MethodArgumentNotValidException ex){
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        List<String> errorDos = objectErrors.stream().filter(
                item -> item instanceof FieldError
        ).map(
                item -> (FieldError) item
        ).map(
                item -> item.getField() + item.getDefaultMessage()
        ).collect(Collectors.toList());
        return DataResponse.success(errorDos).setError("参数验证错误");
    }
}
