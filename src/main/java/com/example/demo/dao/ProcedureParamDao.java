package com.example.demo.dao;

import com.example.demo.entity.ProcedureParamEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcedureParamDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProcedureParamEntity record);

    ProcedureParamEntity selectByPrimaryKey(Integer id);

    List<ProcedureParamEntity> selectAll();

    int updateByPrimaryKey(ProcedureParamEntity record);

    List<ProcedureParamEntity> findProcedureParamByProcedureId(@Param("procedureId") Integer procedureId);
}