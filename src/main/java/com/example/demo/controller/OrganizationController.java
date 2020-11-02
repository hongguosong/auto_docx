package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.exception.JsonResult;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/org")
@RestController
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @RequestMapping("/selectOrgs")
    public List<OrganizationEntity> selectOrgs(HttpServletRequest request){
        return organizationService.selectOrgs();
    }

    @RequestMapping(value = "/selectOrgsByPId", method = RequestMethod.POST)
    public JSONObject selectOrgsByPId(@RequestBody JSONObject jsonObject){
        int pageIndex = jsonObject.getInteger("pageIndex");
        int pageSize = jsonObject.getInteger("pageSize");
        String orgName = jsonObject.getString("orgName");
        String orgDesc = jsonObject.getString("orgDesc");
        int id = jsonObject.getInteger("id");
        int startNum = pageIndex * pageSize;
        List<OrganizationEntity> organizationEntities = organizationService.selectOrgsByPId(startNum, pageSize, orgName, orgDesc, id);
        int totalCount = organizationService.selectTotalCount(orgName, orgDesc, id);
        JSONObject object = new JSONObject();
        object.put("data", organizationEntities);
        object.put("itemsCount", totalCount);
        return object;
    }

    @RequestMapping(value = "/insertOrg",method = RequestMethod.POST)
    public String insertOrg(@RequestBody JSONObject jsonObject){
        String orgName = jsonObject.getString("orgName");
        String orgDesc = jsonObject.getString("orgDesc");
        int pId = jsonObject.getInteger("pId");
        organizationService.updateIsParentById(pId);
        organizationService.insertOrg(orgName, orgDesc, pId);
        return JsonResult.success();
    }

    @RequestMapping("/updateOrg")
    public String updateOrg(@RequestBody JSONObject jsonObject){
        int id = jsonObject.getInteger("id");
        String orgName = jsonObject.getString("orgName");
        String orgDesc = jsonObject.getString("orgDesc");
        organizationService.updateOrg(id, orgName, orgDesc);
        return JsonResult.success();
    }

    @RequestMapping("/deleteOrg")
    public String deleteOrg(@RequestBody JSONObject jsonObject){
        int id = jsonObject.getInteger("id");
        int pId = jsonObject.getInteger("pId");
        System.out.println(pId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", organizationService.deleteOrg(id, pId));
        return JsonResult.success(data);
    }

    @RequestMapping("/getOrgsByPId")
    public List<OrganizationEntity> getOrgsByPId(HttpServletRequest request){
        int pId =Integer.parseInt(request.getParameter("id"));
        List<OrganizationEntity> organizationEntityList = organizationService.getOrgsByPId(pId);
        for (OrganizationEntity organizationEntity : organizationEntityList){
            if (organizationService.getOrgsByPId(organizationEntity.getId()).size() == 0){
                organizationEntity.setIsParent(0);
            }
        }
        return organizationEntityList;
    }
}
