package com.example.demo.dao;

import com.example.demo.entity.ProblemPropertyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface ProblemPropertyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProblemPropertyEntity record);

    ProblemPropertyEntity selectByPrimaryKey(Integer id);

    List<ProblemPropertyEntity> selectAll();

    int updateByPrimaryKey(ProblemPropertyEntity record);

    List<ProblemPropertyEntity> selectByPage(@Param("start") Integer start, @Param("offset") Integer offset);

    int maxId ();
}