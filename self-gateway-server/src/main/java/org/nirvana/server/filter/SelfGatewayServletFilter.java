package org.nirvana.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author gzm
 * @date 2021/2/3 10:31 上午
 * @desc: 整个架构的web 请求的拦截器
 */
public class SelfGatewayServletFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(SelfGatewayServletFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化方法执行");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 需要获取数据库中配置的路由规则
        // 然后根据请求的uri 进行规则匹配
        // 拿到服务注册中心的服务元数据信息
        // 根据匹配到的服务信息和服务注册中心的服务元数据进行服务调用
    }

    void preRouting() {

    }

    void postRouting() {

    }

    void error() {

    }

    @Override
    public void destroy() {
        logger.info("销毁方法执行");
    }
}
