package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.demo.mapper") // 扫描 Mapper 接口所在的包
public class OaSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OaSystemApplication.class, args);
	}
}
