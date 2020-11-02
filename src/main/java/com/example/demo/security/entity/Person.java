package com.example.demo.security.entity;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.entity </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/4/22 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;

@Entry(base = "cn=users,dc=nas,dc=509,dc=com", objectClasses = "inetOrgPerson")
@Data
public class Person {

    @Id
//    @JsonIgnore
    private Name dn;
    @DnAttribute(value = "uid")
    private String uid;
    @Attribute(name = "cn")
    private String cn;
    @Attribute(name = "sn")
    private String sn;
//    @Attribute(name = "userPassword", type= Attribute.Type.BINARY)
//    private byte[] userPassword;
    @Attribute(name = "userPassword", type= Attribute.Type.STRING)
    private String userPassword;
    @Attribute(name = "mail")
    private String mail;

//    public Person(String cn) {
//        Name dn = LdapNameBuilder.newInstance()
//                .add("ou", "people")
//                .add("cn", cn)
//                .build();
//        this.dn = dn;
//    }
//    public Person(){}
//
//    /* getter   */
//    public Name getDn() {
//        return dn;
//    }
//
//    public String getCn() {
//        return cn;
//    }
//
//    public String getSn() {
//        return sn;
//    }
//
//    public String getUserPassword() {
//        return userPassword;
//    }
//
//    /* setter   */
//    public void setDn(Name dn) {
//        this.dn = dn;
//    }
//
//    public void setCn(String cn) {
//        this.cn = cn;
//        if(this.dn==null){
//            Name dn = LdapNameBuilder.newInstance()
//                    .add("ou", "people")
//                    .add("cn", cn)
//                    .build();
//            this.dn = dn;
//        }
//    }
//
//    public void setSn(String sn) {
//        this.sn = sn;
//    }
//
//    public void setUserPassword(String userPassword) {
//        this.userPassword = userPassword;
//    }
//
//    @Override
//    public String toString() {
//        return "Person{" +
//                "dn=" + dn.toString() +
//                ", cn='" + cn + '\'' +
//                ", sn='" + sn + '\'' +
//                ", userPassword='" + userPassword + '\'' +
//                '}';
//    }
}
