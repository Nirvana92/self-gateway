package org.nirvana.server.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.HashMap;

/**
 * @author gzm
 * @date 2021/2/3 10:02 上午
 * @desc: self-gateway 的属性配置
 */
@ConfigurationProperties("self-gateway")
public class SelfGatewayProperties {
    /**
     * 前缀
     */
    private String prefix;

    // private Map<String, Route> routes = new HashMap<>();
}
