package org.nirvana.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author gzm
 * @date 2021/2/7 2:57 下午
 * @desc
 */
public class IpUtils {
    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address.getHostAddress();
    }
}
