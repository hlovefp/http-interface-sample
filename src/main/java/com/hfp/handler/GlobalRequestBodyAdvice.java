package com.hfp.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.hfp.util.ValidatorUtil;


/**
 * 数据的验签，解密等操作
 * @Aspect处理之前执行 ResponseBodyAdvice
 * 创建时间：2019年5月5日 下午7:08:58
 * 文件名称：GlobalRequestBodyAdvice.java
 * @author 贺飞平
 * @version 1.0
 *
 */
@ControllerAdvice(basePackages = "com.hfp.controller")
public class GlobalRequestBodyAdvice implements RequestBodyAdvice{
	private final static Logger logger = LoggerFactory.getLogger(GlobalRequestBodyAdvice.class);

	@Override
	public HttpInputMessage beforeBodyRead(
			HttpInputMessage httpInputMessage, 
			MethodParameter methodParameter, 
			Type type,                                   // @RequestBody注解的参数类型
			Class<? extends HttpMessageConverter<?>> cl)
					throws IOException {


        
		return new HttpInputMessage() {
			@Override
			public InputStream getBody() throws IOException {
				
				String body = IOUtils.toString(httpInputMessage.getBody());
				logger.info("请求数据:"+body);

				// 解密，验签...

				return new ByteArrayInputStream(body.getBytes("UTF-8"));
			}

			@Override
			public HttpHeaders getHeaders() {
				return httpInputMessage.getHeaders();
			}
		};
	}
	
	@Override
	public boolean supports(
			MethodParameter methodParameter, 
			Type type, 
			Class<? extends HttpMessageConverter<?>> cl) {
		return methodParameter.hasParameterAnnotation(RequestBody.class);
	}
	
	@Override
	public Object afterBodyRead(
			Object obj, 
			HttpInputMessage httpInputMessage, 
			MethodParameter methodParameter,
			Type type,
			Class<? extends HttpMessageConverter<?>> cl) {
		
		logger.info(obj.getClass().getName());
		logger.info(ValidatorUtil.validate(obj).toString());
		
		return obj;
	}

	@Override
	public Object handleEmptyBody(
			Object obj, 
			HttpInputMessage arg1, 
			MethodParameter arg2, 
			Type type,
			Class<? extends HttpMessageConverter<?>> arg4) {
		// body为空时处理
		return obj;
	}

	
}
