package com.example.demo.dao;

import com.example.demo.dto.ProjectVersionDto;
import com.example.demo.entity.ProcedureEntity;
import com.example.demo.entity.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectEntity record);

    ProjectEntity selectByPrimaryKey(Integer id);

    List<ProjectEntity> selectAll();

    List<ProjectEntity> selectByGitAddress(String gitAddress);

    int updateByPrimaryKey(ProjectEntity record);

    List<ProjectVersionDto> selectProWidthLastVer();

    List<ProjectEntity> selectProjectsByFunctionId(@Param("startNum")int startNum, @Param("pageSize")int pageSize, @Param("functionId")int functionId);

    int selectProjectsCountByFunctionId(@Param("functionId")int functionId);

    void deleteProject(@Param("id") int id);

    int updateProjectById(ProjectEntity record);
}
