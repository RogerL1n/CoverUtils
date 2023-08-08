package com.lry.exception.handler;




import com.lry.dto.ResultDTO;
import com.lry.exception.GlobalException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lin
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = GlobalException.class)
    public ResultDTO globalExceptionHandler(HttpServletRequest req, GlobalException e) {
        log.error("发生异常！原因是:" + e.getMessage());
        return ResultDTO.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResultDTO nullExceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:" + e.getMessage());
        return ResultDTO.error("-98", e.getMessage());
    }

    /**
     * 参数缺失异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultDTO missingParameterExceptionHandler(HttpServletRequest req, MissingServletRequestParameterException e) {
        log.error("参数缺失：", e);
        return ResultDTO.error(e.getMessage());
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    public ResultDTO exceptionHandler(HttpServletRequest req, Exception e){
        log.error("未知异常！原因是:" + e.getMessage());
        return ResultDTO.error("-99", e.getMessage());
    }

}
