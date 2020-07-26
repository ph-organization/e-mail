package com.puhui.email.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * @author: 邹玉玺
 * @date: 2020/7/8-11:19
 * 自定义线程池
 */
@Configuration
@Slf4j
public class ScheduledConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //配置多线程并发处理，
        scheduledTaskRegistrar.setScheduler(taskScheduler());
    }

    @Bean (destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("dispatch-");
        scheduler.setAwaitTerminationSeconds(600);
        scheduler.setErrorHandler(throwable -> log.error("调度任务发生异常", throwable));
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

}
