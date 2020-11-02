package com.example.demo.dao;

import com.example.demo.entity.ProcedureVarCallEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcedureVarCallDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProcedureVarCallEntity record);

    ProcedureVarCallEntity selectByPrimaryKey(Integer id);

    List<ProcedureVarCallEntity> selectAll();

    int updateByPrimaryKey(ProcedureVarCallEntity record);

    List<ProcedureVarCallEntity> selectByProcedureId(@Param("procedureId") Integer procedureId);

    List<ProcedureVarCallEntity> selectByProcedureIdAndCalledVarId(@Param("procedureId") Integer procedureId, @Param("calledVarId") Integer calledVarId);
}