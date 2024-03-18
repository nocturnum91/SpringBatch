package org.nocturnum.batch;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.nocturnum.batch.mapper.db1.Db1Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@SpringBootTest(properties = "spring.config.location=classpath:/application.yml, classpath:/config/datasource.yml")
@TestPropertySource(properties = "spring.batch.job.enabled=false")
class DBTests {

    @Value("${server.port}")
    private String port;

    @Test
    public void testPort() {
        log.info("PORT: " + port);
    }

    @Qualifier("db1DataSource")
    @Setter(onMethod_ = {@Autowired})
    private DataSource db1DataSource;

    @Qualifier("db2DataSource")
    @Setter(onMethod_ = {@Autowired})
    private DataSource db2DataSource;

    @Qualifier("db1SqlSessionFactory")
    @Setter(onMethod_ = {@Autowired})
    public SqlSessionFactory db1SqlSessionFactory;

    @Qualifier("db2SqlSessionFactory")
    @Setter(onMethod_ = {@Autowired})
    public SqlSessionFactory db2SqlSessionFactory;

    @Setter(onMethod_ = {@Autowired})
    private Db1Mapper db1Mapper;

    @Setter(onMethod_ = {@Autowired})
    private Db1Mapper db2Mapper;

    @Test
    public void testLog() throws Exception {
        log.info("LOG TEST");
    }

    @Test
    public void dataSourceConnectionTest() {
        try {
            Connection con1 = db1DataSource.getConnection();
            Connection con2 = db2DataSource.getConnection();
            log.info("con1: {}", con1);
            log.info("con2: {}", con2);
        } catch (Exception e) {
            log.error("dataSourceConnectionTest error", e);
        }
    }

    @Test
    public void sqlSessionFactoryTest() {
        try {
            SqlSession session1 = db1SqlSessionFactory.openSession();
            SqlSession session2 = db2SqlSessionFactory.openSession();
            log.info("session1: {}", session1);
            log.info("session2: {}", session2);
        } catch (Exception e) {
            log.error("sqlSessionFactoryTest error", e);
        }
    }

}
