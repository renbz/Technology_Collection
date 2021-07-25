package com.ren;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Ren
 */

public class R05_UDP_Client {

    public static void main(String[] args) throws IOException {

        /**
         * 向服务器端发送数据
         */
        //1. 定义服务器的地址、端口号、数据
        InetAddress address = InetAddress.getByName("localhost");
        int port = 8800;
        byte[] data = "用户名: admin , 密码: 123".getBytes();
        //2. 创建数据包，包含发送的数据信息
        DatagramPacket datagramPacket = new DatagramPacket(data,data.length,address,port);
        //3. 创建DatagrameSocket对象
        DatagramSocket datagramSocket = new DatagramSocket();
        //4. 向服务器端发送数据报
        datagramSocket.send(datagramPacket);


        /**
         * 接收服务器端的响应数据
         */
        //1. 创建数据报，用于接收服务端响应的数据
        byte[]data2 = new byte[1024];
        DatagramPacket datagramPacket2 = new DatagramPacket(data2,data2.length);
        //2. 接收服务器响应的数据
        datagramSocket.receive(datagramPacket2);
        //3. 读取服务器端响应的数据信息
        String reply = new String(data2,0,datagramPacket2.getLength());
        System.out.println("我是客户端，服务器说: "+reply);
        //4. 关闭资源
        datagramSocket.close();

    }

}
