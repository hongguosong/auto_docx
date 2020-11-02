package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.entity.PermissionEntity;
import com.example.demo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/selectAllPermissions")
    public List<PermissionEntity> selectAllPermissions(){
        return permissionService.selectAllPermissions();
    }

    @RequestMapping("/selectPermissions")
    public JSONObject selectPermissions(@RequestBody JSONObject jsonObject){
        int pageIndex = jsonObject.getInteger("pageIndex");
        int pageSize = jsonObject.getInteger("pageSize");
        String menuName = jsonObject.getString("menuName");
        String url = jsonObject.getString("url");
        String menuCode = jsonObject.getString("menuCode");
        int parentId = 0;
        if (jsonObject.getInteger("parentId") != null){
            parentId = jsonObject.getInteger("parentId");
        }
        int startNum = pageIndex * pageSize;
        List<PermissionEntity> permissionEntities = permissionService.selectPermissions(startNum, pageSize, menuName, url, menuCode, parentId);
        int totalCount = permissionService.selectTotalCount(menuName, url, menuCode, parentId);
        JSONObject object = new JSONObject();
        object.put("data", permissionEntities);
        object.put("itemsCount", totalCount);
        return object;
    }

    @RequestMapping("/insertPermission")
    public String insertPermission(@RequestBody JSONObject jsonObject){
        String menuName = jsonObject.getString("menuName");
        String url = jsonObject.getString("url");
        String menuCode = jsonObject.getString("menuCode");
        int parentId = jsonObject.getInteger("parentId");
        permissionService.insertPermission(menuName, url, menuCode, parentId);
        return JsonResult.success();
    }

    @RequestMapping("/updatePermission")
    public String updatePermission(@RequestBody JSONObject jsonObject){
        int id = jsonObject.getInteger("id");
        String menuName = jsonObject.getString("menuName");
        String url = jsonObject.getString("url");
        String menuCode = jsonObject.getString("menuCode");
        int parentId = jsonObject.getInteger("parentId");
        permissionService.updatePermission(id, menuName, url, menuCode, parentId);
        return JsonResult.success();
    }

    @RequestMapping("/deletePermission")
    public String deletePermission(@RequestBody JSONObject jsonObject){
        int id = jsonObject.getInteger("id");
        List<PermissionEntity> permissionEntities = permissionService.selectPermissionByParentId(id);
        Map<String, Object> data = new HashMap<String, Object>();
        if (permissionEntities.size() == 0){
            permissionService.deletePermission(id);
            data.put("data", "删除成功");
        } else {
            data.put("data", "请先删除子权限");
        }
        return JsonResult.success(data);
    }

    @RequestMapping("/selectPermissionByParentId")
    public List<PermissionEntity> selectPermissionByParentId(@RequestParam("parentId")int parentId){
        return permissionService.selectPermissionByParentId(parentId);
    }
}
