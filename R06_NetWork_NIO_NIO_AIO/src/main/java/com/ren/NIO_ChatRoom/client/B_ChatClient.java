package com.ren.NIO_ChatRoom.client;

import java.io.IOException;

/**
 * @author Ren
 */

public class B_ChatClient {

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("127.0.0.1", 7777);
        client.start();
    }


}
