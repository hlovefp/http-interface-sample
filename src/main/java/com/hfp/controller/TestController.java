package com.hfp.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hfp.entity.UserInfo;

@RestController
public class TestController {
	private static Logger logger = LoggerFactory.getLogger(TestController.class);

	// 处理application/json请求
	@PostMapping("/test")
	public Map<String, Object> test(@RequestBody UserInfo userInfo){
		Map<String, Object> map = new HashMap<>();
		logger.info("test");
		map.put("name", "测试");
		return map;
	}
}
