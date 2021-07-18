package com.ren;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author Ren
 */

public class NioClient {

    /**
     * 启动客户端
     */
    void start(String nickName) throws IOException {
        /**
         * 连接服务器端
         */
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));
        /**
         * 接收服务器端响应
         * 新开一个线程，专门负责来接收服务端的响应数据
         * selector  socketChannel,注册
         */
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        new Thread(new NioClientHandler(selector)).start();

        /**
         * 向服务器端发送数据
         */
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String request = in.nextLine();
            if (request != null && request.length() > 0) {
                socketChannel.write(Charset.forName("UTF-8").encode(nickName+" : "+request));
            }
        }


    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //new NioClient().start();
    }

}
