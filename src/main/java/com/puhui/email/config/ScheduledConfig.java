package com.puhui.email.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * @author: 邹玉玺
 * @date: 2020/7/8-11:19
 * 自定义线程池
 */
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //配置多线程并发处理，
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(50));
    }
}
