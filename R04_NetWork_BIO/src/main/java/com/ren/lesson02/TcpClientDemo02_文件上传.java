package com.ren.lesson02;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Ren
 */

// 客户端
public class TcpClientDemo02_文件上传 {

    public static void main(String[] args) {

        Socket socket = null;
        OutputStream os = null;
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        InputStream inputStream = null;
        try {
            // 1. 创建一个socket连接
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9000);
            // 2. 创建一个输出流
            os = socket.getOutputStream();
            // 3. 读取文件
            fis = new FileInputStream(new File("E:\\Java_Frame\\kuang_study_elasticsearch\\R03_Network_programming\\src\\main\\resources\\ren.jpg"));
            // 4. 写文件
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            //通知服务器  我已经发送完毕了
            socket.shutdownOutput();  // 我已经传输完了


            //确认服务器接收完毕，才能够断开
            inputStream = socket.getInputStream();
            /*接收字符串  用字符串的管道流*/
            baos = new ByteArrayOutputStream();
            byte[] buffer2 = new byte[1024];
            int len2;
            while ((len2 = (inputStream.read(buffer2))) != -1) {
                baos.write(buffer2, 0, len2);
            }
            System.out.println(baos.toString());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 关闭资源
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
