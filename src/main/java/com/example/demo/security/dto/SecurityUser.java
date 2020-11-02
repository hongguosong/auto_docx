package com.example.demo.security.dto;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.security.dto </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/10/25 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.security.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class SecurityUser extends User implements UserDetails {
    private static final long serialVersionUID = 1L;

    public SecurityUser(User user) {
        if (user != null) {
            this.setId(user.getId());
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setEmail(user.getEmail());
            this.setRoleId(user.getRoleId());
            this.setCreateTime(user.getCreateTime());
            this.setUpdateTime(user.getUpdateTime());
            this.setNickName(user.getNickName());
            this.setLogoutId(user.getLogoutId());
            this.setDeleteId(user.getDeleteId());
            this.setAvatar(user.getAvatar());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String username = this.getUsername();
        if (username != null) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(username);
            authorities.add(authority);
        }
        return authorities;
    }

    //账户是否未过期,过期无法验证
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //指定用户是否解锁,锁定的用户无法进行身份验证
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //指示是否已过期的用户的凭据(密码),过期的凭据防止认证
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //是否可用 ,禁用的用户不能身份验证
    @Override
    public boolean isEnabled() {
        return this.getLogoutId() != 1;     //logout_id ==1 时，用户已注销，被禁用
    }


}
