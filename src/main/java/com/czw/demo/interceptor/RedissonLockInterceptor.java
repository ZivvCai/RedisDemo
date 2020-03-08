package com.czw.demo.interceptor;

import com.czw.demo.annotation.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redisson注解拦截器
 *
 * @author caizw
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class RedissonLockInterceptor {

    @Resource
    private RedissonClient redissonClient;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
        Object obj = null;
        //方法内的所有参数
        Object[] params = joinPoint.getArgs();
        int lockIndex = redissonLock.lockIndex();
        //取得方法名
        String key = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        //-1代表锁整个方法，而非具体锁哪个参数
        if (lockIndex != -1) {
            key += params[lockIndex];
        }
        //多久会自动释放，默认10秒
        int leaseTime = redissonLock.leaseTime();
        int waitTime = 5;
        RLock rLock = redissonClient.getLock(key);
        if (rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
            try {
                log.info("取到锁");
                obj = joinPoint.proceed();
                log.info("释放锁");
            } catch (InterruptedException e) {
                log.info(e.getMessage());
                e.printStackTrace();
            } finally {
                rLock.unlock();
                log.info("成功释放锁");
            }
        } else {
            log.info("没有获得锁");
            throw new RuntimeException("没有获得锁");
        }
        return obj;
    }


}
