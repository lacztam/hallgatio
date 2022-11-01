package hu.lacztam.hallgatoi.aspect;

import java.lang.reflect.Method;

import javax.naming.CannotProceedException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetryAspect {

	@Pointcut("@annotation(hu.lacztam.hallgatoi.aspect.Retry) || @within(hu.lacztam.hallgatoi.aspect.Retry)")
	public void retryPointCut() {
	}

	// @Around("execution(*
	// hu.lacztam.hallgatoi.service.CentralSystemService.mock_usedFreeSemesterByStudent(..))")
	@Around("retryPointCut()")
	public Object tryAgainMethodCall(ProceedingJoinPoint pjp) throws Throwable {

		Retry retry = null;
		Signature signature = pjp.getSignature();

		if (signature instanceof MethodSignature) {
			MethodSignature methodSignature = (MethodSignature) signature;
			Method method = methodSignature.getMethod();
			retry = method.getAnnotation(Retry.class);
		} else {
			Class<?> declaringType = signature.getDeclaringType();
			retry = declaringType.getAnnotation(Retry.class);
		}

		int times = retry.times();
		long waitTime = retry.waitTime();

		if (times <= 0) {
			times = 1;
		}
		
		for(int numTry = 1; numTry <= times; numTry++) {
			System.out.format("Try times: %d %n", numTry);
			try {
				return pjp.proceed();
			}catch (Exception e) {
				if(numTry == times)
					throw e;
				
				if(waitTime > 0) 
					Thread.sleep(waitTime);
			}
		}
		throw new CannotProceedException();
		
//		try {
//			System.out.println("\t\ttryAgainMethodCall trying to save ");
//			Object proceed = pjp.proceed();
//			System.out.println("\t\ttryAgainMethodCall pjp.proceed successfully");
//			return proceed;
//		} catch (Exception e) {
//
//			for (int i = 0; i < 5; i++) {
//				try {
//					System.out.println("\t\t\t" + i + "/5 try, in catch tryAgainMethodCall()");
//					Object proceed = pjp.proceed();
//					System.out.println("\t\t\t" + i + "/5 try, tryAgainMethodCall pjp.proceed successfully");
//					return proceed;
//				} catch (Exception ex) {
//				}
//			}
//		}
		
	}

}
