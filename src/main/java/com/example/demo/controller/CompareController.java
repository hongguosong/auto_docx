package com.example.demo.controller;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.controller </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/1 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.dto.UserDto;
import com.example.demo.entity.PermissionEntity;
import com.example.demo.security.entity.User;
import com.example.demo.service.CurrentUserService;
import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CompareController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/pages/compare")
    public String goFunctionCompare(Map<String, Object> paramMap) {
        HomeController homeController = new HomeController();
        User user = homeController.getUser();
        if(user.getId() == 0){
            return "pages/login";
        }

        List<PermissionEntity> permissionEntities = permissionService.selectCurrentPermission(user.getId());
        homeController.getMenu(paramMap, permissionEntities);

        paramMap.put("nickName", user.getNickName());
        paramMap.put("roleNames", roleService.getRoleNameStr(user.getRoleId()));
        return "pages/function_compare";
    }
}
