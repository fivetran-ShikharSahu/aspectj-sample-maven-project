package mcprol.aspectj.timewatch;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;

@Aspect
public class ExecutionTimeAspect {

    private long startTime;

    @Before("@annotation(ExecutionTimeAnnotation)")
    public void logBeforeMethod(JoinPoint joinPoint) {
        System.out.println("Logging - Method: " + joinPoint.getSignature().getName());
        startTime = System.currentTimeMillis();
    }

    @After("@annotation(ExecutionTimeAnnotation)")
    public void logAfterMethod(JoinPoint joinPoint) {
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("CCC -> JOin point -> " + joinPoint);
        System.out.println("Method execution time: " + executionTime + " milliseconds");
    }


}
