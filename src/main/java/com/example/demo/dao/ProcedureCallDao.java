package com.example.demo.dao;

import com.example.demo.dto.ProcedureCallDto;
import com.example.demo.entity.ProcedureCallEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcedureCallDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProcedureCallEntity record);

    ProcedureCallEntity selectByPrimaryKey(Integer id);

    List<ProcedureCallEntity> selectAll();

    int updateByPrimaryKey(ProcedureCallEntity record);

    List<ProcedureCallDto> findProcedureCallChildByProcedureId(@Param("procedureId") Integer procedureId);
    List<ProcedureCallDto> findProVerProcedureCallChildByProcedureId(@Param("procedureId") Integer procedureId, @Param("project") Integer project, @Param("version") String version);
    List<ProcedureCallDto> findProcedureCallParentByProcedureId(@Param("procedureId") Integer procedureId);
    List<ProcedureCallDto> findProVerProcedureCallParentByProcedureId(@Param("procedureId") Integer procedureId, @Param("project") Integer project, @Param("version") String version);

    List<ProcedureCallEntity> selectByProcedureId(@Param("procedureId") Integer procedureId);

    List<ProcedureCallEntity> selectByProcedureIdAndCallId(@Param("procedureId") Integer procedureId, @Param("calledId") Integer calledId);
}