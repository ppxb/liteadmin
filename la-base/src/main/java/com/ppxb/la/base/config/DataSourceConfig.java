package com.ppxb.la.base.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.ppxb.la.base.handler.MybatisPlusFillHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.initial-size}")
    private int initialSize;

    @Value("${spring.datasource.min-idle}")
    private int minIdle;

    @Value("${spring.datasource.max-active}")
    private int maxActive;

    @Value("${spring.datasource.max-wait}")
    private long maxWait;

    @Value("${spring.datasource.time-between-eviction-runs-millis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.min-evictable-idle-time-millis}")
    private long minEvictableIdleTimeMillis;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Value("${spring.datasource.druid.username}")
    private String druidUserName;

    @Value("${spring.datasource.druid.password}")
    private String druidPassword;

    @Value("${spring.datasource.druid.login.enabled}")
    private boolean druidLoginEnabled;

    @Value("${spring.datasource.druid.method.pointcut}")
    private String methodPointCut;

    @Resource
    private MybatisPlusInterceptor paginationInterceptor;

//    @Resource
//    private DataScopePlugin dataScopePlugin;

    @Bean
    @Primary
    public DataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDbType(DbType.MYSQL.getDb());
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery("SELECT 1");

        try {
            dataSource.setFilters(filters);
            dataSource.setProxyFilters(List.of(createStateFilter()));
            dataSource.init();
        } catch (SQLException e) {
            log.error("初始化数据源错误", e);
        }

        return dataSource;
    }

    private StatFilter createStateFilter() {
        StatFilter statFilter = new StatFilter();
        statFilter.setMergeSql(true);
        statFilter.setLogSlowSql(true);
        statFilter.setSlowSqlMillis(1000);
        return statFilter;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(druidDataSource());

        try {
            factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:mapper/*.xml"));
        } catch (IOException e) {
            log.error("设置mapper路径失败", e);
            throw new RuntimeException("无法设置 mapper 文件路径", e);
        }

        List<Interceptor> plugins = new ArrayList<>();
        plugins.add(paginationInterceptor);
//        if (dataScopePlugin != null) {
//            plugins.add(dataScopePlugin);
//        }
        factoryBean.setPlugins(plugins.toArray(new Interceptor[0]));
        factoryBean.setGlobalConfig(new GlobalConfig()
                .setBanner(false)
                .setMetaObjectHandler(new MybatisPlusFillHandler()));

        return factoryBean.getObject();
    }

    @Bean
    @Conditional(SystemEnvironmentConfig.class)
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new StatViewServlet());
        bean.addUrlMappings("/druid/*");
        HashMap<String, String> initParameters = new HashMap<>();
        if (druidLoginEnabled) {
            initParameters.put("loginUsername", druidUserName);
            initParameters.put("loginPassword", druidPassword);
        }
        initParameters.put("resetEnable", "false");
        bean.setInitParameters(initParameters);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());
        bean.addUrlPatterns("/*");
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/*");
        return bean;
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor() {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPattern(methodPointCut);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(new DruidStatInterceptor());
        return advisor;
    }
}
