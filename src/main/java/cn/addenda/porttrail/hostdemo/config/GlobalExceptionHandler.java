package cn.addenda.porttrail.hostdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final String MSG_5XX_1 = "url：[{}]，异常类型：[5XX，组件异常]，信息：[{}]。";
  private static final String MSG_5XX_2 = "url：[{}]，异常类型：[5XX，未知异常]，信息：[{}]。";
  private static final String MSG_4XX = "url：[{}]，异常类型：[4XX]，信息：[{}]。";
  private static final String MSG_2XX = "url：[{}]，异常类型：[2XX]，信息：[{}]。";

  @ExceptionHandler(IllegalArgumentException.class)
  public Object handleException(IllegalArgumentException ex, HttpServletRequest request, HttpServletResponse response) {
    log.error(MSG_5XX_1, request.getRequestURI(), ex.getMessage(), ex);
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.setCharacterEncoding("UTF-8");

    Map<String, String> result = new HashMap<>();
    result.put("errorMsg", ex.getMessage());
    return result;
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public Object handleException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response) {
    log.warn(MSG_4XX, request.getRequestURI(), ex.getMessage(), ex);
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    response.setCharacterEncoding("UTF-8");

    Map<String, String> result = new HashMap<>();
    result.put("errorMsg", String.format("[%s] not found!", request.getRequestURI()));
    return result;
  }

  @ExceptionHandler(ServletRequestBindingException.class)
  public Object handleException(ServletRequestBindingException ex, HttpServletRequest request, HttpServletResponse response) {
    log.warn(MSG_4XX, request.getRequestURI(), ex.getMessage(), ex);
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    response.setCharacterEncoding("UTF-8");

    Map<String, String> result = new HashMap<>();
    result.put("errorMsg", String.format("[%s] not found!", request.getRequestURI()));
    return result;
  }

  @ExceptionHandler(Exception.class)
  public Object handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
    log.error(MSG_5XX_2, request.getRequestURI(), ex.getMessage(), ex);
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.setCharacterEncoding("UTF-8");

    Map<String, String> result = new HashMap<>();
    result.put("errorMsg", "系统异常，请联系IT处理！");
    return result;
  }

}

