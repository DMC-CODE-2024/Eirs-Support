package com.gl.ceir.panel.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class DatabaseAppConfigInitializer implements BeanPostProcessor, InitializingBean, EnvironmentAware {

	private final JdbcTemplate jdbcTemplate;
	private ConfigurableEnvironment environment;

	public DatabaseAppConfigInitializer(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterPropertiesSet() {
		if (environment != null) {
			Map<String, Object> systemConfigMap = new HashMap<>();
			String sql = "SELECT tag,value from eirs_response_param";
			List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);

			for (Map<String, Object> map : maps) {
				String key = String.valueOf(map.get("tag"));
				Object value = map.get("value");
				systemConfigMap.put(key, value);
				log.info("loading db eirs_response_param, [key {}=value {}]", key, value);
			}
			String propertySourceName = "propertiesInsideDatabase";
			environment.getPropertySources().addFirst(new MapPropertySource(propertySourceName, systemConfigMap));
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		if (environment instanceof ConfigurableEnvironment) {
			this.environment = (ConfigurableEnvironment) environment;
		}
	}
}
