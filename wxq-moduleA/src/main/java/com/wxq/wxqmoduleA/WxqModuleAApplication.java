package com.wxq.wxqmoduleA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class WxqModuleAApplication {

	public static void main(String[] args) {
		SpringApplication.run(WxqModuleAApplication.class, args);
	}

}
