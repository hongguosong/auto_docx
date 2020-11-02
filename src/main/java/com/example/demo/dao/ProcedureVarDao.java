package com.example.demo.dao;

import com.example.demo.entity.ProcedureVarEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcedureVarDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProcedureVarEntity record);

    ProcedureVarEntity selectByPrimaryKey(Integer id);

    List<ProcedureVarEntity> selectAll();

    int updateByPrimaryKey(ProcedureVarEntity record);

    List<ProcedureVarEntity> findProcedureVarByProcedureId(@Param("procedureId") Integer procedureId);

    List<ProcedureVarEntity> selectByProcedureId(@Param("procedureId") Integer procedureId);
}