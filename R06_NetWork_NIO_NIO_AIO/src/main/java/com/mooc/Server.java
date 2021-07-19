package com.mooc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Ren
 */

public class Server {
    public static void main(String[] args) {

        final int DEFAULT_PORT = 8888;
        ServerSocket serverSocket = null;
        try {
            //绑定端口
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器  监听端口: " + DEFAULT_PORT);

            while (true) {
                // accept阻塞式的，等待客户端连接
                Socket socket = serverSocket.accept();
                System.out.println("客户端【" + socket.getPort() + "】已连接");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                //读取客户端发送的消息
                String msg = null;
                while ((msg = reader.readLine()) != null) {
                    //msg = reader.readLine();
                    System.out.println("客户端【" + socket.getPort() + "】: " + msg);

                    // 回复客户发送的消息
                    write.write("服务器：" + msg + "\n");
                    write.flush();

                    //查看客户端是否退出
                    if("quit".equals(msg)){
                        System.out.println("客户端【"+socket.getPort()+"]已断开连接");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
