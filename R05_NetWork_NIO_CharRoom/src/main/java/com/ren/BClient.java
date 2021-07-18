package com.ren;

import java.io.IOException;

/**
 * @author Ren
 */

public class BClient {
    public static void main(String[] args) throws IOException {
        new NioClient().start("BClient");
    }

}
