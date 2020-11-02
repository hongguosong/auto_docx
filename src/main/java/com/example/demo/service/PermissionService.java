package com.example.demo.service;

import com.example.demo.dao.PermissionDao;
import com.example.demo.entity.PermissionEntity;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Value("${ldaprolestr}")
    private String ldaprolestr;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private  RolePermissionService rolePermissionService;

    @Autowired
    private UserService userService;

    public List<PermissionEntity> selectAllPermissions(){
        return permissionDao.selectAllPermissions();
    }

    public List<PermissionEntity> selectPermissions(int startNum, int pageSize,String menuName, String url, String menuCode, int parentId){
        return permissionDao.selectPermissions(startNum, pageSize, menuName, url, menuCode, parentId);
    }

    public int selectTotalCount(String menuName, String url, String menuCode, int parentId){
        return permissionDao.selectTotalCount(menuName, url, menuCode, parentId);
    }

    public List<PermissionEntity> selectCurrentPermission(int id){
        String roleIdStr = "";
        if(id == -1){
            roleIdStr = ldaprolestr;
        }else{
            roleIdStr = userService.selectRoleIdById(id);
        }
        if (!roleIdStr.equals("")){
            String[] roleIdArr = roleIdStr.split(",");
            List<PermissionEntity> listAll = new ArrayList<>();
            for (String roleId : roleIdArr){
                List<PermissionEntity> permissionEntities = permissionDao.selectPermissionByRoleId(Integer.parseInt(roleId));
                listAll.addAll(permissionEntities);
            }
            listAll = listAll.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PermissionEntity:: getId))), ArrayList::new));
            return listAll;
        } else {
            return null;
        }
    }

    public void insertPermission(String menuName, String url, String menuCode, int parentId){
        permissionDao.insertPermission(menuName, url, menuCode, parentId);
    }

    public void updatePermission(int id, String menuName, String url, String menuCode, int parentId){
        permissionDao.updatePermission(id, menuName, url, menuCode, parentId);
    }

    public void deletePermission(int id){
        rolePermissionService.deletePermission(id);
        permissionDao.deletePermission(id);
    }

    public List<PermissionEntity> selectPermissionByParentId(int parentId){
        return permissionDao.selectPermissionByParentId(parentId);
    }

    public int[] selectPrePermissionIds(int roleId){
        return permissionDao.selectPrePermissionIds(roleId);
    }
}
