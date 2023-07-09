package com.wxq.codemanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.wxq.codemanage.mapper")
@SpringBootApplication
public class CodemanageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodemanageApplication.class, args);
	}

}
