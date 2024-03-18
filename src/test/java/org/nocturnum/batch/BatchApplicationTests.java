package org.nocturnum.batch;

import org.junit.jupiter.api.Test;
import org.nocturnum.batch.common.config.Db1Config;
import org.nocturnum.batch.common.config.Db2Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(properties = "spring.config.location=classpath:/application.yml, classpath:/config/datasource.yml")
@ContextConfiguration(classes = {Db1Config.class, Db2Config.class})
class BatchApplicationTests {

    @Test
    void contextLoads() {
    }

}
