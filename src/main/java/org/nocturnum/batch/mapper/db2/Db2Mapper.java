package org.nocturnum.batch.mapper.db2;

import org.apache.ibatis.annotations.Mapper;
import org.nocturnum.batch.common.utils.ParameterMap;

import java.util.List;

@Mapper
public interface Db2Mapper {

    List<ParameterMap> selectMemberList(ParameterMap parameterMap) throws Exception;

    int countMember() throws Exception;

}
