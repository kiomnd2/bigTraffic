package kr.kiomn2.bigtraffic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BigTrafficApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigTrafficApplication.class, args);
	}

}