package com.example.demo.dao;

import com.example.demo.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface LogDao {
//    List<LogEntity> getLogList(@Param("startNum")int startNum,
//                               @Param("pageSize")int pageSize,
//                               @Param("log")String log,
//                               @Param("createPerson")String createPerson,
//                               @Param("createTime")String createTime);

    List<LogEntity> getLogList(@Param("startNum")int startNum,
                               @Param("pageSize")int pageSize,
                               @Param("preTime")Timestamp preTime,
                               @Param("nextTime")Timestamp nextTime,
                               @Param("createPerson")String createPerson);

//    int getLogListCount(@Param("log")String log,
//                        @Param("createPerson")String createPerson,
//                        @Param("createTime")String createTime);

    int getLogListCount(@Param("preTime")Timestamp preTime,
                        @Param("nextTime")Timestamp nextTime,
                        @Param("createPerson")String createPerson);

    void insertLog(@Param("log")String log,
                   @Param("createPerson")String createPerson,
                   @Param("createTime") Timestamp createTime);

    List<LogEntity> getCreatePersonList();
}
