package com.example.demo.dao;

import com.example.demo.entity.OrganizationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrganizationDao {
    List<OrganizationEntity> selectOrgs();

    List<OrganizationEntity> selectOrgsByPId(@Param("startNum")int startNum,
                                             @Param("pageSize")int pageSize,
                                             @Param("orgName")String orgName,
                                             @Param("orgDesc")String orgDesc,
                                             @Param("id")int id);

    List<OrganizationEntity> selectOrgsByPId2(int id);

    int selectTotalCount(@Param("orgName")String orgName,
                         @Param("orgDesc")String orgDesc,
                         @Param("id")int id);

    void insertOrg(@Param("orgName")String orgName, @Param("orgDesc")String orgDesc, @Param("pId")int pId);

    void updateOrg(@Param("id")int id, @Param("orgName")String orgName, @Param("orgDesc")String orgDesc);

    void deleteOrg(@Param("id")int id);

    void updateIsParentById(int id, int isParent);
}
