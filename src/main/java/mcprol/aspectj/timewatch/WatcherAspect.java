package mcprol.aspectj.timewatch;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import java.io.IOException;

@Aspect
public class WatcherAspect {
    @After("@annotation(Watcher)")
    public void watcherAfterMethod(JoinPoint joinPoint) throws IOException {
        WatcherUtil.publish();
    }
}
