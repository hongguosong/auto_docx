package com.example.demo.dao;

import com.example.demo.entity.ProjectVersionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProjectVersionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectVersionEntity record);

    ProjectVersionEntity selectByPrimaryKey(Integer id);

    List<ProjectVersionEntity> selectAll();

    int updateByPrimaryKey(ProjectVersionEntity record);

    List<ProjectVersionEntity> selectByProjectId(Integer projectId);

    List<ProjectVersionEntity> selectVByFuncIdAndProId(int functionId, int projectId);

    Date selectMaxDate(@Param("projectId") Integer projectId);

    List<ProjectVersionEntity> selectByVersionAndId(@Param("projectId") Integer projectId, @Param("projectVersion") String version);
}
