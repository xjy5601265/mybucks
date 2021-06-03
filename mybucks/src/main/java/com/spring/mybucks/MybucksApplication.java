package com.spring.mybucks;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.spring.mybucks.controller.PerformanceInteceptor;
import com.spring.mybucks.converter.*;
import com.spring.mybucks.model.Coffee;
import com.spring.mybucks.service.CoffeeOrderService;
import com.spring.mybucks.service.CoffeeService;
import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.TimeZone;

@SpringBootApplication
@Slf4j
@EnableTransactionManagement
public class MybucksApplication implements WebMvcConfigurer {
	@Autowired
	private CoffeeService coffeeService;
	@Autowired
	private CoffeeOrderService orderService;
	public static void main(String[] args) {
		SpringApplication.run(MybucksApplication.class, args);
	}

	@Bean
	public MongoCustomConversions mongoCustomConversions(){
		return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
	}

	@Bean
	public RedisTemplate<String, Coffee> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Coffee> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	public RedisCustomConversions redisCustomConversions() {
		return new RedisCustomConversions(
				Arrays.asList(new MoneyToBytesConverter(), new BytesToMoneyConverter()));
	}

	@Bean
	public LettuceClientConfigurationBuilderCustomizer customizer() {
		return builder -> builder.readFrom(ReadFrom.MASTER_PREFERRED);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new PerformanceInteceptor())
				.addPathPatterns("/coffee/**").addPathPatterns("/order/**");
	}

	@Bean
	public Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonBuilderCustomizer() {
		return builder -> {
			builder.indentOutput(true);
			builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		};
	}
}
