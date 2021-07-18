package com.ren.lesson01;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Ren
 */


// 测试ip
public class R01_InterAddress {
    public static void main(String[] args) {
        try {
            // 查询本机ip地址
            InetAddress inetAddress1 = InetAddress.getByName("127.0.0.1");
            System.out.println(inetAddress1);
            InetAddress inetAddress3 = InetAddress.getLocalHost();
            System.out.println(inetAddress3);
            InetAddress inetAddress4 = InetAddress.getByName("localhost");
            System.out.println(inetAddress4);

            //查询网站ip地址
            InetAddress inetAddress2 = InetAddress.getByName("www.baidu.com");
            System.out.println(inetAddress2);

            System.out.println("常用方法----");
            System.out.println(inetAddress2.getAddress());
            System.out.println(inetAddress2.getHostAddress());
            System.out.println(inetAddress2.getCanonicalHostName());
            System.out.println(inetAddress2.getHostName());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
