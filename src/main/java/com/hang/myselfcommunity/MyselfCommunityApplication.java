package com.hang.myselfcommunity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.hang.myselfcommunity.mapper")
public class MyselfCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyselfCommunityApplication.class, args);
    }

}
