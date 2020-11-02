package com.example.demo.dao;

import com.example.demo.entity.WordMubanEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WordMubanDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WordMubanEntity record);

    WordMubanEntity selectByPrimaryKey(Integer id);

    List<WordMubanEntity> selectAll();

    int updateByPrimaryKey(WordMubanEntity record);

    List<WordMubanEntity> selectByProjectIdAndVersion(@Param("projectId") Integer projectId, @Param("version") String version, @Param("fileType") Integer fileType);

    List<WordMubanEntity> selectByIdAndVersion(@Param("projectId") Integer projectId, @Param("version") String version);
}