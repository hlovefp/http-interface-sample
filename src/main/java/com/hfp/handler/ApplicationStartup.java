package com.hfp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("-------------------系统初始化-----------------------");
        //InitializeService initializeService = contextRefreshedEvent.getApplicationContext().getBean(InitializeService.class);
        //initializeService.initialize();
        logger.info("-------------------初始化结束-----------------------");
    }

}
