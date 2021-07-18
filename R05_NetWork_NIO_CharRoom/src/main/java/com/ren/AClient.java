package com.ren;

import java.io.IOException;

/**
 * @author Ren
 */

public class AClient {

    public static void main(String[] args) throws IOException {
        new NioClient().start("AClient");
    }

}
