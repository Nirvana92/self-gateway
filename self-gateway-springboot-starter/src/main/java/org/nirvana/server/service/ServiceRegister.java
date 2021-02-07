package org.nirvana.server.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.apache.commons.lang3.StringUtils;
import org.nirvana.util.IpUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

/**
 * @author gzm
 * @date 2021/2/3 5:20 下午
 * @desc: 将本地的服务注册到服务注册中心
 */
public class ServiceRegister implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    @NacosInjected
    private NamingService namingService;

    Environment environment;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 监控到了事件
        String port = environment.getProperty("server.port");
        // 服务id
        String applicationName = environment.getProperty("spring.application.name");

        Instance instance = new Instance();
        if(StringUtils.isNumeric(port)) {
            instance.setPort(Integer.parseInt(port));
        }
        instance.setIp(IpUtils.getLocalIpAddress());

        try {
            namingService.registerInstance(applicationName, instance);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        environment = applicationContext.getEnvironment();
    }
}
