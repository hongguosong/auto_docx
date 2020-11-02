package com.example.demo.security.service;/*
 * <p>项目名称: Chapter3-2-10 </p>
 * <p>包名称: com.didispace </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/4/26 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.security.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

@Service
public class LDAPAuthentication {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${myldap.username}")
    private String ROOT;  // 根，根据自己情况修改
    @Value("${myldap.password}")
    private String PASSWORD;  // 根，根据自己情况修改
    @Value("${myldap.url}")
    private String URL;  //// 根，根据自己情况修改
    @Value("${myldap.basedn}")
    private String BASEDN;  // 根据自己情况进行修改
    private final String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private LdapContext ctx = null;
    private final Control[] connCtls = null;

    private void LDAP_connect() {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
        env.put(Context.PROVIDER_URL, URL + BASEDN);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        //String root = "cn=Manager,dc=nas,dc=509,dc=com";  // 根，根据自己情况修改
        env.put(Context.SECURITY_PRINCIPAL, ROOT);   // 管理员
        env.put(Context.SECURITY_CREDENTIALS, PASSWORD);  // 管理员密码

        try {
            ctx = new InitialLdapContext(env, connCtls);
            LOGGER.error( "认证成功" );

        } catch (AuthenticationException e) {
            LOGGER.error("认证失败：");
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.error("认证出错：");
            LOGGER.error(e.getMessage());
        }
    }

    public void close(){
        if (ctx != null) {
            try {
                ctx.close();
            }
            catch (NamingException e) {
                LOGGER.error(e.getMessage());
            }

        }
    }

    private String getUserDN(String uid, Person person) {
        String userDN = "";
        LDAP_connect();
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> en = ctx.search("", "uid=" + uid, constraints);
            if (en == null || !en.hasMoreElements()) {
                LOGGER.error("未找到该用户");
            }
            // maybe more than one element
            while (en != null && en.hasMoreElements()) {
                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;
                    userDN += si.getName();
                    userDN += "," + BASEDN;
                    Attributes attrs = si.getAttributes();
                    if(attrs != null && attrs.size() > 0){
                        Attribute attr = attrs.get("mail");
                        if(attr != null && attr.size()>0){
                            person.setMail(attr.get(0).toString());
                        }else{
                            LOGGER.error("未找到用户的mail");
                        }
                        attr = attrs.get("cn");
                        if(attr != null && attr.size()>0){
                            person.setCn(attr.get(0).toString());
                        }else{
                            LOGGER.error("未找到用户的cn");
                        }
                        attr = attrs.get("sn");
                        if(attr != null && attr.size()>0){
                            person.setSn(attr.get(0).toString());
                        }else{
                            LOGGER.error("未找到用户的Sn");
                        }
                        attr = attrs.get("uid");
                        if(attr != null && attr.size()>0){
                            person.setUid(attr.get(0).toString());
                        }else{
                            LOGGER.error("未找到用户的uid");
                        }
//                        attr = attrs.get("userPassword");
//                        if(attr != null && attr.size()>0){
//                            person.setUserPassword(attr.get(0).toString());
//                        }else{
//                            LOGGER.error("未找到用户的userPassword");
//                        }
                    }else{
                        LOGGER.error("未获取到用户的属性信息");
                    }
                } else {
                    LOGGER.error(obj.toString());
                }
            }
        } catch (Exception e) {
            LOGGER.error("查找用户时产生异常。");
            LOGGER.error(e.getMessage());
        }

        return userDN;
    }

    public boolean authenricate(String UID, String password, Person person) {
        boolean valide = false;
        String userDN = getUserDN(UID,person);

        try {
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
            ctx.reconnect(connCtls);
            LOGGER.error(userDN + " 验证通过");
            valide = true;
        } catch (AuthenticationException e) {
            LOGGER.error(userDN + " 验证失败");
            LOGGER.error(e.toString());
            valide = false;
        } catch (NamingException e) {
            LOGGER.error(userDN + " 验证失败");
            valide = false;
        }

        return valide;
    }

//    public static void main(String[] args) {
//        Person person = new Person();
//        LDAPAuthentication ldap = new LDAPAuthentication();
//        if(ldap.authenricate("xiejiahua", "password",person) == true){
//            System.out.println( "该用户认证成功" );
//        }
//        ldap.close();
//    }
}
