package org.nirvana.server.autoconfig;

import org.nirvana.server.filter.SelfGatewayServletFilter;
import org.nirvana.server.rule.RouteLocator;
import org.nirvana.server.rule.SimpleRouteLocator;
import org.nirvana.server.service.ServiceRegister;
import org.nirvana.server.service.SimpleServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gzm
 * @date 2021/2/3 9:40 上午
 * @desc: 自动装配配置类: 在这个类中做SelfGateway 的模块初始化的功能
 */
@Configuration
@EnableConfigurationProperties({ SelfGatewayProperties.class })
@ConditionalOnBean(SelfGatewayServerMarkerConfiguration.Marker.class)
public class SelfGatewayAutoConfiguration {

    @Autowired
    private SelfGatewayProperties selfGatewayProperties;

    /**
     * 注入全局web 请求的额拦截filter
     */
    @Bean
    public SelfGatewayServletFilter selfGatewayServletFilter(RouteLocator routeLocator) {
        return new SelfGatewayServletFilter(routeLocator);
    }

    @Bean
    @ConditionalOnMissingBean(SimpleRouteLocator.class)
    public SimpleRouteLocator simpleRouteLocator() {
        return new SimpleRouteLocator(selfGatewayProperties);
    }

    @Bean
    @ConditionalOnMissingBean(SimpleServiceLocator.class)
    public SimpleServiceLocator simpleServiceLocator(ScheduledExecutorService threadPools) {
        return new SimpleServiceLocator(threadPools);
    }

    @Bean
    @ConditionalOnMissingBean(ScheduledExecutorService.class)
    public ScheduledExecutorService threadPools() {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);
        return threadPool;
    }

    /**
     * 注册当前的服务信息到配置中心: 抽离配置中心的处理方法的时候可以通过抽离一个接口。 然后通过接口控制具体的实现
     * @return
     */
    @Bean
    public ServiceRegister serviceRegister() {
        return new ServiceRegister();
    }
}
