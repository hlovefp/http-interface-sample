package com.hfp.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//@Component
public class ControllerInterceptor implements HandlerInterceptor{
	private final static Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

	// 处理器实际执行之前就会被执行: 校验数据，验签
	// RequestBodyAdvice
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String body = IOUtils.toString(request.getInputStream());
		logger.info("请求数据:"+body);
		return true;  // 决定是否继续执行处理链
	}

	// 处理器执行完毕以后被执行
	// ResponseBodyAdvice是spring4.1的新特性
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	// 整个请求处理完成之后被执行
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
