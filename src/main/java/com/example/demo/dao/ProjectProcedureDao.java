package com.example.demo.dao;

import com.example.demo.entity.ProjectProcedureEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectProcedureDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectProcedureEntity record);

    ProjectProcedureEntity selectByPrimaryKey(Integer id);

    List<ProjectProcedureEntity> selectAll();

    int updateByPrimaryKey(ProjectProcedureEntity record);

    List<ProjectProcedureEntity> selectProjectProcedureList(@Param("projectId") Integer projectId, @Param("projectVersion") String projectVersion);

}