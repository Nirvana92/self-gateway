package org.nirvana.server.service;

import org.nirvana.service.ServiceInstanceMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author gzm
 * @date 2021/2/3 5:02 下午
 * @desc
 */
public class SimpleServiceLocator implements ServiceLocator {

    AtomicReference<Map<String, List<ServiceInstanceMetadata>>> serviceInstanceMaps = new AtomicReference<>();
    ScheduledExecutorService threadPool;

    public SimpleServiceLocator(ScheduledExecutorService threadPool) {
        this.threadPool = threadPool;

        threadPool.schedule(()-> {
            // 去服务注册中心刷新服务

        }, 1000*10, TimeUnit.SECONDS);
    }

    @Override
    public List<ServiceInstanceMetadata> getServices() {
        return serviceInstanceMaps.get().values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<ServiceInstanceMetadata> getServices(String serviceId) {
        return serviceInstanceMaps.get().get(serviceId);
    }
}
