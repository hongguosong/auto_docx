package com.example.demo.dao;

import com.example.demo.entity.FunDescEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FunDescDao {
    int deleteByPrimaryKey(Integer id);

    int insert(FunDescEntity record);

    FunDescEntity selectByPrimaryKey(Integer id);

    List<FunDescEntity> selectAll();

    int updateByPrimaryKey(FunDescEntity record);

    List<FunDescEntity> selectByProcedureId(@Param("procedureId") Integer procedureId);

    List<FunDescEntity> selecByName(@Param("name") String name, @Param("project") Integer project, @Param("version") String version);

    List<FunDescEntity> selectByProjectIdAndVersion(@Param("project") Integer project, @Param("version") String version);

    List<FunDescEntity> selectByFullName(@Param("fullName") String fullName);
}