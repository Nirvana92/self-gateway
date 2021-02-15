package org.nirvana.service;

import java.util.Objects;

/**
 * @author gzm
 * @date 2021/2/3 11:04 上午
 * @desc: 服务实例的元数据信息
 */
public class ServiceInstanceMetadata {
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 服务地址
     */
    private String host;
    /**
     * 服务端口
     */
    private Integer port;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceInstanceMetadata that = (ServiceInstanceMetadata) o;
        return Objects.equals(host, that.host) &&
                Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
