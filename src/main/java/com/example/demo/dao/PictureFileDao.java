package com.example.demo.dao;

import com.example.demo.entity.PictureFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureFileDao {
    int deleteByPrimaryKey(Integer id);

    int deleteByFunctionNameAndHashValue(@Param("longName") String longName, @Param("fullName") String fullName, @Param("hashValue") String hashValue);

    int insert(PictureFileEntity record);

    PictureFileEntity selectByPrimaryKey(Integer id);

    List<PictureFileEntity> selectAll();

    int updateByPrimaryKey(PictureFileEntity record);

    List<PictureFileEntity> selectByNameAndProjectIdAndVersion(@Param("name") String name, @Param("fileType") Integer type, @Param("projectId") Integer projectId, @Param("version") String version);

    List<PictureFileEntity> selectByProjectIdAndVersion(@Param("projectId") Integer projectId, @Param("version") String version);

    List<PictureFileEntity> findPictureFileByProcedureFullName (@Param("functionName") String functionName, @Param("projectId") Integer projectId, @Param("version") String version);

    List<PictureFileEntity> findPictureFileByFuncNameAndType (@Param("functionName") String functionName, @Param("fileType") Integer fileType, @Param("hashValue") String hashValue);

    List<PictureFileEntity> findPictureFileByFuncName (@Param("functionName") String functionName, @Param("hashValue") String hashValue);

    int updatePictureFileByFuncNameAndType(PictureFileEntity record);
}