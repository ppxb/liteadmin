package com.ppxb.la.admin;

import com.ppxb.la.base.listener.LogVariableListener;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan(ApplicationBootstrap.COMPONENT_SCAN)
@MapperScan(value = ApplicationBootstrap.COMPONENT_SCAN, annotationClass = Mapper.class)
@SpringBootApplication
public class ApplicationBootstrap {

    public static final String COMPONENT_SCAN = "com.ppxb.la";

    public static void main(String[] args) {
        var app = new SpringApplication(ApplicationBootstrap.class);
        app.addListeners(new LogVariableListener());
        app.run(args);
    }
}
