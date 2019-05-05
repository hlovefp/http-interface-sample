package com.hfp.handler;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.weaver.JoinPointSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 注解式切面编程 
 * 创建时间：2019年5月5日 下午3:32:54
 * 文件名称：ControllerLogAspec.java
 * @author hfp
 * @version 1.0
 *
 */
@Aspect           // 声明是一个切面
@Component        // 声明为Spring容器管理的一个Bean
public class ControllerAspect {

    private Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    // 声明切点
    @Pointcut("execution(public * com.hfp.controller.*.*(..))")
    private void webOperationLog() { }

    @Around("webOperationLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

    	//String className = pjp.getSignature().getDeclaringTypeName();
        String className = pjp.getTarget().getClass().getName(); // 拦截的类名   
        String methodName = pjp.getSignature().getName();        // 拦截的方法名称
        
        //Object[] args = pjp.getArgs();                         // 拦截的方法参数
        logger.info("****** around {}.{} begin ***********", className, methodName);

        long start = System.currentTimeMillis();

        /*
         * 执行拦截的方法之前先执行 @Before
         */
        Object object = pjp.proceed(); // 返回处理结果

        long end = System.currentTimeMillis();
        logger.info("用时: {} 毫秒", (end-start));

        logger.info("****** around {}.{} end ***********", className, methodName);

        return object;
    }
    

    @Before("webOperationLog()")
    public void before(JoinPoint joinPoint) throws Throwable {
    	// 打印请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        logger.info("请求地址: " + request.getRequestURL().toString());
        if(request.getMethod().equalsIgnoreCase("GET")){
        	logger.info("GET 请求数据 : " + request.getQueryString());
        }
        
    	String className = joinPoint.getSignature().getDeclaringTypeName(); // 拦截的类名   
        String methodName = joinPoint.getSignature().getName();        // 拦截的方法名称
        logger.info("方法名: " + className + "." + methodName);
        logger.info("参数 : " + Arrays.toString(joinPoint.getArgs()));
        
        // String requestParam = new BodyReaderHttpServletRequestWrapper(httpServletRequest).getBodyString(httpServletRequest);
        // logger.info("环绕通知中获取的请求参数requestParam:{}" + JSON.toJSONString(requestParam, SerializerFeature.PrettyFormat));

    }
    
    /**
     * @Around 执行完毕后执行 @After
     * @param joinPoint
     * @throws Throwable
     */
    @After("webOperationLog()")  // 声明一个建言，并使用@PointCut定义的切点
	public void after(JoinPoint joinPoint){
    	/*
		// 通过反射获得注解上的属性，然后做日志记录相关操作
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		Action action = method.getAnnotation(Action.class);
		System.out.println("注解式拦截==>"+action.name());
		logger.info("after");
		*/
	}
    
    /**
     *  @After 执行完毕后执行 @AfterReturning
     */
    @AfterReturning(returning = "ret", pointcut = "webOperationLog()")// returning的值和doAfterReturning的参数名一致
    public void doAfterReturning(Object ret) throws Throwable {
    	logger.info("返回结果：{}", JSON.toJSONString(ret, SerializerFeature.PrettyFormat));
    }
}
