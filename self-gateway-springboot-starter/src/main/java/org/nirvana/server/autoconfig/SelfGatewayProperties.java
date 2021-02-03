package org.nirvana.server.autoconfig;

import org.nirvana.entity.Route;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.HashMap;
import java.util.Map;

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
    /**
     * 路由规则
     */
    private Map<String, Route> routes = new HashMap<>();

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }
}
