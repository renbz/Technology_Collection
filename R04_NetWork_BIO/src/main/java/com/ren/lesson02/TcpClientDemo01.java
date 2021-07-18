package com.ren.lesson02;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Ren
 */

// 客户端
public class TcpClientDemo01 {

    public static void main(String[] args) {

        Socket socket = null;
        OutputStream os = null;
        try {
            // 1. 要知道服务器的地址，端口号
            InetAddress serverIp = InetAddress.getByName("127.0.0.1");
            int port = 9999;
            // 2. 创建一个socket连接
            socket = new Socket(serverIp, port);
            // 3. 发送消息流
            os = socket.getOutputStream();
            os.write("你好，欢迎学习网络编程".getBytes(StandardCharsets.UTF_8));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
