package com.ren;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ren
 */

public class R02_URL {

    public static void main(String[] args) throws MalformedURLException {
        URL imooc = new URL("http://imooc.com");
        // ?后面是参数  #后面是锚点
        URL url = new URL(imooc,"/index.html?username=tom#test");
        System.out.println("协议: "+url.getProtocol());
        System.out.println("主机: "+url.getHost());

        //如果未指定端口号，则使用默认的端口号
        System.out.println("端口: "+url.getPort());
        System.out.println("文件路径: "+url.getPath());
        System.out.println("文件名: "+url.getFile());
        System.out.println("相对路径: "+url.getRef());
        System.out.println("查询字符串: "+url.getQuery());


    }

}
