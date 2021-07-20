package com.ren.NIO_ChatRoom.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * @author Ren
 */

public class ChatServer {

    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER);
    private Charset charset = Charset.forName("UTF-8");
    private int port;

    public ChatServer() {
    }

    public ChatServer(int port) {
        this.port = port;
    }


    void start() throws IOException {
        try {

            /**
             * 1. 创建selector
             */
            selector = Selector.open();
            /**
             * 2. 通过ServerSocketChannel创建channel通道
             */
            serverSocketChannel = ServerSocketChannel.open();
            /**
             * 3. 为通道绑定监听端口
             */
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            /**
             * 4. 设置channel为非阻塞模式
             */
            serverSocketChannel.configureBlocking(false);
            /**
             * 5. 将channel注册到selector上，监听连接事件
             */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动成功!  监听端口" + port + "...");
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

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    // 处理被触发的事件
                    handles(key);
                }
                selectionKeys.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (selector != null) {
                selector.close();
            }
        }

    }

    private void handles(SelectionKey key) throws IOException {
        // ACCEPT事件   -  和客户端建立了连接
        if (key.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            /**
             * 如果要是接入事件，创建socketChannel与客户端端建立连接
             */
            SocketChannel clientSocketChannel = serverSocketChannel.accept();
            /**
             * 将socketChannel设置为非阻塞工作模式
             */
            clientSocketChannel.configureBlocking(false);
            /**
             * 将channel注册到selector上，监听 可读事件
             */
            clientSocketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("客户端 [" + clientSocketChannel.socket().getPort() + "] 已连接");
            /**
             * 回复客户端可读信息
             */
            clientSocketChannel.write(Charset.forName("UTF-8").encode("你与聊天室其他人都不是朋友关系,请注意隐私安全"));
        }

        // READ事件  -  客户端发送了消息
        else if (key.isReadable()) {

            SocketChannel clientSocketChannel = (SocketChannel) key.channel();
            /**
             * 创建buffer
             */
            readBuffer = ByteBuffer.allocate(1024);
            /**
             * 循环读取客户端请求信息
             */
            String fwdMsg = "";
            while (clientSocketChannel.read(readBuffer) > 0) {
                /**
                 * 切换为读模式
                 */
                readBuffer.flip();
                /**
                 * 读取buffer中内容
                 */
                fwdMsg += charset.decode(readBuffer);
            }


            /**
             * 将channel再次注册到selector上，监听其他的可读事件
             */
            clientSocketChannel.register(selector, SelectionKey.OP_READ);



            /**
             * 将客户端发送的请求信息广播给其他客户端
             */
            if (fwdMsg.length() > 0) {
                //广播给其他客户端
                System.out.println("::" + fwdMsg);
                broadCast(clientSocketChannel, fwdMsg);

                //检查用户是否退出
                if (readyToQuit(fwdMsg)) {
                    key.cancel();  // 取消监听
                    selector.wakeup();
                    System.out.println("客户端 [" + clientSocketChannel.socket().getPort() + "] 已断开");
                }
            } else {
                //客户端异常
                key.cancel();
                selector.wakeup();
            }
        }

    }

    /**
     * 广播给其他客户端
     */
    private void broadCast(SocketChannel clientSocketChannel, String fwdMsg) throws IOException {
        /**
         * 获取到所有已接入的客户端channel
         */
        Set<SelectionKey> selectionKeySet = selector.keys();
        /**
         * 循环向所有channel广播信息
         */
        for (SelectionKey key : selectionKeySet) {
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && key.isValid() && targetChannel != clientSocketChannel) {
                writeBuffer.clear();
                writeBuffer.put(charset.encode("客户端 [" + clientSocketChannel.socket().getPort() + "] " + fwdMsg));
                writeBuffer.flip();
                while (writeBuffer.hasRemaining()) ((SocketChannel) targetChannel).write(writeBuffer);
            }
        }
    }

    /**
     * 是否准备端口连接
     *
     * @param msg
     * @return
     */
    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer(7777);
        chatServer.start();
    }


}
