package org.nirvana.entity;

/**
 * @author gzm
 * @date 2021/2/3 11:37 上午
 * @desc
 */
public class Route {
    /**
     * 路径: 设置拦截的服务请求的路径: eg: /service/** -> 所有访问网关的 /localhost:port/service/** 的请求均转发到 serviceId 上的同请求路径下
     */
    private String path;
    /**
     * 服务名id: 服务注册中心的服务id[即注册到服务注册中心的服务名]
     */
    private String serviceId;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
