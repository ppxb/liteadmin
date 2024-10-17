package com.ppxb.la.base.listener;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.URLUtil;
import com.ppxb.la.base.common.code.ErrorCodeRegister;
import com.ppxb.la.base.common.enumeration.SystemEnvironmentEnum;
import com.ppxb.la.base.common.util.EnumUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(value = 1000)
public class WebServiceListener implements ApplicationListener<WebServerInitializedEvent> {

    @Value("${reload.interval-seconds}")
    private Integer intervalSeconds;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        var context = event.getApplicationContext();
        showProjectMessage(event);
    }

    private void showProjectMessage(WebServerInitializedEvent event) {
        var context = event.getApplicationContext();
        var env = context.getEnvironment();

        var ip = NetUtil.getLocalhost().getHostAddress();
        var port = event.getWebServer().getPort();
        var contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        var profile = env.getProperty("spring.profiles.active");
        var environmentEnum = EnumUtil.getEnumByValue(profile, SystemEnvironmentEnum.class);
        var projectName = env.getProperty("project.name");
        var title = String.format("-------------[%s] 服务已成功启动 （%s started successfully）-------------", projectName, projectName);

        var codeCount = ErrorCodeRegister.initialize();
        var localhostUrl = URLUtil.normalize(String.format("http://localhost:%d%s", port, contextPath), false, true);
        var externalUrl = URLUtil.normalize(String.format("http://%s:%d%s", ip, port, contextPath), false, true);
        var swaggerUrl = URLUtil.normalize(String.format("http://localhost:%d%s/swagger-ui/index.html", port, contextPath), false, true);
        var knife4jUrl = URLUtil.normalize(String.format("http://localhost:%d%s/doc.html", port, contextPath), false, true);
        log.warn("""
                        
                        {}
                        \t当前启动环境:\t{},{}\
                        
                        \t返回码初始化:\t完成{}个返回码初始化\
                        
                        \t服务本机地址:\t{}\
                        
                        \t服务外网地址:\t{}\
                        
                        \tSwagger地址:\t{}\
                        
                        \tknife4j地址:\t{}\
                        
                        -------------------------------------------------------------------------------------
                        """,
                title, profile, environmentEnum.getDesc(), codeCount, localhostUrl, externalUrl, swaggerUrl, knife4jUrl);
    }
}
