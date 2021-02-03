package org.nirvana.server.rule;

import org.nirvana.entity.Route;

import java.util.List;

/**
 * @author gzm
 * @date 2021/2/3 4:03 下午
 * @desc: 获取路由信息
 */
public interface RouteLocator {

    List<Route> getRoutes();
}
