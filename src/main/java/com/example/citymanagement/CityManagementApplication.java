package com.example.citymanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

@MapperScan("com.example.citymanagement.Dao")
public class CityManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityManagementApplication.class, args);
    }

}
