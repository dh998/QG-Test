package com.dorm.newrepair;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication//启动类
@MapperScan("com.dorm.newrepair.mapper")//扫描Mapper的接口包
public class DormRepairApplication {
    public static void main(String[] args) {
        //启动SpringBoot应用
        SpringApplication.run(DormRepairApplication.class, args);
    }
}
