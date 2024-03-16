package org.nocturnum.batch.common.config.db;


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
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "org.nocturnum.batch.mapper.db1", sqlSessionFactoryRef = "db1SqlSessionFactory") /*멀티DB사용시 mapper클래스파일 스켄용 basePackages를 DB별로 따로설정*/
@EnableTransactionManagement
//@PropertySource("file:/C:\\Users\\Nocturnum\\config.properties")
public class Db1Config {

//    @Autowired
//    Environment env;

    @Primary
    @Bean(name = "db1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db1") // appliction.properties 참고.
    public DataSource db1DataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class).build();
    }


    @Primary
    @Bean(name = "db1SqlSessionFactory")
    public SqlSessionFactory db1sqlSessionFactory(@Qualifier("db1DataSource") DataSource db1DataSource,
                                                  ApplicationContext applicationContext) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(db1DataSource);
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/db1/*.xml"));
        sessionFactory.setTypeAliasesPackage("org.nocturnum.batch.common.utils");
        return sessionFactory.getObject();
    }

    @Primary
    @Bean(name = "db1SqlSessionTemplate")
    public SqlSessionTemplate db1sqlSessionTemplate(SqlSessionFactory db1sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(db1sqlSessionFactory);
    }

    @Primary
    @Bean(name = "db1transactionManager")
    public PlatformTransactionManager db1transactionManager(@Qualifier("db1DataSource") DataSource db1DataSource) {
        return new DataSourceTransactionManager(db1DataSource);
    }

}
