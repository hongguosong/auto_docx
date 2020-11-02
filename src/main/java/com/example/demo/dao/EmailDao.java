package com.example.demo.dao;

import com.example.demo.entity.EmailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface EmailDao {
    int deleteByPrimaryKey(String id);

    int insert(EmailEntity record);

    EmailEntity selectByPrimaryKey(String id);

    List<EmailEntity> selectAll();

    int updateByPrimaryKey(EmailEntity record);
}