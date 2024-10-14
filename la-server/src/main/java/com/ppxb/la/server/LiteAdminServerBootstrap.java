package com.ppxb.la.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan(LiteAdminServerBootstrap.COMPONENT_SCAN)
@SpringBootApplication
public class LiteAdminServerBootstrap {

    public static final String COMPONENT_SCAN = "com.ppxb.la";

    public static void main(String[] args) {
        SpringApplication.run(LiteAdminServerBootstrap.class, args);
    }
}
