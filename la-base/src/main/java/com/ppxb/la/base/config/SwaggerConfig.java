package com.ppxb.la.base.config;

import com.google.common.collect.Lists;
import com.ppxb.la.base.common.util.StringUtil;
import com.ppxb.la.base.constant.RequestHeaderConst;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@Conditional(SystemEnvironmentConfig.class)
public class SwaggerConfig {

    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/swagger-ui.html/**",
            "/doc.html",
    };

    @Value("${springdoc.swagger-ui.server-base-url:''}")
    private String serverBaseUrl;

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(RequestHeaderConst.TOKEN, new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(RequestHeaderConst.TOKEN)))
                .info(new Info()
                        .title("Lite Admin 接口文档")
                        .contact(new Contact()
                                .name("ppxb")
                                .email("ppxb123@gmail.com")
                                .url("https://github.com/ppxb"))
                        .version("v1.0")
                        .description("开箱即用的中后台管理系统")
                )
                .addSecurityItem(new SecurityRequirement().addList(RequestHeaderConst.TOKEN));
    }

    @Bean
    public OpenAPIService openAPIBuilder(
            Optional<OpenAPI> openAPI,
            SecurityService securityParser,
            SpringDocConfigProperties springDocConfigProperties,
            PropertyResolverUtils propertyResolverUtils,
            Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomizers,
            Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomizers,
            Optional<JavadocProvider> javadocProvider
    ) {
        var list = Lists.newArrayList((ServerBaseUrlCustomizer) baseUrl -> {
            if (StringUtil.isNotBlank(serverBaseUrl)) {
                return serverBaseUrl;
            }
            return baseUrl;
        });
        return new OpenAPIService(openAPI, securityParser, springDocConfigProperties, propertyResolverUtils, openApiBuilderCustomizers, Optional.of(list), javadocProvider);
    }
}
