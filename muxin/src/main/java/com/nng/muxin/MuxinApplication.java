package com.nng.muxin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MuxinApplication  {
	@Bean
	public SpringUtil getSpingUtil() {
		return new SpringUtil();
	}


	public static void main(String[] args) {
		SpringApplication.run(MuxinApplication.class, args);
	}

//	//外置tomcat启动
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(MuxinApplication.class);
//	}

}

