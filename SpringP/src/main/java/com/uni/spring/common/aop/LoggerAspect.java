package com.uni.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import ch.qos.logback.classic.Logger;

public class LoggerAspect {
	private static final Logger logger=(Logger) LoggerFactory.getLogger(LoggerAspect.class);
	
	
	
	public Object aroundLogAdvice(ProceedingJoinPoint join) throws Throwable {
		
		Signature sig = join.getSignature();
		String type = sig.getDeclaringTypeName();
		String methodName = sig.getName();
		
		String cName = "";
		if(type.indexOf("Controller")>-1) {
			cName = "Controller : ";
		}else if(type.indexOf("Service")>-1) {
			cName = "Service : ";
		}else if(type.indexOf("Dao")>-1) {
			cName = "Dao : ";
		}
	
		logger.info("[Before] "+ cName+ type+"."+ methodName+"()");
		
		Object obj = join.proceed();// 전과 후를 나누는 기준 
		
		logger.info("[After] "+ cName+ type+"."+ methodName+"()");
		return obj;
	}
}
