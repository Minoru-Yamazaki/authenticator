package br.com.authenticator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AuthenticationAPI {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationAPI.class, args);
	}

}
