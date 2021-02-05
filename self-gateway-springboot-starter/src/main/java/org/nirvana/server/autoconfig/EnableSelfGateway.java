package org.nirvana.server.autoconfig;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gzm
 * @date 2021/2/3 10:10 上午
 * @desc: 引入Marker 标记类来进行SelfGateway 的自动装配
 *
 * Target=ElementType.TYPE: 设置可以放在类接口上
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SelfGatewayServerMarkerConfiguration.class)
public @interface EnableSelfGateway {
}
