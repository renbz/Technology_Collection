package com.ren;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Ren
 * 基于TCP协议的Socket通信，实现用户登录
 * 服务器端
 */

public class R04_TCP_Thread_Server_Main {
    public static void main(String[] args) throws IOException {
        //1. 创建一个服务器端的socket，即 serverSocket，指定绑定的端口，并监听此端口
        ServerSocket serverSocket = new ServerSocket(8888);
        //2. 调用accept方法开始监听，等待客户端连接
        System.out.println("****服务器即将启动，等待客户端的连接***");
        Socket socket = null;
        // 循环监听等待客户端连接
        while (true){
            // 调用accept()方法开始监听，等待客户端的连接
            socket = serverSocket.accept();
            // 创建一个新的线程
            R04_TCP_Thread_Server serverThread = new R04_TCP_Thread_Server(socket);
            // 启动线程
            serverThread.start();
        }
    }
}
