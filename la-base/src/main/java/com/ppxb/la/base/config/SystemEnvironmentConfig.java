package com.ppxb.la.base.config;

import com.ppxb.la.base.common.domain.SystemEnvironment;
import com.ppxb.la.base.common.enumeration.SystemEnvironmentEnum;
import com.ppxb.la.base.common.util.EnumUtil;
import com.ppxb.la.base.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
public class SystemEnvironmentConfig implements Condition {

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${project.name}")
    private String projectName;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return false;
    }

    private boolean isDevOrTest(ConditionContext context) {
        var property = context.getEnvironment().getProperty("spring.profiles.active");
        return StringUtil.isNotBlank(property)
                && (SystemEnvironmentEnum.TEST.equalsValue(property) || SystemEnvironmentEnum.DEV.equalsValue(property));
    }

    @Bean("systemEnvironment")
    public SystemEnvironment initEnvironment() {
        var currentEnvironment = EnumUtil.getEnumByValue(environment, SystemEnvironmentEnum.class);
        if (currentEnvironment == null) {
            throw new ExceptionInInitializerError("无法获取当前环境，请在 application.yaml 中配置参数：spring.profiles.active");
        }
        if (StringUtil.isBlank(projectName)) {
            throw new ExceptionInInitializerError("无法获取项目名称，请在 application.yaml 中配置参数：project.name");
        }
        return new SystemEnvironment(currentEnvironment == SystemEnvironmentEnum.PROD, projectName, currentEnvironment);
    }
}
