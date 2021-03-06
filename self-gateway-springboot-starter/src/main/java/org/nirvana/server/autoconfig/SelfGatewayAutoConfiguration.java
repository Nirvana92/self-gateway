package org.nirvana.server.autoconfig;

import org.nirvana.server.filter.SelfGatewayServletFilter;
import org.nirvana.server.rule.RouteLocator;
import org.nirvana.server.rule.SimpleRouteLocator;
import org.nirvana.server.service.NacosServiceInfoRegister;
import org.nirvana.server.service.ServiceLocator;
import org.nirvana.server.service.SimpleServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author gzm
 * @date 2021/2/3 9:40 上午
 * @desc: 自动装配配置类: 在这个类中做SelfGateway 的模块初始化的功能
 */
@Configuration
@EnableConfigurationProperties({SelfGatewayProperties.class})
@ConditionalOnBean(SelfGatewayServerMarkerConfiguration.Marker.class)
public class SelfGatewayAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(SelfGatewayAutoConfiguration.class);

    @Autowired
    private SelfGatewayProperties selfGatewayProperties;

    /**
     * 注入全局web 请求的额拦截filter
     */
    @Bean
    public SelfGatewayServletFilter selfGatewayServletFilter(RouteLocator routeLocator, ServiceLocator serviceLocator) {
        return new SelfGatewayServletFilter(routeLocator, serviceLocator);
    }

    @Bean
    @ConditionalOnMissingBean(SimpleRouteLocator.class)
    public SimpleRouteLocator simpleRouteLocator(ScheduledExecutorService threadPools) {
        SimpleRouteLocator simpleRouteLocator = new SimpleRouteLocator(threadPools, selfGatewayProperties);
        logger.info("注入到路由规则中的规则信息: " + simpleRouteLocator.getRoutes());
        return simpleRouteLocator;
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
     *
     * @return
     */
    @Bean
    public NacosServiceInfoRegister serviceRegister() {
        return new NacosServiceInfoRegister();
    }
}
