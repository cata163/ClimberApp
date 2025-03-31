package com.doc;

import org.springframework.boot.SpringApplication;

public class TestHikerApplication {

	public static void main(String[] args) {
		SpringApplication.from(HikerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
