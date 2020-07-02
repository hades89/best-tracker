package com.example.demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogExecutionTimeAspect {

	@Around("@annotation(LogExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object proceed = joinPoint.proceed();
		stopWatch.stop();
		System.out.println(joinPoint.getSignature() + " executed in " + stopWatch.getLastTaskTimeMillis() + "ms");
		return proceed;

	}



	@After("execution(* com.example.demo.ResourceService.getName(String))")
	public void abc(JoinPoint joinPoint){
		System.out.println("here?");

		for (Object object : joinPoint.getArgs()) {
			System.out.println("hehe: "+ object);
		}
	}
}
