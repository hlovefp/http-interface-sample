package com.hfp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hfp.handler.ApplicationStartup;

@SpringBootApplication
public class App {
	private static Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
        //SpringApplication.run(App.class, args);

		SpringApplication springApplication = new SpringApplication(App.class);
        springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);
        
        logger.info("启动完成。。。。。。。");
	}
}
