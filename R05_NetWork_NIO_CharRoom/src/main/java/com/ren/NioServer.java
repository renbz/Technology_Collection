package com.ren;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ren
 * NIO服务端
 */

public class NioServer {

    public void start() throws IOException {
        /**
         * 1. 通过Selector.open() 创建selector
         */
        Selector selector = Selector.open();
        /**
         * 2. 通过ServerSocketChannel.open 创建channel通道
         */
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        /**
         * 3. 为通道绑定监听端口
         */
        serverSocketChannel.socket().bind(new InetSocketAddress(8000));
        /**
         * 4. 设置channel为非阻塞模式
         */
        serverSocketChannel.configureBlocking(false);
        /**
         * 5. 将channel注册到selector上，监听连接事件
         */
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功!");
        /**
         * 6. 循环等待新接入的连接
         */
        while (true) {
            /**
             * 只有当注册到select上的channel监听的事件就绪了才会返回
             * 获取可用channel数量
             */
            int reayChannels = selector.select();
            if (reayChannels == 0) continue;

            /**
             * 获取可用channel的集合
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                /**
                 * 7. 根据就绪状态，调用对应方法处理业务逻辑
                 */
                // 如果是接入事件
                if (selectionKey.isAcceptable()) acceptHandler(serverSocketChannel, selector);
                // 如果是可读事件
                if (selectionKey.isReadable()) readHandler(selectionKey, selector);
            }
            selectionKeys.clear();
        }
    }

    /**
     * 接入事件处理器
     */
    private void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        /**
         * 如果要是接入事件，创建socketChannel与客户端端建立连接
         */
        SocketChannel socketChannel = serverSocketChannel.accept();
        /**
         * 将socketChannel设置为非阻塞工作模式
         */
        socketChannel.configureBlocking(false);
        /**
         * 将channel注册到selector上，监听 可读事件
         */
        socketChannel.register(selector, SelectionKey.OP_READ);
        /**
         * 回复客户端可读信息
         */
        socketChannel.write(Charset.forName("UTF-8").encode("你与聊天室其他人都不是朋友关系,请注意隐私安全"));
    }

    /**
     * 可读事件处理器
     */
    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        /**
         * 要从 selectionKey中获取已经就绪的channel
         */
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        /**
         * 创建buffer
         */
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        /**
         * 循环读取客户端请求信息
         */
        String request = "";
        while (socketChannel.read(byteBuffer) > 0) {
            /**
             * 切换为读模式
             */
            byteBuffer.flip();
            /**
             * 读取buffer中内容
             */
            request += Charset.forName("UTF-8").decode(byteBuffer);
        }

        /**
         * 将channel再次注册到 selector 上，监听其他的可读事件
         */
        socketChannel.register(selector, SelectionKey.OP_READ);
        /**
         * 将客户端发送的请求信息广播给其他客户端
         */
        if (request.length() > 0) {
            //广播给其他客户端
            System.out.println("::" + request);
            broadCast(selector, socketChannel, request);
        }
    }

    /**
     * 广播给其他客户端
     */
    private void broadCast(Selector selector, SocketChannel sourceChannel, String request) {
        /**
         * 获取到所有已接入的客户端channel
         */
        Set<SelectionKey> selectionKeySet = selector.keys();
        /**
         * 循环向所有channel广播信息
         */
        selectionKeySet.forEach(selectionKey -> {
            Channel targetChannel = selectionKey.channel();
            // 剔除发消息的客户端
            if (targetChannel instanceof SocketChannel && targetChannel != sourceChannel) {
                try {
                    // 将消息发送到targetChannel客户端
                    ((SocketChannel) targetChannel).write(Charset.forName("UTF-8").encode(request));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.start();
    }

}
