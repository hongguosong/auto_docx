package com.example.demo.dao;

import com.example.demo.entity.ProblemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.security.PermitAll;
import java.util.List;
@Mapper
@Repository
public interface ProblemDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProblemEntity record);

    ProblemEntity selectByPrimaryKey(Integer id);

    List<ProblemEntity> selectAll();

    int updateByPrimaryKey(ProblemEntity record);

    List<ProblemEntity> getByProcedureId(@Param("procedureId") Integer procedureId);

    List<ProblemEntity> getByProcedureIdAndProjectAndVersion(@Param("procedureId") Integer procedureId, @Param("projectId") Integer projectId, @Param("version") String version);

    List<ProblemEntity> selectByPage(@Param("start") Integer start, @Param("offset") Integer offset);

    List<ProblemEntity> selectByNameAndProjectAndVersion(@Param("name") String name, @Param("project") Integer project, @Param("version") String version);

    List<ProblemEntity> selectByProjectAndVersion(@Param("project") Integer project, @Param("version") String version);
}