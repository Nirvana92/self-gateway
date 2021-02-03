package org.nirvana.server.service;

import org.nirvana.service.ServiceInstanceMetadata;

import java.util.List;

/**
 * @author gzm
 * @date 2021/2/3 5:01 下午
 * @desc
 */
public interface ServiceLocator {
    /**
     * 获取所有的服务列表
     * @return
     */
    List<ServiceInstanceMetadata> getServices();

    /**
     * 根据服务id拿到服务配置的列表
     * @param serviceId
     * @return
     */
    List<ServiceInstanceMetadata> getServices(String serviceId);
}
