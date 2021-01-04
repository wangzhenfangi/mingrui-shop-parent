package com.baidu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 2 *@ClassName RunXXXApplication
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2020/12/22
 *
 * @Version V1.0
 * 7
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.baidu.shop.mapper")
public class RunXXXApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunXXXApplication.class);
    }
}