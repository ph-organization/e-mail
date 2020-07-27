package com.puhui.email;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@EnableCaching
@ComponentScan("com.puhui.email")
@MapperScan(value = "com.puhui.email.mapper")
@EnableTransactionManagement
@EnableAsync
public class EMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(EMailApplication.class, args);
    }

}
