package com.atc.auto;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @ClassName AutoApplication
 * @Description 项目启动类
 * @Author monco
 * @Date 2019/3/5 1:48
 * @Version 1.0
 **/
@Slf4j
@SpringBootApplication
@EnableJpaRepositories
@MapperScan("com.atc.auto.core.mapper")
public class AutoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoApplication.class, args);
    }
}
