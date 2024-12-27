package com.gl.ceir.downloader.config;

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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "aapEntityManagerFactory", transactionManagerRef = "appTransactionManager", basePackages = {
		"com.gl.ceir.downloader.repository" })
@EntityScan("com.gl.ceir.downloader.entity")
public class RedmineDatasourceConfiguration {
	@Primary
	@Bean(name = "appSourceProperties")
	@ConfigurationProperties("redmine.datasource")
	public DataSourceProperties appSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "appDatasource")
	@ConfigurationProperties(prefix = "redmine.datasource")
	public DataSource appDatasource(@Qualifier("appSourceProperties") DataSourceProperties appSourceProperties) {
		return appSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Primary
	@Bean(name = "aapEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean aapEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("appDatasource") DataSource appDatasource) {
		return builder.dataSource(appDatasource).packages("com.gl.ceir.downloader.entity").persistenceUnit("redmine")
				.properties(jpaProperties()).build();
	}

	@Primary
	@Bean(name = "appTransactionManager")
	public PlatformTransactionManager appTransactionManager(@Qualifier("aapEntityManagerFactory") EntityManagerFactory aapEntityManagerFactory) {
		return new JpaTransactionManager(aapEntityManagerFactory);
	}

	protected Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put("hibernate.physical_naming_strategy",
				org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy.class.getName());
		props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
		return props;
	}
}
