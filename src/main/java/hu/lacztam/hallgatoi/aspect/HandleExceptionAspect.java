package hu.lacztam.hallgatoi.aspect;

import javax.naming.CannotProceedException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class HandleExceptionAspect {

	@Pointcut("@annotation(hu.lacztam.hallgatoi.aspect.SecureMethodCall) || @within(hu.lacztam.hallgatoi.aspect.SecureMethodCall)")
	public void annotationSecureMethodCall() {
	}

	// nem tudtam rátanni a mock_usedFreeSemesterByStudent(int) metódusra, sem az @AfterThrowing annotációval sem, sem ezzel
	@Around("execution(* hu.lacztam.hallgatoi.service.PseudoCentralSystemService.mock_usedFreeSemesterByStudent(..))")
	public Object tryAgainMethodCall(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("\ntryAgainMethodCall() called");
		try {
			System.out.println("pjp.getSignature().getName():" + pjp.getSignature().getName());
			pjp.proceed();
			return pjp.proceed();
		} catch (Exception e) {
			for(int i = 0; i < 5; i++) {
				try {
					pjp.proceed();
				} catch (Exception ex) { }
			}
		}
		
		throw new CannotProceedException();
	}
	
}


