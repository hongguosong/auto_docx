package com.example.demo.service;

import com.example.demo.dao.OrganizationDao;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private UserService userService;

    public List<OrganizationEntity> selectOrgs(){
        return organizationDao.selectOrgs();
    }

    public List<OrganizationEntity> selectOrgsByPId(int startNum, int pageSize, String orgName, String orgDesc, int id){
        return organizationDao.selectOrgsByPId(startNum, pageSize, orgName, orgDesc, id);
    }

    public int selectTotalCount(String orgName, String orgDesc, int id){
        return organizationDao.selectTotalCount(orgName, orgDesc, id);
    }

    public void insertOrg(String orgName, String orgDesc, int pId){
        organizationDao.insertOrg(orgName, orgDesc, pId);
    }

    public void updateOrg(int id, String orgName, String orgDesc){
        organizationDao.updateOrg(id, orgName, orgDesc);
    }

    public String deleteOrg(int id, int pId){
        List<UserEntity> userEntities =  userService.selectUserByOrgId(id);
        List<OrganizationEntity> organizationEntities = organizationDao.selectOrgsByPId2(id);
        if (organizationEntities.size() > 0){
            return "请先删除该部门的子部门";
        }
        if (userEntities.size() > 0){
            return "该组织下存在人员，不可删除";
        }
        organizationDao.deleteOrg(id);
        if (organizationDao.selectOrgsByPId2(pId).size() == 0){
            organizationDao.updateIsParentById(pId, 0);
        }
        return "删除成功";
    }

    public List<OrganizationEntity> getOrgsByPId(int pId){
        return organizationDao.selectOrgsByPId2(pId);
    }

    public void updateIsParentById(int id){
        organizationDao.updateIsParentById(id, 1);
    }
}
