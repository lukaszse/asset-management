package pl.com.seremak.assetsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class AssetsManagementApplication {

	public static void main(final String[] args) {
		SpringApplication.run(AssetsManagementApplication.class, args);
	}

}
