package com.hfp.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;


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
			Type arg2,
			Class<? extends HttpMessageConverter<?>> arg3) throws IOException {
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
	public Object afterBodyRead(Object obj, HttpInputMessage arg1, MethodParameter arg2, Type arg3,
			Class<? extends HttpMessageConverter<?>> arg4) {
		return obj;
	}

	@Override
	public Object handleEmptyBody(Object obj, HttpInputMessage arg1, MethodParameter arg2, Type arg3,
			Class<? extends HttpMessageConverter<?>> arg4) {
		return obj;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Type arg1, Class<? extends HttpMessageConverter<?>> arg2) {
		return true;
		//return methodParameter.hasParameterAnnotation(RequestBody.class);
	}
}
