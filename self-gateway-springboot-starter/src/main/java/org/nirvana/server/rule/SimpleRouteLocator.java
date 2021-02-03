package org.nirvana.server.rule;

import org.nirvana.entity.Route;
import org.nirvana.server.autoconfig.SelfGatewayProperties;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author gzm
 * @date 2021/2/3 4:05 下午
 * @desc
 */
public class SimpleRouteLocator implements RouteLocator {
    private SelfGatewayProperties properties;
    AtomicReference<Map<String, Route>> routes = new AtomicReference<>();

    public SimpleRouteLocator(SelfGatewayProperties properties) {
        this.properties = properties;
    }

    @Override
    public List<Route> getRoutes() {
        if(routes.get() == null) {
            routes.set(refreshRoute());
        }

        return routes.get().values().stream().collect(Collectors.toList());
    }

    Map<String, Route> refreshRoute() {
        return properties.getRoutes();
    }
}
