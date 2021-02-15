package org.nirvana.server.rule;

import org.nirvana.entity.Route;
import org.nirvana.server.autoconfig.SelfGatewayProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author gzm
 * @date 2021/2/3 4:05 下午
 * @desc
 */
public class SimpleRouteLocator implements ApplicationListener<ContextRefreshedEvent>, RouteLocator {
    private Logger logger = LoggerFactory.getLogger(SimpleRouteLocator.class);

    private SelfGatewayProperties properties;
    AtomicReference<Map<String, Route>> routes = new AtomicReference<>();
    ScheduledExecutorService threadPool;
    // 线程池中的任务的初始延迟
    private Long initialDelay = 1000 * 1L;
    // 规律延迟
    private Long delay = 1000 * 10L;

    public SimpleRouteLocator(ScheduledExecutorService threadPool, SelfGatewayProperties properties) {
        logger.info("配置文件中配置的路由规则: "+properties.getRoutes().toString());

        this.threadPool = threadPool;
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

    /**
     * 新建定时任务刷新路由规则的信息[可以抽离到abstract 类中执行]
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info(" 路由规则刷新到本地缓存开始");

        // 执行成功之后再间隔delay 之后再启动下一个任务执行
        threadPool.scheduleWithFixedDelay(() -> {
            // 刷新路由规则到本地缓存中。
            // 可以抽离一个接口让开发者具体实现。

        }, initialDelay, delay, TimeUnit.MILLISECONDS);
    }
}
