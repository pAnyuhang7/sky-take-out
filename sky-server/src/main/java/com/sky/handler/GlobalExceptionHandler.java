package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }


    /**
     * 捕获数据库唯一约束冲突（如新增员工时用户名重复）
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result duplicateKeyExceptionHandler(DuplicateKeyException ex){
        log.error("异常信息：{}", ex.getMessage());
        // 完整报错里有一段：Duplicate entry '键盘' for key 'employee.idx_username'
        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            // "Duplicate entry " 后面紧跟的就是重复的值，用下标截取出来
            int start = message.indexOf("Duplicate entry") + "Duplicate entry ".length();
            int end = message.indexOf(" for key", start);
            String duplicateValue = message.substring(start, end).replace("'", "");
            return Result.error(duplicateValue + MessageConstant.ALREADY_EXISTS);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * 捕获文件上传超限异常（上传的文件超过了 spring.servlet.multipart.max-file-size 限制）
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result maxUploadSizeExceptionHandler(MaxUploadSizeExceededException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error("上传文件过大，请上传 10MB 以内的文件");
    }

}
