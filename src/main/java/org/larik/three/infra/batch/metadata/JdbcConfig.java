package org.larik.three.infra.batch.metadata;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JdbcJobRepositoryFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class JdbcConfig {

    private String username;
    private String password;
    private String jdbcUrl;

    @Bean
    @Primary
    public JobRepository jobRepositoryMeta (DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JdbcJobRepositoryFactoryBean factoryBean = new JdbcJobRepositoryFactoryBean();
        factoryBean.setDatabaseType("POSTGRES");
        factoryBean.setTransactionManager(transactionManager);
        factoryBean.setDataSource(dataSource);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/challenge");
        return new HikariDataSource(config);
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
