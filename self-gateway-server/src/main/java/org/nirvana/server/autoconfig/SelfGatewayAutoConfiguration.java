package org.nirvana.server.autoconfig;

import org.nirvana.server.filter.SelfGatewayServletFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gzm
 * @date 2021/2/3 9:40 上午
 * @desc: 自动装配配置类: 在这个类中做SelfGateway 的模块初始化的功能
 */
@Configuration
@EnableConfigurationProperties({ SelfGatewayProperties.class })
@ConditionalOnBean(SelfGatewayServerMarkerConfiguration.Marker.class)
public class SelfGatewayAutoConfiguration {

    /**
     * 注入全局web 请求的额拦截filter
     */
    @Bean
    public SelfGatewayServletFilter selfGatewayServletFilter() {
        return new SelfGatewayServletFilter();
    }
}
