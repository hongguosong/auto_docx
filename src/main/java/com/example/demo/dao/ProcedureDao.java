package com.example.demo.dao;

import com.example.demo.dto.ProcedureDto;
import com.example.demo.entity.ProcedureEntity;
import com.example.demo.entity.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface ProcedureDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ProcedureEntity record);

    ProcedureEntity selectByPrimaryKey(Integer id);

    List<ProcedureEntity> selectAll();

    int updateByPrimaryKey(ProcedureEntity record);

    List<ProcedureEntity> findProcedureListByFullName(@Param("fullName") String fullName);
    List<ProcedureEntity> findProcedureListByLongName(@Param("longName") String fullName);

    List<ProcedureEntity> findProcedureListByHashValue(@Param("fullName") String fullName, @Param("hashValue") String hashValue);

    List<ProcedureDto> findProcedureListByPVAndAndFullNameHashValue(@Param("projectId") Integer projectId, @Param("projectVersion") String projectVersion, @Param("fullName") String fullName, @Param("hashValue") String hashValue);

    List<ProcedureDto> findProcedureListByIdAndVersion(@Param("projectId") Integer projectId, @Param("projectVersion") String projectVersion,
                                                       @Param("sortField") String sortField, @Param("sortOrder") String sortOrder,
                                                       @Param("name") String name,
                                                       @Param("longName") String longName, @Param("fullName") String fullName,
                                                       @Param("version") String version, @Param("hashValue") String hashValue,
                                                       @Param("returnType") String returnType,
                                                       @Param("lineCount") String lineCount,
                                                       @Param("comment") String comment,
                                                       @Param("returnComment") String returnComment,
                                                       @Param("parentId") String parentId, @Param("createTime") String createTime,
                                                       @Param("updateTime") String updateTime, @Param("insertFlag") Integer insertFlag,
                                                       @Param("affirm") Integer affirm);

    List<ProcedureDto> findProcedureListByIdAndVersionFull(@Param("projectId") Integer projectId, @Param("projectVersion") String projectVersion,
                                                       @Param("tcf") String tcf,
                                                       @Param("coverage") String coverage,
                                                       @Param("picture") String picture,
                                                       @Param("problem") String problem,
                                                       @Param("insertFlag") Integer insertFlag);

    List<ProcedureEntity> selectProcedureAll(@Param("name") String name, @Param("longName") String longName,
                                             @Param("fullName") String fullName,
                                             @Param("version") String version, @Param("hashValue") String hashValue,
                                             @Param("returnType") String returnType, @Param("lineCount") Integer lineCount,
                                             @Param("comment") String comment, @Param("returnComment") String returnComment,
                                             @Param("parentId") String parentId, @Param("createTime") String createTime,
                                             @Param("updateTime") String updateTime);

    List<ProcedureDto> selectFunctionsByProjectId(@Param("projectId")int projectId, @Param("projectVersion")String projectVersion);

    List<ProcedureDto> selectFunctionsByProjectId2(@Param("startNum")int startNum,
                                                   @Param("pageSize")int pageSize,
                                                   @Param("projectId")int projectId,
                                                   @Param("projectVersion")String projectVersion);

    Integer selectProcedureCount(@Param("projectId") Integer projectId, @Param("projectVersion") String projectVersion, @Param("insertFlag") Integer insertFlag);

    List<ProjectEntity> findProjectByProcedureId(@Param("procedureId") Integer procedureId);

    List<ProcedureDto> getFuncCallByProAndV(@Param("projectId")int projectId, @Param("projectVersion")String projectVersion);

    List<ProcedureEntity> getOverloadedFunByLongName(@Param("projectId")int projectId, @Param("projectVersion")String projectVersion, @Param("longName") String longName);

    List<ProcedureEntity> findNotOtherProjectUse(@Param("projectId") int projectId);

    List<ProcedureEntity> findNotOtherProjectVersionUse(@Param("projectId") int projectId, @Param("projectVersion")String projectVersion);

    List<ProcedureEntity> findNotOtherUsePackage();

    List<ProcedureEntity> findProjectUse(@Param("projectId") int projectId);
}
