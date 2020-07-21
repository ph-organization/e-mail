
package com.puhui.email.config;


import com.puhui.email.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;


/**
 * @description:
 * @author: 杨利华
 * @date: 2020/7/9
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    LoginService loginService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new DefinitionSecurityPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService);
//        auth.inMemoryAuthentication().withUser("admin").password("123456").roles("admin");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**")
                .hasRole("superAdmin")
                //访问/mailUser/**","/role/**需要admin权限
                .antMatchers("/mailUser/**")
                .hasRole("admin")
                .antMatchers("/mailUser/user/*")
                .hasRole("user")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login").permitAll()
                .and()
                //关闭csrf
                .csrf()
                .disable();
    }
}

