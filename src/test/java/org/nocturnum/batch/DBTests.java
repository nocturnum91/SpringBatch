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
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.springframework.test.util.AssertionErrors.fail;

@Slf4j
@SpringBootTest(properties = "spring.config.location=classpath:/application.yml, classpath:/config/datasource.yml")
@ActiveProfiles("local")
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
    public void testDataSourceConnection() {
        try {
            Connection con1 = db1DataSource.getConnection();
            Connection con2 = db2DataSource.getConnection();
            log.info("" + con1);
            log.info("" + con2);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testMyBatis() {
        try  {
            SqlSession session1 = db1SqlSessionFactory.openSession();
            SqlSession session2 = db2SqlSessionFactory.openSession();
            log.info("" + session1);
            log.info("" + session2);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
