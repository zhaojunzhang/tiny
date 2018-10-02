package com.tiny;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.tiny.mapper")
@SpringBootApplication
public class TinyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyApplication.class, args);

	}
}
