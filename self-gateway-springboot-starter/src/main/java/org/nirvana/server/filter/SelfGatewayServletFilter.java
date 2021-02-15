package org.nirvana.server.filter;

import org.nirvana.entity.Route;
import org.nirvana.server.rule.RouteLocator;
import org.nirvana.server.service.ServiceLocator;
import org.nirvana.server.utils.HttpUtils;
import org.nirvana.service.ServiceInstanceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

/**
 * @author gzm
 * @date 2021/2/3 10:31 上午
 * @desc: 整个架构的web 请求的拦截器
 */
public class SelfGatewayServletFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(SelfGatewayServletFilter.class);
    private RouteLocator routeLocator;
    private ServiceLocator serviceLocator;

    public SelfGatewayServletFilter(RouteLocator routeLocator, ServiceLocator serviceLocator) {
        this.routeLocator = routeLocator;
        this.serviceLocator = serviceLocator;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info(" 整个网关的全局过滤器 ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 需要获取数据库中配置的路由规则
        List<Route> routes = routeLocator.getRoutes();
        // 然后根据请求的uri 进行规则匹配
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        // 拿到服务注册中心的服务元数据信息
        routes.forEach(route -> {
            logger.info("--uri: " + uri);
            // boolean matches = Pattern.matches(route.getPath(), uri);
            // logger.info("path: " + route.getPath() + ", matches: " + matches);
            if (uri.startsWith(route.getPath().replace("*", ""))) {
                List<ServiceInstanceMetadata> services = serviceLocator.getServices(route.getServiceId());
                ServiceInstanceMetadata serviceInstanceMetadata = services.get(0);

                // 根据匹配到的服务信息和服务注册中心的服务元数据进行服务调用
                logger.info("获取到的服务元数据信息: " + serviceInstanceMetadata);
                String tmpUri = uri.replace(route.getPath().replace("*", ""), "");
                if (!tmpUri.startsWith("/")) {
                    tmpUri = "/" + tmpUri;
                }
                String url = "http://" + serviceInstanceMetadata.getHost() + ":" + serviceInstanceMetadata.getPort() + tmpUri;
                String rsp = "";
                if (Objects.equals(req.getMethod(), HttpMethod.GET.name())) {
                    rsp = HttpUtils.get(url);
                } else {
                    // post. 简单的处理方式
                    // req.getParameterMap().
                    rsp = HttpUtils.post(url, "");
                }

                try {
                    System.out.println(" rsp: " + rsp);
                    PrintWriter writer = response.getWriter();
                    writer.write(rsp);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        });

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("销毁方法执行");
    }
}
