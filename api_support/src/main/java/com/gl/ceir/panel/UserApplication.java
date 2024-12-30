package com.gl.ceir.panel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.util.unit.DataSize;

import com.gl.ceir.panel.repository.app.UserRepository;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import jakarta.servlet.MultipartConfigElement;
import net.jodah.expiringmap.ExpiringMap;

@EnableEncryptableProperties
@SpringBootApplication
@EnableFeignClients
@ComponentScan({ "com.gl.ceir.common", "com.gl.ceir.panel" })
@EnableWebSecurity
@EnableScheduling
@EnableAsync
@ImportAutoConfiguration({ FeignAutoConfiguration.class })
public class UserApplication implements CommandLineRunner {
	@Value("${eirs.otp.expirty.time:15}")
	private int otpExpiryTime;
	@Value("${eirs.redmine.backend.api.key}")
	private String redmineKey;
	@Value("${eirs.app.jwt.expiration.ms}")
	private int jwtExpirationMs;
	@Autowired
	private UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	public PassiveExpiringMap<String, String> otpMap() {
		PassiveExpiringMap<String, String> otpMap = new PassiveExpiringMap<>(otpExpiryTime, TimeUnit.SECONDS);
		return otpMap;
	}

	@Bean
	public Map<String, Integer> sessionMap() {
		return new HashMap<String, Integer>();
	}

	@Bean
	public HttpHeaders toRedmineBackendHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Redmine-API-Key", redmineKey);
		return headers;
	}

	@Override
	public void run(String... args) throws Exception {

	}

	@Bean
	public PhysicalNamingStrategy physical() {
		return new CamelCaseToUnderscoresNamingStrategy();
	}

	@Bean
	public ImplicitNamingStrategy implicit() {
		return new ImplicitNamingStrategyLegacyJpaImpl();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.ofBytes(100000000L));
		factory.setMaxRequestSize(DataSize.ofBytes(100000000L));
		return factory.createMultipartConfig();
	}

	@Bean
	public ExpiringMap<String, String> session() {
		ExpiringMap<String, String> map = ExpiringMap.builder().variableExpiration()
				.expirationListener((key, username) -> {
					try {userRepository.decreaseActiveSession(username.toString());}catch(Exception e) {}}).build();
		return map;
	}

}
