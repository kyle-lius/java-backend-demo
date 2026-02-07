package com.example.backenddemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.backenddemo.mapper")
public class BackendDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendDemoApplication.class, args);
    }

}
