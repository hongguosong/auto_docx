package com.example.demo.dao;

import com.example.demo.entity.ReportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ReportEntity record);

    ReportEntity selectByPrimaryKey(Integer id);

    List<ReportEntity> selectAll();

    int updateByPrimaryKey(ReportEntity record);

    List<ReportEntity> findProcedureListByIdAndVersion(@Param("project") Integer project, @Param("version") String version);
}