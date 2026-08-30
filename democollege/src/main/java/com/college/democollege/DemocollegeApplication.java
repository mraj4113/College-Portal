package com.college.democollege;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemocollegeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemocollegeApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper(){
		ModelMapper model = new ModelMapper();
		model.getConfiguration()
          .setSkipNullEnabled(true);
		return model;
	}

}
