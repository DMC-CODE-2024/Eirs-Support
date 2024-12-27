package com.gl.ceir.panel.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "auditEntityManagerFactory", transactionManagerRef = "auditTransactionManager", basePackages = {
		"com.gl.ceir.panel.repository.audit" })
@EntityScan("com.gl.ceir.panel.entity.audit")
public class AuditDatasourceConfiguration {
	
	@Bean(name = "auditSourceProperties")
	@ConfigurationProperties("audit.datasource")
	public DataSourceProperties auditSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "auditDatasource")
	@ConfigurationProperties(prefix = "audit.datasource")
	public DataSource auditDatasource(@Qualifier("auditSourceProperties") DataSourceProperties auditSourceProperties) {
		return auditSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Bean(name = "auditEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean auditEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("auditDatasource") DataSource auditDatasource) {
		return builder.dataSource(auditDatasource).packages("com.gl.ceir.panel.entity.audit").persistenceUnit("aud")
				.properties(jpaProperties()).build();
	}

	@Bean(name = "auditTransactionManager")
	public PlatformTransactionManager auditTransactionManager(@Qualifier("auditEntityManagerFactory") EntityManagerFactory auditEntityManagerFactory) {
		return new JpaTransactionManager(auditEntityManagerFactory);
	}

	
	protected Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put("hibernate.physical_naming_strategy",
				org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy.class.getName());
		props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
		return props;
	}
}
