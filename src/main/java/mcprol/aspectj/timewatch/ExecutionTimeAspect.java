package mcprol.aspectj.timewatch;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Aspect
public class ExecutionTimeAspect {

    private Map<String, Long> functionStartTimes = new HashMap<>();
    private Map<String, List<Long>> functionExecTimes = new HashMap<>();

    @Before("@annotation(ExecutionTimeAnnotation)")
    public void logBeforeMethod(JoinPoint joinPoint) {
        String functionName = joinPoint.getSignature().getName();
        System.out.println("Logging - Method: " + functionName);
        long startTime = System.currentTimeMillis();
        WatcherUtil.addStartTime(functionName, startTime);
    }

    @After("@annotation(ExecutionTimeAnnotation)")
    public void logAfterMethod(JoinPoint joinPoint) {
        String functionName = joinPoint.getSignature().getName();
        long endTime = System.currentTimeMillis();
        WatcherUtil.saveExecTime(functionName, endTime);
    }


}
