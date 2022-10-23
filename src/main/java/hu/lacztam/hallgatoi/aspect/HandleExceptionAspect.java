package hu.lacztam.hallgatoi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.aspectj.weaver.Advice;


@Aspect
@Component
public class HandleExceptionAspect {

	//@Pointcut("execution(* hu.lacztam.hallgatoi.service.PseudoCentralSystem.mock_usedFreeSemesterByStudent(..)) && args(int) ")
	@Pointcut("@annotation(hu.lacztam.hallgatoi.aspect.SecureMethodCall) || @within(hu.lacztam.hallgatoi.aspect.SecureMethodCall)")
	public void annotationSecureMethodCall() {

	}

	//@Around("hu.lacztam.hallgatoi.aspect.HandleExceptionAspect.annotationSecureMethodCall()")
	@Around("execution(* hu.lacztam.hallgatoi.service.*.*(..))")
	public Object tryAgainMethodCall(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("\ntryAgainMethodCall() called");
		try {
			System.out.println("pjp.getSignature().getName():" + pjp.getSignature().getName());
			pjp.proceed();
			return pjp.proceed();
		} catch (Exception e) {
			System.out.println("exception: " + e.getMessage());
		}
		return null;
	}
	
	@Pointcut("@annotation(hu.lacztam.hallgatoi.aspect.CheckThrow) || @within(hu.lacztam.hallgatoi.aspect.CheckThrow)")
	public void annotationExCheckCall() { }
	
	@Before("hu.lacztam.hallgatoi.aspect.HandleExceptionAspect.annotationExCheckCall()")
	public void checkEx(JoinPoint joinPoint) {
		Class<? extends Object> clazz = joinPoint.getTarget().getClass();
		Class<?>[] interfaces = clazz.getInterfaces();
		String targetType = interfaces.length == 0 ? clazz.getName() : interfaces[0].toString();
		if(interfaces.length > 1)
		
		System.out.println( interfaces[1].toString() + "");
		
		System.out.format("Method %s called in class %s%n", joinPoint.getSignature(), targetType);
	}
	
}


