package org.nocturnum.batch.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "org.nocturnum.batch.mapper.db2", sqlSessionFactoryRef = "db2SqlSessionFactory")/*멀티DB사용시 mapper클래스파일 스켄용 basePackages를 DB별로 따로설정*/
@EnableTransactionManagement
public class Db2Config {

    @Bean(name = "db2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db2") // appliction.properties 참고.
    public DataSource db2DataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "db2SqlSessionFactory")
    public SqlSessionFactory db2sqlSessionFactory(@Qualifier("db2DataSource") DataSource db2DataSource, ApplicationContext applicationContext) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(db2DataSource);
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/db2/*.xml"));
        sessionFactory.setTypeAliasesPackage("org.nocturnum.batch.common.utils");
        return sessionFactory.getObject();
    }

    @Bean(name = "db2SqlSessionTemplate")
    public SqlSessionTemplate db2sqlSessionTemplate(SqlSessionFactory db2sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(db2sqlSessionFactory);
    }

    @Bean(name = "db2transactionManager")
    public PlatformTransactionManager db2transactionManager(@Qualifier("db2DataSource") DataSource db2DataSource) {
        return new DataSourceTransactionManager(db2DataSource);
    }

}
