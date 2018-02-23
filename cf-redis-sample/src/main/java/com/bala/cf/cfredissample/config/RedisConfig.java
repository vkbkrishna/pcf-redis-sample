/**
 * 
 */
package com.bala.cf.cfredissample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.bala.cf.cfredissample.domain.Employee;
import com.bala.cf.cfredissample.repository.EmployeeRepository;

/**
 * @author bala
 *
 */
@Configuration
public class RedisConfig {

	@Bean
	public EmployeeRepository repository(RedisTemplate<String, Employee> redisTemplate) {
		return new EmployeeRepository(redisTemplate);
	}

	@Bean
	public RedisTemplate<String, Employee> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Employee> template = new RedisTemplate<>();

		template.setConnectionFactory(redisConnectionFactory);

		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<Employee> employeeSerializer = new Jackson2JsonRedisSerializer<>(Employee.class);

		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(employeeSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(employeeSerializer);

		return template;
	}
}
