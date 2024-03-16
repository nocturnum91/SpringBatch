package org.nocturnum.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EnableBatchProcessing
@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(BatchApplication.class)
                .properties("spring.config.additional-location=classpath:/config/datasource.yml");
        System.exit(SpringApplication.exit(builder.run(args)));
    }

}
