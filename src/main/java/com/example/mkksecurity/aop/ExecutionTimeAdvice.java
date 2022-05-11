package com.example.mkksecurity.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.enabled:true}")
public class ExecutionTimeAdvice {


	@Around("@annotation(ExecutionTime)")
	public Object executionTimeLog(ProceedingJoinPoint point) throws Throwable {

		long startTime = System.currentTimeMillis();

		Object object = point.proceed(); // methodun ilk girdiği yerde bekler!. methodu sonlandrı

		long endTime = System.currentTimeMillis();

		log.info("Class Name "+point.getSignature().getDeclaringTypeName()+" . Method Name :"+point.getSignature().getName() +" " +
				" Time taken for execution is :"+(endTime-startTime)+" ms");

		return object;

	}

}
