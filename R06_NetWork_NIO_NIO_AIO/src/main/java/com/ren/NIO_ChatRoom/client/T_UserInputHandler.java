package com.ren.NIO_ChatRoom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class T_UserInputHandler implements Runnable {

    private T_ChatClient chatClient;

    public T_UserInputHandler(T_ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        try {
            // 等待用户输入消息
            BufferedReader consoleReader =
                    new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = consoleReader.readLine();
                System.out.println("input :"+input);
                // 向服务器发送消息
                chatClient.send(input);
                // 检查用户是否准备退出
                if (chatClient.readyToQuit(input)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}