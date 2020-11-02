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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {
    @Bean
    public Executor getExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);//线程池维护线程的最少数量
        executor.setMaxPoolSize(30);//线程池维护线程的最大数量
        executor.setQueueCapacity(8); //缓存队列
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //对拒绝task的处理策略
        executor.setKeepAliveSeconds(60);//允许的空闲时间
        executor.initialize();
        return executor;
    }
}
