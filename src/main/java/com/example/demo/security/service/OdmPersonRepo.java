package com.example.demo.security.service;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.security.service </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/4/23 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.security.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class OdmPersonRepo {
    @Autowired
    private LdapTemplate ldapTemplate;

    public Person create(Person person){
        ldapTemplate.create(person);
        return person;
    }

    public List<Person> findAll(){
        return ldapTemplate.findAll(Person.class);
    }

    public Person findByCn(String cn){
        return ldapTemplate.findOne(query().where("cn").is(cn),Person.class);
    }

    public Person findByUid(String uid){
        return ldapTemplate.findOne(query().where("uid").is(uid),Person.class);
    }

    public Person modifyPerson(Person person){
        ldapTemplate.update(person);
        return person;
    }

    public void deletePerson(Person person){
        ldapTemplate.delete(person);
    }
}
