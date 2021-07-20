package com.ren.BIO_ChatRoom.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ren
 */

public class ChatServer {

    private int DEFAULT_PORT = 8888;
    private final String QUIT = "quit";
    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private Map<Integer, Writer> connectedClients;

    public ChatServer() {
        executorService = Executors.newFixedThreadPool(10);
        connectedClients = new HashMap<>();
    }

    /**
     * 用户上线
     *
     * @param socket
     * @throws IOException
     */
    public synchronized void addClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            connectedClients.put(port, write);
            System.out.println("客户端【" + port + "】已连接到服务器");
        }
    }

    /**
     * 用户下线
     *
     * @param socket
     * @throws IOException
     */
    public synchronized void removeClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            if (connectedClients.containsKey(port)) {
                connectedClients.get(port).close();
            }
            connectedClients.remove(port);
            System.out.println("客户端【" + port + "】已断开连接");
        }
    }

    /**
     * 向非发送端的客户端广播消息
     *
     * @param socket
     * @param fwdMsg
     * @throws IOException
     */
    public synchronized void forwardMessage(Socket socket, String fwdMsg) throws IOException {
        for (Integer id : connectedClients.keySet()) {
            if (!id.equals(socket.getPort())) {
                Writer writer = connectedClients.get(id);
                writer.write(fwdMsg);
                writer.flush();
            }
        }
    }

    /**
     * 关闭连接
     */
    public synchronized void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                System.out.println("关闭ServerSocket");
            } catch (IOException e) {
                e.printStackTrace();
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

    /**
     * 主流程
     */
    public void start() {
        try {
            // 绑定监听端口
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器，监听端口: " + DEFAULT_PORT + "...");
            while (true) {
                // 等待客户端连接
                Socket socket = serverSocket.accept();
                // 创建chathandler线程
                executorService.execute(new ChatHandler(this, socket));
                /*new Thread(new ChatHandler(this, socket)).start();*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }
}
