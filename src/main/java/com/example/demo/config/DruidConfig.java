package com.example.demo.config;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.config </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/10/25 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DruidConfig {
    private static final String DB_PREFIX = "spring.datasource.";

    @Autowired
    private Environment environment;

    @Bean
    //@ConfigurationProperties(prefix = DB_PREFIX)
    public DataSource druidDataSource() {
        Properties dbProperties = new Properties();
        Map<String, Object> map = new HashMap<>();
        for (PropertySource<?> propertySource : ((AbstractEnvironment) environment).getPropertySources()) {
            getPropertiesFromSource(propertySource, map);
        }
        dbProperties.putAll(map);
        DruidDataSource dds;
        try {
            dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(dbProperties);
            dds.init();
        } catch (Exception e) {
            throw new RuntimeException("load datasource error, dbProperties is :" + dbProperties, e);
        }
        return dds;
    }

    private void getPropertiesFromSource(PropertySource<?> propertySource, Map<String, Object> map) {
        if (propertySource instanceof MapPropertySource) {
            for (String key : ((MapPropertySource) propertySource).getPropertyNames()) {
                if (key.startsWith(DB_PREFIX))
                    map.put(key.replaceFirst(DB_PREFIX, ""), propertySource.getProperty(key));
                else if (key.startsWith(DB_PREFIX))
                    map.put(key.replaceFirst(DB_PREFIX, ""), propertySource.getProperty(key));
            }
        }

        if (propertySource instanceof CompositePropertySource) {
            for (PropertySource<?> s : ((CompositePropertySource) propertySource).getPropertySources()) {
                getPropertiesFromSource(s, map);
            }
        }
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}