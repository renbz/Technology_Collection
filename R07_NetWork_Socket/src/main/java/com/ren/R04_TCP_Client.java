package com.ren;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Ren
 */

public class R04_TCP_Client {

    public static void main(String[] args) throws IOException {
        //1. 创建客户端socket,指定服务器地址和端口
        Socket socket = new Socket("localhost",8888);
        //2. 获取输出流  用来向服务器端发送登录信息
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.write("用户名: admin; 密码: 123");
        pw.flush();
        //3. 获取输入流 用来读取服务器端的响应信息
        BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //4.
        String info = null;
        while ((info = bf.readLine())!=null){
            System.out.println("我是客户端，服务器说: "+info);
        }
        socket.shutdownInput();
        socket.shutdownOutput();
        bf.close();
        pw.close();
        socket.close();

    }
}
