package ChuyenNganh.Seafood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SeaFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeaFoodApplication.class, args);
	}
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> builder.failOnEmptyBeans(false);
	}
}
