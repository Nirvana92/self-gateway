package org.nirvana.server.autoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gzm
 * @date 2021/2/3 10:04 上午
 * @desc: 引入self-gateway 的自动装配的标记类
 *
 * 参考: {@link SelfGatewayAutoConfiguration}
 */
@Configuration
public class SelfGatewayServerMarkerConfiguration {
    @Bean
    public Marker selfGatewayServerMarkerBean() {
        return new Marker();
    }

    class Marker { }
}
