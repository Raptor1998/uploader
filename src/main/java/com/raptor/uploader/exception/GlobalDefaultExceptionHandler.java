package com.raptor.uploader.exception;

import com.raptor.uploader.utils.response.Result;
import com.raptor.uploader.utils.response.ResultUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;


/**
 * @author raptor
 */
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * 主动throw的异常
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleUnProcessableServiceException(ServiceException e, HttpServletResponse response) {
        response.setStatus(e.getStatusCode().value());
        return ResultUtil.defineFail(e.getErrorCode(), e.getMessage());
    }


}