package com.ren;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ren
 */

public class R03_URL_ReadNetSource {



    public static void main(String[] args) throws Exception {
        // 创建一个url实例
        URL url = new URL("http://www.baidu.com");
        //通过URL的openStream方法获取URL对象所表示的资源的字节输入流
        InputStream is = url.openStream();
        // 将字节输入流转换为字符输入流
        InputStreamReader isr = new InputStreamReader(is);
        // 为字符输入流添加缓冲
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.readLine();
        while (data != null) {
            System.out.println(data);
            data=reader.readLine();
        }
        reader.close();
        isr.close();



    }

}
