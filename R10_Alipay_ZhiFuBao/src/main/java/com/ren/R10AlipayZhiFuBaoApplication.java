package com.ren;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ren.mapper")
@EnableEncryptableProperties //开启加密注解
public class R10AlipayZhiFuBaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(R10AlipayZhiFuBaoApplication.class, args);
    }
}
