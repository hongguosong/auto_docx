package com.example.demo.dao;

import com.example.demo.entity.CoverageFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoverageFileDao {
    int deleteByPrimaryKey(Integer id);

    int deleteByFunctionNameAndHashValue(@Param("longName") String longName, @Param("fullName") String fullName, @Param("hashValue") String hashValue);

    int insert(CoverageFileEntity record);

    CoverageFileEntity selectByPrimaryKey(Integer id);

    List<CoverageFileEntity> selectAll();

    int updateByPrimaryKey(CoverageFileEntity record);

    List<CoverageFileEntity> selectByNameAndProjectIdAndVersion(@Param("name") String name, @Param("projectId") Integer projectId, @Param("version") String version);

    List<CoverageFileEntity> selectByProjectIdAndVersion(@Param("projectId") Integer projectId, @Param("version") String version);

    List<CoverageFileEntity> findCoverageFileByProcedureFullName (@Param("functionName") String functionName, @Param("projectId") Integer projectId, @Param("version") String version);

    List<CoverageFileEntity> findCoverageFileByFuncName (@Param("functionName") String functionName, @Param("hashValue") String hashValue);

    int updateCoverageFileByFuncName(CoverageFileEntity record);
}