package com.ren;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Ren
 * 基于TCP协议的Socket通信，实现用户登录
 * 服务器端
 */

public class R04_TCP_Server {
    public static void main(String[] args) throws IOException {
        //1. 创建一个服务器端的socket，即 serverSocket，指定绑定的端口，并监听此端口
        ServerSocket serverSocket = new ServerSocket(8888);
        //2. 调用accept方法开始监听，等待客户端连接
        System.out.println("****服务器即将启动，等待客户端的连接***");
        Socket socket = serverSocket.accept();
        //3. 获取输入流用来读取客户端信息,将字节流转换为字符流，为输入流添加缓冲
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String info = null;
        while ((info = br.readLine())!=null){
            System.out.println("我是服务器，客户端说: "+info);
        }
        socket.shutdownInput();
        //4. 获取输入流,响应客户端的请求
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.write("欢迎您");
        pw.flush();

        // 关闭资源
        pw.close();
        br.close();
        serverSocket.close();


    }
}
