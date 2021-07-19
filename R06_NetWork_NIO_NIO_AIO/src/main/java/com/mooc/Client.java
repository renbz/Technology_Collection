package com.mooc;

import java.io.*;
import java.net.Socket;

/**
 * @author Ren
 */

public class Client {

    public static void main(String[] args) throws IOException {
        final String DEFAULT_SERVER_HOST = "127.0.0.1";
        final int DEFAULT_SERVER_PORT = 8888;
        Socket socket = null;
        // 创建socket
        socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
        // 创建IO流  用于和向服务器写数据 和 读取服务器返回的数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        // 等待用户输入信息
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String input = consoleReader.readLine();
            //发送消息给服务器
            writer.write(input + "\n");
            writer.flush();
            //读取服务器返回的消息
            String msg = reader.readLine();
            System.out.println(msg);

            // 查看用户是否退出
            if("quit".equals(input)) break;
        }

    }

}
