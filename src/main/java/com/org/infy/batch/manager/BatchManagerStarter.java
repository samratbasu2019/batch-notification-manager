package com.org.infy.batch.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.org.infy.batch.manager.property.FileStorageProperties;


@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class BatchManagerStarter {
	public static void main(String[] args) {
		SpringApplication.run(BatchManagerStarter.class, args);
	}
}
