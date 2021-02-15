package org.nirvana.server.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import org.nirvana.service.ServiceInstanceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Long delay = 1000 * 60L;

    @NacosInjected
    NamingService namingService;

    public SimpleServiceLocator(ScheduledExecutorService threadPool) {
        this.threadPool = threadPool;
        serviceInstanceMaps.set(new HashMap<>());
        logger.info(" 启动定时任务 获取服务列表. {}", namingService);

        threadPool.scheduleWithFixedDelay(() -> {
            // 去服务注册中心刷新服务
            logger.info("开始拉取服务列表");
            ListView<String> services = null;

            try {
                // defaultGroupServices = namingService.getServicesOfServer(0, 10, "DEFAULT_GROUP");
                // 获取服务名,  这种可以循环往后分区获取
                services = namingService.getServicesOfServer(0, 100);
                for (String serviceName : services.getData()) {
                    logger.info("服务信息: {}", serviceName);
                    List<Instance> instances = namingService.getAllInstances(serviceName);
                    // logger.info(serviceName + " instances: " + instances.size());
                    if (!CollectionUtils.isEmpty(instances)) {
                        // 更新本地缓存中的服务数据
                        List<ServiceInstanceMetadata> instanceMetadatas = new ArrayList<>();
                        instances.forEach(instance -> {
                            ServiceInstanceMetadata instanceMetadata = new ServiceInstanceMetadata();
                            instanceMetadata.setServiceName(serviceName);
                            instanceMetadata.setHost(instance.getIp());
                            instanceMetadata.setPort(instance.getPort());

                            instanceMetadatas.add(instanceMetadata);
                        });
                        logger.info(" instanceMetadatas: " + instanceMetadatas + ", maps: " + serviceInstanceMaps.get());
                        serviceInstanceMaps.get().put(serviceName, instanceMetadatas);
                        logger.info("刷新的服务实例元数据信息: " + serviceInstanceMaps);
                    }
                }
            } catch (NacosException e) {
                // e.printStackTrace();
                logger.error(" 获取Nacos 服务数据失败. errorMessage: {}", e.getErrMsg());
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
