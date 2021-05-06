package com.hang.myselfcommunity.advice;

import com.hang.myselfcommunity.dto.ResultDTO;
import com.hang.myselfcommunity.exception.CustomizeErrorCode;
import com.hang.myselfcommunity.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//@ControllerAdvice
@RestControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    Object handle(HttpServletRequest request, Throwable ex, Model model) {
        HttpStatus status = getStatus(request);
        String contentType = request.getContentType();
        System.out.println(contentType);

        ex.printStackTrace();
        if ("application/json".equals(contentType)) {
            /* 返回json*/
            if (ex instanceof CustomizeException) {
                return ResultDTO.errorOf((CustomizeException) ex);
            } else {
                return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
        } else {
            /* 错误页面跳转 */
            if (ex instanceof CustomizeException) {
                model.addAttribute("message", ex.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            model.addAttribute("httpStatus", status.toString());
            return new ModelAndView("error");
        }
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
