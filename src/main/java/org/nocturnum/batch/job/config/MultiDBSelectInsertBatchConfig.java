package org.nocturnum.batch.job.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.nocturnum.batch.common.utils.ParameterMap;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 예제 시나리오
 * 1. db2의 tb_member 테이블의 데이터를 조회한다
 * 2. 조회된 데이터를 db1의 tb_member 테이블에 저장한다
 * 값을 변경해야 하는 경우 주석 처리 되어있는 processor 참고하여 구현
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class MultiDBSelectInsertBatchConfig {

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
    public final SqlSessionFactory db2SqlSessionFactory;

    public MultiDBSelectInsertBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                          @Qualifier("db1SqlSessionFactory") SqlSessionFactory db1SqlSessionFactory,
                                          @Qualifier("db2SqlSessionFactory") SqlSessionFactory db2SqlSessionFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.db1SqlSessionFactory = db1SqlSessionFactory;
        this.db2SqlSessionFactory = db2SqlSessionFactory;
    }

    @Bean
    @Transactional
    public Job selectInsertBatchJob() throws Exception {
        return jobBuilderFactory.get("selectInsertBatchJob").start(selectInsertStep()).incrementer(new RunIdIncrementer()).build();
    }

    @Bean
    @JobScope
    public Step selectInsertStep() throws Exception {
        log.info("############STEP");
        return stepBuilderFactory.get("Step")
                .<ParameterMap, ParameterMap>chunk(CHUNK_SIZE).reader(reader())
//				.processor(processor(null))
                .writer(writer()).build();
    }

    /**
     * Item을 읽어오는 역할
     */
    @Bean
    @StepScope
    public MyBatisPagingItemReader<ParameterMap> reader() throws Exception {
        log.info("############READER");

        return new MyBatisPagingItemReaderBuilder<ParameterMap>().
                sqlSessionFactory(db2SqlSessionFactory)
                // Mapper안에서도 Paging 처리 시 OrderBy는 필수!
                .queryId("org.nocturnum.batch.mapper.db2.Db2Mapper.selectMemberList")
//                .parameterValues(parameterMap)
                .pageSize(CHUNK_SIZE).build();
    }

    /**
     * Item을 처리하는 역할
     */
//    @Bean
//    @StepScope
//    public ItemProcessor<ParameterMap, ParameterMap> processor() {
//
//        return new ItemProcessor<ParameterMap, ParameterMap>() {
//            @Override
//            public ParameterMap process(ParameterMap parameterMap) throws Exception {
//                // 1000원 추가 적립
//                parameterMap.put("count", parameterMap.getInt("count") + 1000);
//                log.info("#################" + parameterMap);
//                return parameterMap;
//            }
//        };
//    }

    /**
     * 처리된 데이터를 저장하는 역할
     */
    @Bean
    @StepScope
    public MyBatisBatchItemWriter<ParameterMap> writer() {
        log.info("############WRITER");

        return new MyBatisBatchItemWriterBuilder<ParameterMap>()
                .sqlSessionFactory(db1SqlSessionFactory)
                .statementId("org.nocturnum.batch.mapper.db1.Db1Mapper.insertMember")
                .build();
    }


}
