package com.example.demo.config;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.config </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/10/24 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.example.demo.security.dao.UserRepository;
import com.example.demo.security.dto.SecurityUser;
import com.example.demo.security.entity.Person;
import com.example.demo.security.entity.User;
import com.example.demo.security.service.LDAPAuthentication;
import com.example.demo.security.service.OdmPersonRepo;
import com.example.demo.util.Constants;
import com.example.demo.util.LdapEncoderByMd5;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    private static final String KEY = "dashflat.com";
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${ldaprolestr}")
    private String ldaprolestr;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OdmPersonRepo odmPersonRepo;

    @Autowired
    private LDAPAuthentication ldap;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();

        http.authorizeRequests()
            //表单登录方式
            .antMatchers("/images/**","/css/**","/fonts/**","/js/**","/myjs/**","/scss/**","/vendors/**","gulpfiles.js","projectVersion","/payload","/file-upload/downloadReport").permitAll()
            .and().formLogin().loginPage("/login").failureUrl("/login?error=true").permitAll().successHandler(loginSuccessHandler())
            .and().logout().permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler())
            //.and().rememberMe().key(KEY) // 启用 remember me
            .and().exceptionHandling().accessDeniedPage("/403")  // 处理异常，拒绝访问就重定向到 403 页面
            .and()
            .authorizeRequests()
            //任何请求
            .anyRequest()
            //需要身份认证
            .authenticated()
                .and()
                .headers().frameOptions().disable()
            .and()
            //关闭跨站请求防护
            .csrf().disable()
            .sessionManagement().invalidSessionUrl("/login").maximumSessions(10).expiredUrl("/login");
    }

    /**
     * 认证信息管理
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        // 这个里面已经验证了，不用再验证了
        auth.authenticationProvider(loginAuthenticationProvider());
        //auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(false);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { //密码加密
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() { //登出处理
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                try {
                    SecurityUser user = (SecurityUser) authentication.getPrincipal();
                    log.info("USER : " + user.getUsername() + " LOGOUT SUCCESS !  ");
                } catch (Exception e) {
                    log.info("LOGOUT EXCEPTION , e : " + e.getMessage());
                }
                httpServletResponse.sendRedirect("/login");
            }
        };
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler() { //登入处理
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                User userDetails = (User) authentication.getPrincipal();
                log.info("USER : " + userDetails.getUsername() + " LOGIN SUCCESS !  ");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }

//    @Bean
//    public SimpleUrlAuthenticationFailureHandler loginFailiureHandler() {
//        return new SimpleUrlAuthenticationFailureHandler() {
//            @Override
//            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//                //super.onAuthenticationFailure(request, response, exception);
//                response.getWriter().write(objectMapper.writeValueAsString(exception.getMessage()));
//            }
//        };
//    }

    public User verifyLdapUser(String inputName, String inputPassword){
        try{
//            inputPassword = LdapEncoderByMd5.encode(inputPassword);
//            Person p = odmPersonRepo.findByCn(inputName);
//            inputPassword = LdapEncoderByMd5.getCrypt(inputPassword);
//            Person p = odmPersonRepo.findByUid(inputName);
//            String password = new String(p.getUserPassword(), "utf-8");
//            String temp = new String(p.getUserPassword(), "utf-8");
//            String[] psList = temp.split(",");
//            if(psList != null && psList.length > 0){
//                for(int i=0; i<psList.length; i++){
//                    char c = (char) Integer.valueOf(psList[i]).intValue();
//                    password.append(c);
//                }
//            }
            if(!"admin".equals(inputName)){
                Person p = new Person();
                if(ldap.authenricate(inputName, inputPassword,p) == true){
                    User user = new User();
                    user.setId(-1);
                    user.setDeleteId(2);
                    user.setRoleId(ldaprolestr);
                    user.setLogoutId(2);
                    user.setUsername(p.getUid());
                    user.setNickName(p.getSn());
                    user.setPassword(inputPassword);
                    user.setEmail(p.getMail());
                    return  user;
                }
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }finally {
            ldap.close();
        }
        return null;
    }

//    @Bean
//    public UserDetailsService userDetailsService() {    //用户登录实现
//        return new UserDetailsService() {
//            @Autowired
//            private UserRepository userRepository;
//
//            @Override
//            public UserDetails loadUserByUsername(String s) throws AuthenticationException {
//                //获取当前的HttpServletRequest
//                HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//                //获取当前页面的登录密码
//                String password = request.getParameter("password");
//                //自定义错误信息
//                if ("".equals(s)) throw new BadCredentialsException("用户名不能为空");
//                if (password == null || "".equals(password)) throw new BadCredentialsException("密码不能为空");
//
//                User user = verifyLdapUser(s,password);
//                if(user != null){
//                    return new SecurityUser(user);
//                }
//
//                user = userRepository.findByUsername(s);
//                if(user !=null && password != null){
//                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//                    //BCryptPasswordEncoder 的 matches 方法将未加密的密码与加密后的密码进行比较
//                    if (!encoder.matches(password, user.getPassword())) throw new BadCredentialsException("密码错误");
//                }
//                if (user == null || user.getDeleteId() == 1) throw new BadCredentialsException("用户名 " + s + " 不存在");
//                if (user.getLogoutId() == 1) throw new DisabledException("用户已注销");
//                return new SecurityUser(user);
//            }
//        };
//    }

    class LoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
        @Autowired
        private UserRepository userRepository;

        @Override
        protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
            System.out.println("haha");
        }

        @Override
        protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
            //获取当前的HttpServletRequest
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            //获取当前页面的登录密码
            String password = request.getParameter("password");
            //自定义错误信息
            if ("".equals(username)) throw new BadCredentialsException("用户名不能为空");
            if (password == null || "".equals(password)) throw new BadCredentialsException("密码不能为空");

            User user = verifyLdapUser(username,password);
            if(user != null){
                return new SecurityUser(user);
            }

            user = userRepository.findByUsername(username);
            if(user !=null && password != null){
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                //BCryptPasswordEncoder 的 matches 方法将未加密的密码与加密后的密码进行比较
                if (!encoder.matches(password, user.getPassword())) throw new BadCredentialsException("密码错误");
            }
            if (user == null || user.getDeleteId() == 1) throw new BadCredentialsException("用户名 " + username + " 不存在");
            if (user.getLogoutId() == 1) throw new DisabledException("用户已注销");
            return new SecurityUser(user);
        }

    }

    @Bean
    public LoginAuthenticationProvider loginAuthenticationProvider() {
        return new LoginAuthenticationProvider();
    }
}
