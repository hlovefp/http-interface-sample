package com.hfp.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 处理Controller返回的数据，比如增加签名，增加时间戳，加密等
 * @Aspect处理完毕后再执行 ResponseBodyAdvice
 * 创建时间：2019年5月5日 下午5:37:15
 * 文件名称：GlobalResponseBodyAdvice.java
 * @author hfp
 * @version 1.0
 *
 */
@ControllerAdvice(basePackages = "com.hfp.controller")
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {
	private final static Logger logger = LoggerFactory.getLogger(GlobalResponseBodyAdvice.class);

	@Override
	public boolean supports(MethodParameter methodParameter, Class aClass) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(
			Object body, 
			MethodParameter methodParameter, 
			MediaType mediaType, 
			Class aClass,
			ServerHttpRequest serverHttpRequest, 
			ServerHttpResponse serverHttpResponse) {
		
		JSONObject responseMessage = null;
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
			logger.debug("获取返回参数: " + result);
			responseMessage = JSONObject.parseObject(result);
			responseMessage.put("timeStamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			
			logger.debug("处理后返回数据:"+JSON.toJSONString(responseMessage, SerializerFeature.PrettyFormat));
			return responseMessage;
		} catch (Exception e) {
			logger.error("",e);
		}
		return body;
	}
}