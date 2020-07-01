package com.neusoft;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement//开启事务管理
@MapperScan("com.neusoft.bookstore.*.mapper")
public class XzsdApplication {

    public static void main(String[] args) {
        SpringApplication.run(XzsdApplication.class, args);
        System.out.println("启动完成");
    }

}
