package com.example.demo.service;

import com.example.demo.controller.HomeController;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CurrentUserService currentUserService;

    public UserDto selectCurrentUserInfo(String username){
        return userDao.selectCurrentUserInfo(username);
    }

//    public List<UserEntity> selectUsers(int startNum, int pageSize, int orgId){
//        return userDao.selectUsers(startNum, pageSize, orgId);
//    }

    public List<UserDto> selectUsers2(int orgId){
        List<UserDto> userDtos = userDao.selectUsers2(orgId);
        for (UserDto userDto : userDtos) {
            if (userDto.getLogoutId() == 2){
                userDto.setLogoutName("否");
            } else {
                userDto.setLogoutName("是");
            }
            if (!userDto.getRoleId().equals("")){
                String roleIdStr = userDto.getRoleId();
                String[] roleIdArr = roleIdStr.split(",");
                List<String> roleNameList = new ArrayList<>();
                for (String s : roleIdArr) {
                    String roleName = roleService.selectRoleNameById(Integer.parseInt(s));
                    roleNameList.add(roleName);
                }
                String roleNameStr = String.join(",", roleNameList);
                userDto.setRoleName(roleNameStr);
            } else {
                userDto.setRoleName("");
            }
        }
        return userDtos;
    }

    public int selectTotalCount(){
        return userDao.selectTotalCount();
    }

    public void insertUser(String username, String nickName, String password, String email, Timestamp createTime, Timestamp updateTime, String roleId, int orgId, int logoutId){
        userDao.insertUser(username, nickName, password, email, createTime, updateTime, roleId, orgId, logoutId);
    }

    public void updateUser(int id, String username, String nickName, String email, Timestamp createTime, Timestamp updateTime, String roleId, int logoutId){
        userDao.updateUser(id, username, nickName, email, createTime, updateTime, roleId, logoutId);
    }

    public void deleteUser(int id){
        userDao.deleteUser(id);
    }

    public List<UserEntity> selectUserByOrgId(int orgId){
        return userDao.selectUserByOrgId(orgId);
    }

    public List<UserEntity> selectUserByRoleId(int roleId){
       return userDao.selectUserByRoleId(roleId);
    }

    public void updateRoleId(int id, String roleId){
        userDao.updateRoleId(id, roleId);
    }

    public String selectRoleIdById(int id){
       return userDao.selectRoleIdById(id);
    }

    public List<UserEntity> selectUserByUsername(String username){
        return userDao.selectUserByUsername(username);
    }

    public String userSettings(MultipartFile avatarFile, String username, String email, String passwordOld, String passwordNew, String passwordRe, Boolean isChecked, String basePath){
        if(currentUserService.getUser().getId() == -1){
            return "非本系统用户,不允许修改";
        }
        String avatar = selectAvatar();
        if (avatarFile != null){
            // 文件后缀
            String suffix = avatarFile.getOriginalFilename().substring(avatarFile.getOriginalFilename().lastIndexOf("."));
            // 上传文件名
            String avatarFileName = UUID.randomUUID() + suffix;

            String path = File.separator+"picture"+File.separator+"avatar";
            File dir = new File(basePath, path);
            if (!dir.exists()){
                dir.mkdirs();
            }

            //保存到本地文件
            File file = new File(dir, avatarFileName);
            if (file.exists()){
                file.delete();
            }
            try {
                avatarFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            avatar = file.getAbsolutePath();
        }
        if (email.equals("")){
            return "请输入邮箱";
        }
        String password = currentUserService.getUser().getPassword();
        if (isChecked){
            if (passwordOld.equals("")){
                return "请输入旧密码";
            }
            if (passwordNew.equals("")){
                return "请输入新密码";
            }
            if (passwordRe.equals("")){
                return "请再次输入新密码";
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(passwordOld, password)){
                return "旧密码输入错误";
            }
            if (!passwordNew.equals(passwordRe)){
                return "两次新密码输入不一致";
            }
            password = encoder.encode(passwordNew);
        }
        File base = new File(basePath);
        if(avatar != null){
            avatar = avatar.replace(base.getAbsolutePath(),"");
        }
        userDao.userSettings(avatar, username, email, password);
        return "设置成功";
    }

    public String selectAvatar(){
        int id = currentUserService.getUser().getId();
        return userDao.selectAvatar(id);
    }
}
