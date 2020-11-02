package com.example.demo.security.dao;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.security.dao </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/10/25 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{
    User findByUsername(String username);
}
