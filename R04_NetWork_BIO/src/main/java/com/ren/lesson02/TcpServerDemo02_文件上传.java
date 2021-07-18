package com.ren.lesson02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Ren
 */

// 服务端
public class TcpServerDemo02_文件上传 {

    public static void main(String[] args) {
        FileOutputStream fos = null;
        InputStream is = null;
        Socket socket = null;
        ServerSocket serverSocket = null;
        try {
            //1. 创建服务
            serverSocket = new ServerSocket(9000);
            //2. 监听客户端连接，阻塞式监听，会一直等待客户端的连接
            socket = serverSocket.accept();
            //3. 读取客户端的文件
            is = socket.getInputStream();


            // 4. 文件输出
            /*接收文件用文件的管道留*/
            fos = new FileOutputStream(new File("receive.jpg"));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer,0,len);
            }

            // 5. 通知客户端 接收完毕
            OutputStream os = socket.getOutputStream();
            os.write("我接收文件完毕了，你可以断开了".getBytes());


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
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
