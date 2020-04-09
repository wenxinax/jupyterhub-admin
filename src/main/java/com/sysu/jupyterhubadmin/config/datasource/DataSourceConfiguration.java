package com.sysu.jupyterhubadmin.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@Configuration
//@MapperScan(basePackages = DataSourceConfiguration.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
@MapperScan("com.sysu.jupyterhubadmin.dao")
public class DataSourceConfiguration {

    @Value("${jdbc.driver}")
    private String driverClassName;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);

        dataSource.setInitialSize(5);
        dataSource.setMaxActive(30);
        dataSource.setMinIdle(5);
        dataSource.setMaxWait(60000);
        return dataSource;
    }

    @Bean(name="sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("/mapper/*.xml"));
        return sqlSessionFactoryBean;
    }

//    @Value("${jdbc.driver}")
//    private String jdbcDriver;
//    @Value("${jdbc.url}")
//    private String jdbcUrl;
//    @Value("${jdbc.username}")
//    private String jdbcUsername;
//    @Value("${jdbc.password}")
//    private String jdbcPassword;
//
//    @Bean(name = "dataSource")
//    public ComboPooledDataSource createDataSource() throws PropertyVetoException {
//        ComboPooledDataSource dataSource = new ComboPooledDataSource();
//        dataSource.setDriverClass(jdbcDriver);
//        dataSource.setJdbcUrl(jdbcUrl);
//        dataSource.setUser(jdbcUsername);
//        dataSource.setPassword(jdbcPassword);
//        dataSource.setMaxStatements(0);
////        dataSource.setMaxStatementsPerConnection(5);
////        dataSource.setMaxIdleTime(1800);
//
//        dataSource.setInitialPoolSize(5);
//        dataSource.setMinPoolSize(3);
//        dataSource.setMaxPoolSize(10);
//        dataSource.setMaxIdleTime(600);
//        dataSource.setAcquireIncrement(2);
//        dataSource.setIdleConnectionTestPeriod(120);
//        dataSource.setMaxConnectionAge(400);
//        //关闭连接后不自动commit
//        dataSource.setAutoCommitOnClose(false);
//        return dataSource;
//    }

}
