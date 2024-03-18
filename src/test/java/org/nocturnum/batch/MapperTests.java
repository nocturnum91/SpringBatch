package org.nocturnum.batch;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.nocturnum.batch.common.config.Db1Config;
import org.nocturnum.batch.common.config.Db2Config;
import org.nocturnum.batch.common.utils.ParameterMap;
import org.nocturnum.batch.mapper.db1.Db1Mapper;
import org.nocturnum.batch.mapper.db2.Db2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@MybatisTest(properties = "spring.config.location=classpath:/application.yml, classpath:/config/datasource.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {Db1Config.class, Db2Config.class})
//@Rollback(value = false)
@Slf4j
public class MapperTests {

    @Setter(onMethod_ = {@Autowired})
    private Db1Mapper db1Mapper;

    @Setter(onMethod_ = {@Autowired})
    private Db2Mapper db2Mapper;

    @Test
    public void selectDeleteTargetMemberTest() throws Exception {
        ParameterMap parameterMap = new ParameterMap();
        parameterMap.put("_skiprows", 0);
        parameterMap.put("_pagesize", 10);
        List<ParameterMap> result = db1Mapper.selectDeleteTargetMember(parameterMap);
        result.forEach(resultMap -> {
            log.info("resultMap: {}", resultMap);
        });
    }

    @Test
    public void insertMemberTest() throws Exception {
        ParameterMap parameterMap = new ParameterMap();
        parameterMap.put("id", "aaa");
        parameterMap.put("name", "aaa");
        parameterMap.put("email", "aaa@mail.com");
        parameterMap.put("join_date", "2023-02-05 17:05:00");
        parameterMap.put("last_access_date", "2023-02-07 17:05:00");
        db1Mapper.insertMember(parameterMap);
    }

    @Test
    public void deleteMemberTest() throws Exception {
        ParameterMap parameterMap = new ParameterMap();
        parameterMap.put("id", "aaa");
        db1Mapper.deleteMember(parameterMap);
    }

    @Test
    public void selectMemberListTest() throws Exception {
        ParameterMap parameterMap = new ParameterMap();
        parameterMap.put("_skiprows", 0);
        parameterMap.put("_pagesize", 10);
        List<ParameterMap> result = db2Mapper.selectMemberList(parameterMap);
        result.forEach(resultMap -> {
            log.info("resultMap: {}", resultMap);
        });
    }

}
