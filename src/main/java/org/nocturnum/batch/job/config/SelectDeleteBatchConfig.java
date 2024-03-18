package org.nocturnum.batch.job.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.nocturnum.batch.common.utils.ParameterMap;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;


/**
 * 예제 시나리오
 * 1. tb_member 테이블의 last_access_date가 1년보다 오래된 데이터를 조회한다
 * 2. 조회된 데이터를 삭제한다
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class SelectDeleteBatchConfig {

    /**
     * Job Launcher -> Job -> Step -> Tasklet or Chunk(ItemReader / ItemProcessor / ItemWriter)
     * Tasklet: 간단함, 한번에 처리, 대량의 데이터 처리에는 X
     * Tasklet Interface 구현체를 만들거나 MethodInvokingTaskletAdapter를 사용
     * Chunk: 대량의 데이터를 chunkSize 만큼 씩 처리
     * ItemReader: Item을 읽어오는 역할
     * ItemProcessor: ItemReader가 읽어온 데이터를 가공하는 역할 (생략 가능)
     * ItemWriter: ItemReader가 읽어온 데이터나 ItemProcessor가 가공한 데이터를 저장하는 역할
     * Spring Batch의 Chunk 단위로 데이터 처리를 함 / Transaction도 Chunk단위
     */

    private static final int CHUNK_SIZE = 1000;

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;
    public final SqlSessionFactory db1SqlSessionFactory;

    public SelectDeleteBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                   @Qualifier("db1SqlSessionFactory") SqlSessionFactory db1SqlSessionFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.db1SqlSessionFactory = db1SqlSessionFactory;
    }

    @Bean
    @Transactional
    public Job selectDeleteBatchJob() throws Exception {
        return jobBuilderFactory.get("selectDeleteBatchJob").start(selectDeleteStep()).incrementer(new RunIdIncrementer()).build();
    }

    @Bean
    @JobScope
    public Step selectDeleteStep() throws Exception {
        log.info("############DELETE STEP");
        return stepBuilderFactory.get("Step")
                .<ParameterMap, ParameterMap>chunk(CHUNK_SIZE).reader(deleteTargetReader())
                .writer(deleteTargetWriter()).build();
    }

    @Bean
    @StepScope
    public MyBatisPagingItemReader<ParameterMap> deleteTargetReader() throws Exception {
        log.debug("############DELETE TARGET READER");
        //writer에서 삭제를 수행하는 경우 chunkSize만큼 한번씩 건너뛰게 되므로 page 값을 0으로 고정한다.
        MyBatisPagingItemReader<ParameterMap> itemReader = new MyBatisPagingItemReader<ParameterMap>() {
            @Override
            public int getPage() {
                return 0;
            }
        };

        itemReader.setSqlSessionFactory(db1SqlSessionFactory);
        // Mapper안에서도 Paging 처리 시 OrderBy는 필수!
        itemReader.setQueryId("org.nocturnum.batch.mapper.db1.Db1Mapper.selectDeleteTargetMember");
        itemReader.setPageSize(CHUNK_SIZE);

        return itemReader;
    }

    @Bean
    @StepScope
    public MyBatisBatchItemWriter<ParameterMap> deleteTargetWriter() {
        log.info("############DELETE");
        return new MyBatisBatchItemWriterBuilder<ParameterMap>()
                .sqlSessionFactory(db1SqlSessionFactory)
                .statementId("org.nocturnum.batch.mapper.db1.Db1Mapper.deleteMember")
                .build();
    }

}
