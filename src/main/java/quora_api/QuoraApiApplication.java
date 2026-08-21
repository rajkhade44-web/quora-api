package quora_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableAspectJAutoProxy
@Slf4j
public class QuoraApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuoraApiApplication.class, args);
		log.info("Hello World!!!");
	}

}
