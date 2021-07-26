package com.ren;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Ren
 * 服务器线程处理类
 */

public class R04_TCP_Thread_Server extends Thread {

    // 和本线程相关的socket
    Socket socket = null;

    public R04_TCP_Thread_Server(Socket socket) {
        this.socket = socket;
    }

    // 线程执行的操作，响应客户端的请求

    @Override
    public void run() {
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            //3. 获取输入流用来读取客户端信息,将字节流转换为字符流，为输入流添加缓冲
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String info = null;
            while ((info = br.readLine()) != null) {
                System.out.println("我是服务器，客户端说: " + info);
            }
            socket.shutdownInput();
            //4. 获取输入流,响应客户端的请求
            pw = new PrintWriter(socket.getOutputStream());
            pw.write("欢迎您");
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            pw.close();
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
