package com.ren;

import java.io.IOException;

/**
 * @author Ren
 */

public class CClient {

    public static void main(String[] args) throws IOException {
        new NioClient().start("CClient");
    }

}
