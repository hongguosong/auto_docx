package com.example.demo.dao;

import com.example.demo.entity.TcfFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TcfFileDao {
    int deleteByPrimaryKey(Integer id);

    int deleteByFunctionNameAndHashValue(@Param("longName") String longName, @Param("fullName") String fullName, @Param("hashValue") String hashValue);

    int insert(TcfFileEntity record);

    TcfFileEntity selectByPrimaryKey(Integer id);

    List<TcfFileEntity> selectAll();

    int updateByPrimaryKey(TcfFileEntity record);

    List<TcfFileEntity> selectByNameAndProjectIdAndVersion(@Param("name") String name, @Param("projectId") Integer projectId, @Param("version") String version);

    List<TcfFileEntity> selectByProjectIdAndVersion(@Param("projectId") Integer projectId, @Param("version") String version);

    List<TcfFileEntity> findTcfFileByProcedureFullName (@Param("functionName") String functionName, @Param("projectId") Integer projectId, @Param("version") String version);

    List<TcfFileEntity> findTcfFileByFuncName(@Param("functionName") String functionName, @Param("hashValue") String hashValue);

    int updateTcfFileByFuncName(TcfFileEntity record);
}