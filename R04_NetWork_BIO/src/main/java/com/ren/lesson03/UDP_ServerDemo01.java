package com.ren.lesson03;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author Ren
 */

// 等待客户端的连接
public class UDP_ServerDemo01 {
    public static void main(String[] args) throws Exception {

        //开放端口
        DatagramSocket socket = new DatagramSocket(9090);

        //接收数据包
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer,0,buffer.length);  // 接收

        socket.receive(packet);  //阻塞接收

        System.out.println(packet.getAddress().getHostAddress());
        System.out.println(new String(packet.getData(),0, packet.getLength()));

        // 关闭连接
        socket.close();

    }

}
