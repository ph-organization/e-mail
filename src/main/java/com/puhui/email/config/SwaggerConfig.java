package com.puhui.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author: 邹玉玺
 * @date: 2020/7/3-14:19
 */
@Configuration
@EnableSwagger2   //开启swagger2
public class SwaggerConfig {


    //配置swagger的docket的bean实例
    @Bean
    public Docket docket1(Environment environment) {
//        //设置要显示的swagger环境
//        Profiles profiles = Profiles.of("dev");
//        //通过environment.acceptsProfiles判断是否处在自己设定的环境当中
//        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(ylhApiInfo())
                .groupName("杨利华_用户crud")
                //enable(false) 关闭swagger 不能在浏览器中访问，默认是启动的（true）
                .enable(true)
                .select()
                //RequestHandlerSelectors配置要扫描接口的方式
                //basePackage("包名")：指定要扫描的包
                //any：扫描全部
                //none:不扫描
                //withClassAnnotation(RestController.class) 扫描类上的注解
                //withMethodAnnotation(GetMapping.class) 扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("com.puhui.email.controller"))
                //paths() 过滤某某路径（让过滤的路径通过）
                .paths(PathSelectors.ant("/mailUser/**"))
                .build();
    }

    @Bean
    public Docket docket2(Environment environment) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(ylhApiInfo())
                .groupName("杨利华_角色crud_角色与用户关系的添加和地删除")
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.puhui.email.controller"))
                .paths(PathSelectors.ant("/role/**"))
                .build();
    }

    //配置swagger信息apiInfo
    public ApiInfo ylhApiInfo() {
        //作者信息
        Contact contact = new Contact("杨利华", "http://www.baidu.com", "Y2235662296@163.com");
        return new ApiInfo(
                "杨利华的swaggerAPI文档",
                "这狗东西居然写api文档了",
                "1.0",
                "http://www.baidu.com",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }

    @Bean
    public Docket getDocket(Environment environment) {
//        //设置需要显示的swagger环境
//        Profiles profiles=Profiles.of("dev");
//        //获取项目环境    判断是否处在自己设定的环境
//        boolean flag = environment.acceptsProfiles(profiles);
//        System.out.println(flag);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .enable(true)//是否开启swagger
                .select()
                //配置要扫描接口的方式
                /**
                 * basePackage()  指定包扫描  （通常用这个）
                 * any     扫描全部
                 * none      全部不扫描
                 * withMethodAnnotation     扫描方法上的注解（参数是一个注解的反射对象）
                 * withClassAnnotation        扫描类上的注解（）
                 */
                .apis(RequestHandlerSelectors.basePackage("com.puhui.email.controller"))
                .paths(PathSelectors.ant("/mail/**"))//只扫描mail有关的接口
                .build()
                .groupName("邹玉玺");


    }

    //配置api信息
    private ApiInfo getApiInfo() {
        //作者信息
        Contact contact = new Contact("邹玉玺", "https://user.qzone.qq.com/1003941268/infocenter", "1003941268@qq.com");
        return new ApiInfo("邮件发送的swagger API文档",
                "Api 描述",       //描述
                "1.0",     //版本
                "urn:https://user.qzone.qq.com/1003941268/infocenter",     //地址
                contact,     //作者信息
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList()
        );
    }
}
