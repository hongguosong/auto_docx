package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

///**
// * 自带springBoot启动，同时将pom.xml里的tomcat注释掉
// */
//@SpringBootApplication
//public class DashflatApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(DashflatApplication.class, args);
//	}
//}

/**
 * 修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
 * 打包发布、在tomcat中运行，需要执行下面方法, 同时将pom.xml里的tomcat注释取消掉
 * 打包的时候要修改application.yml为pro环境
 * 在linux下运行，还要修改配置文件application-pro.yml，改成linux路径
 */
@SpringBootApplication
public class DashflatApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(DashflatApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(DashflatApplication.class, args);
	}
}
