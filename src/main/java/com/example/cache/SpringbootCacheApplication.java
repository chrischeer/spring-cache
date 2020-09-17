package com.example.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @description
 * @author Chris
 * @date 2020/6/6 0:10
 *
 */
@SpringBootApplication
//@EnableScheduling
@EnableTransactionManagement
@EnableCaching
@MapperScan("com.example.cache.dao")
public class SpringbootCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootCacheApplication.class, args);
    }

}
