package com.bestiansoft.pdfgen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir");
	   System.out.println("Current working directory : " + workingDir);
		SpringApplication.run(DemoApplication.class, args);
	}

}

