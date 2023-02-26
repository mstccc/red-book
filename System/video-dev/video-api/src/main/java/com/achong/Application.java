package com.achong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.achong.mapper"})
@ComponentScan(basePackages = {"com.achong", "org.n3r.idworker"})
@EnableMongoRepositories    //开启mongoDB
@RefreshScope   //配置刷新
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
