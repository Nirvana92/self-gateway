package org.nirvana.server.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ListView;
import org.nirvana.service.ServiceInstanceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    Logger logger = LoggerFactory.getLogger(SimpleServiceLocator.class);

    AtomicReference<Map<String, List<ServiceInstanceMetadata>>> serviceInstanceMaps = new AtomicReference<>();
    ScheduledExecutorService threadPool;

    // 初始化的延迟时间
    private Long initialDelay = 1000 * 1L;
    // 往后每次的间隔延迟
    private Long delay = 1000 * 5L;

    @NacosInjected
    private NamingService namingService;

    public SimpleServiceLocator(ScheduledExecutorService threadPool) {
        this.threadPool = threadPool;

        logger.info(" 启动定时任务 获取服务列表. {}", namingService);

        threadPool.scheduleWithFixedDelay(()-> {
            // 去服务注册中心刷新服务
            logger.info("开始拉取服务列表");
            ListView<String> defaultGroupServices = null;

            try {
                defaultGroupServices = namingService.getServicesOfServer(0, 10, "DEFAULT_GROUP");
                for (String service : defaultGroupServices.getData()) {
                    logger.info("服务信息: {}", service);
                }
            } catch (NacosException e) {
                e.printStackTrace();
            }
        }, initialDelay, delay, TimeUnit.MILLISECONDS);
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
