package org.nocturnum.batch.mapper.db1;

import org.apache.ibatis.annotations.Mapper;
import org.nocturnum.batch.common.utils.ParameterMap;

import java.util.List;

@Mapper
public interface Db1Mapper {

    List<ParameterMap> selectDeleteTargetMember(ParameterMap parameterMap) throws Exception;

    int countMember() throws Exception;

    int insertMember(ParameterMap parameterMap) throws Exception;

    int deleteMember(ParameterMap parameterMap) throws Exception;

}
