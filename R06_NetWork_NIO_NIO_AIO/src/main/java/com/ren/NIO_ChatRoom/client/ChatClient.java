package com.ren.NIO_ChatRoom.client;

import com.mooc.Client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Ren
 */

public class ChatClient {

    private static final String DEFAULT_SERVER_HOST = "127.0.0.1";
    private static final int DEFAULT_SERVER_PORT = 8888;
    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;

    private String host;
    private int port;
    private SocketChannel socketChannel;
    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Selector selector;
    private Charset charset = Charset.forName("UTF-8");

    public ChatClient() {
        this(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }

    private void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void start() throws IOException {
        /**
         * 连接服务器端
         */
        socketChannel = SocketChannel.open(new InetSocketAddress(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT));
        /**
         * 接收服务器端响应
         * 新开一个线程，专门负责来接收服务端的响应数据
         * selector  socketChannel,注册
         */
        selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey key : selectionKeys) {
                handles(key);
            }
            selectionKeys.clear();
        }
    }


    private void handles(SelectionKey key) throws IOException {
        // CONNECT事件 - 连接就绪事件
        if (key.isConnectable()) {
            SocketChannel clientSocketChannel = (SocketChannel) key.channel();
            if (clientSocketChannel.isConnectionPending()) {
                clientSocketChannel.finishConnect();
                // 处理用户输入的信息
                new Thread(new UserInputHandler(this)).start();
            }
            clientSocketChannel.register(selector, SelectionKey.OP_READ);
        }
        // READ事件  -  服务器转发消息
        else if (key.isReadable()) {
            SocketChannel clientSocketChannel = (SocketChannel) key.channel();
            String msg = receive(clientSocketChannel);
            if (msg.isEmpty()) {
                // 服务器异常
                close(selector);
            } else {
                System.out.println(msg);
            }
        }
    }

    public void send(String msg) throws IOException {
        if (msg.isEmpty()) {
            return;
        }

        wBuffer.clear();
        wBuffer.put(charset.encode(msg));
        wBuffer.flip();
        while (wBuffer.hasRemaining()) {
            // 将wBuffer内容写入socketChannel
            socketChannel.write(wBuffer);
        }

        // 检查用户是否准备退出
        if (readyToQuit(msg)) {
            close(selector);
        }
    }

    private String receive(SocketChannel client) throws IOException {
        rBuffer.clear();
        while (client.read(rBuffer) > 0) ;
        rBuffer.flip();
        return String.valueOf(charset.decode(rBuffer));
    }

    /*public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("127.0.0.1", 7777);
        client.start();
    }*/


}
