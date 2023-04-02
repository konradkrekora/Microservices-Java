package pl.kk.castleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CastleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CastleServiceApplication.class, args);
	}

}
