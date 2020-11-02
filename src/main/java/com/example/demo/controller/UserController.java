package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {
    @Value("${uploadPath}")
    private String basePath;

    @Autowired
    private UserService userService;

//    @RequestMapping("/selectUsers")
//    public JSONObject selectUsers(@RequestParam(value = "pageIndex", defaultValue = "0", required = false)Integer pageIndex,
//                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false)Integer pageSize,
//                                  @RequestParam("orgId")int orgId){
//        int startNum = pageIndex * pageSize;
//        List<UserEntity> userEntities = userService.selectUsers(startNum, pageSize, orgId);
//        int totalCount = userService.selectTotalCount();
//        JSONObject object = new JSONObject();
//        object.put("data", userEntities);
//        object.put("itemsCount", totalCount);
//        return object;
//    }

    @RequestMapping("/selectUsers2")
    public List<UserDto> selectUsers2(@RequestParam("orgId")int orgId){
        return userService.selectUsers2(orgId);
    }

    @RequestMapping("/insertUser")
    public void insertUser(@RequestParam("username")String username,
                           @RequestParam("nickName")String nickName,
                           @RequestParam("email")String email,
                           @RequestParam("createTime")String createTime,
                           @RequestParam("updateTime")String updateTime,
                           @RequestParam("roleId")String roleId,
                           @RequestParam("orgId")int orgId,
                           @RequestParam("logoutId")int logoutId){
        Timestamp create = Timestamp.valueOf(createTime);
        Timestamp update = Timestamp.valueOf(updateTime);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("123456");
        userService.insertUser(username, nickName, password, email, create, update, roleId, orgId, logoutId);
    }

    @RequestMapping("/updateUser")
    public void updateUser(@RequestParam("id")int id,
                           @RequestParam("username")String username,
                           @RequestParam("nickName")String nickName,
                           @RequestParam("email")String email,
                           @RequestParam("createTime")String createTime,
                           @RequestParam("updateTime")String updateTime,
                           @RequestParam("roleId")String roleId,
                           @RequestParam("logoutId")int logoutId){
        Timestamp create = Timestamp.valueOf(createTime);
        Timestamp update = Timestamp.valueOf(updateTime);
        userService.updateUser(id, username, nickName, email, create, update, roleId, logoutId);
    }

    @RequestMapping("/deleteUser")
    public void deleteUser(@RequestParam("id")int id){
        userService.deleteUser(id);
    }

    @RequestMapping("/selectUserByUsername")
    public List<UserEntity> selectUserByUsername(@RequestParam("username")String username){
        return userService.selectUserByUsername(username);
    }

    @RequestMapping("/userSettings")
    public String userSettings(@RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                               @RequestParam("username")String username,
                               @RequestParam("email")String email,
                               @RequestParam("passwordOld")String passwordOld,
                               @RequestParam("passwordNew")String passwordNew,
                               @RequestParam("passwordRe")String passwordRe,
                               @RequestParam("isChecked")Boolean isChecked){
        return userService.userSettings(avatarFile,username,email,passwordOld,passwordNew,passwordRe,isChecked,basePath);
    }

    //从本地服务器读取图片并显示
    @RequestMapping("/showAvatar")
    public void showAvatar(HttpServletRequest request, HttpServletResponse response){
        String path = userService.selectAvatar();
        path = basePath + File.separator + path;
        FileInputStream fis = null;
        OutputStream os = null;
        File file = new File(path);
        if(file.exists()){
            try {
                fis = new FileInputStream(file);
                long size = file.length();
                byte[] temp = new byte[(int) size];
                fis.read(temp, 0, (int) size);
                fis.close();
                byte[] data = temp;
                response.setContentType("image/png");
                os = response.getOutputStream();
                os.write(data);
                os.flush();
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
