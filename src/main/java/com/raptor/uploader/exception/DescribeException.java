package com.raptor.uploader.exception;

import com.raptor.uploader.enume.ResultEnum;
import org.springframework.http.HttpStatus;

/**
 * @author raptor
 */
public class DescribeException extends ServiceException {

    private static final long serialVersionUID = 8362753245631601878L;

    public DescribeException(ResultEnum resultEnum) {
        super(resultEnum.getCode(), resultEnum.getMsg());
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
