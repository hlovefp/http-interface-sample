package com.hfp.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获Controller处理过程中出现的异常
 * 创建时间：2019年5月5日 下午2:11:12
 * 文件名称：GlobalExceptionHandler.java
 * @author hfp
 * @version 1.0
 *
 */
@ControllerAdvice
public class GlobalExceptionAdvice {
	private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

	/**
	 * 返回Json字符串
	 */
	@ExceptionHandler(Exception.class)           // 处理 Exception 类型的异常
    @ResponseBody
    public Map<String,Object> defaultExceptionHandler(Exception e) {
		logger.error("",e);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code", 500);
        map.put("msg", e.getMessage());
        return map;
    }
	
	@ExceptionHandler(ArithmeticException.class)
	@ResponseBody
    public Map<String,Object> arithmeticExceptionHandler(Exception e) {
		logger.error("",e);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code", 501);
        map.put("msg", e.getMessage());
        return map;
    }
}
