package com.ppxb.la.base.listener;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.URLUtil;
import com.ppxb.la.base.common.code.ErrorCodeRegister;
import com.ppxb.la.base.common.enumeration.SystemEnvironmentEnum;
import com.ppxb.la.base.common.util.EnumUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Order(value = 1000)
public class WebServiceListener implements ApplicationListener<WebServerInitializedEvent> {

    private static final String LOG_TEMPLATE = """
            
            🚀 {} started successfully 🎉 🎉 🎉
            🌍 Env: {}[{}]
            🔢 Return Codes: {} codes have been initialized
            🏠 Local: {}
            🌐 Web: {}
            📚 API Docs:
                Swagger: {}
                knife4j: {}
            """;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        showProjectMessage(event);
    }

    private void showProjectMessage(WebServerInitializedEvent event) {
        var env = event.getApplicationContext().getEnvironment();

        var localhost = NetUtil.getLocalhost();
        var port = event.getWebServer().getPort();

        var contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path")).orElse("");
        var profile = env.getProperty("spring.profiles.active");
        var environmentEnum = EnumUtil.getEnumByValue(profile, SystemEnvironmentEnum.class);
        var projectName = env.getProperty("project.name");

        var codeCount = ErrorCodeRegister.initialize();

        var localhostUrl = buildUrl("localhost", port, contextPath);
        var externalUrl = buildUrl(localhost.getHostAddress(), port, contextPath);
        var swaggerUrl = buildUrl("localhost", port, contextPath, "swagger-ui/index.html");
        var knife4jUrl = buildUrl("localhost", port, contextPath, "doc.html");


        log.warn(LOG_TEMPLATE, projectName, profile, environmentEnum.getDesc(), codeCount,
                localhostUrl, externalUrl, swaggerUrl, knife4jUrl);
    }

    private String buildUrl(String host, int port, String contextPath, String... additionalPaths) {
        var urlBuilder = new StringBuilder(String.format("http://%s:%d%s", host, port, contextPath));
        for (String path : additionalPaths) {
            urlBuilder.append("/").append(path);
        }
        return URLUtil.normalize(urlBuilder.toString(), false, true);
    }
}
