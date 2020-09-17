package com.example.cache.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
public class DruidConfig {

    private static final Logger logger = LoggerFactory.getLogger(DruidConfig.class);

    @Bean(name = "dataSource")
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource druidDataSource(DataSourceProperties properties) {
        DruidDataSource dataSource = createDataSource(properties);
        DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
        String validationQuery = databaseDriver.getValidationQuery();
        if (validationQuery != null) {
            dataSource.setTestOnBorrow(true);
            dataSource.setValidationQuery(validationQuery);
        }

        return dataSource;
    }

//    @Bean(name = "pageHelper")
//    @ConfigurationProperties("pagehelper")
//    public PageInterceptor pageHelper() {
//        PageInterceptor pageHelper = new PageInterceptor();
//        Properties properties = new Properties();
//        /**默认false，设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用*/
//        properties.setProperty("offsetAsPageNum", "true");
//        /**默认false，设置为true时，使用RowBounds分页会进行count查询 */
//        properties.setProperty("rowBoundsWithCount", "true");
//        /** 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据 */
//        properties.setProperty("reasonable", "true");
//        /** always总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page */
//        properties.setProperty("returnPageInfo", "check");
//        /** 支持通过Mapper接口参数来传递分页参数 */
//        properties.setProperty("supportMethodsArguments", "false");
//        /**  配置数据库的方言  */
//        properties.setProperty("helperDialect", "mysql");
//        pageHelper.setProperties(properties);
//        return pageHelper;
//    }
//
//    @Bean(name = "masterSqlSessionFactory")
//    @Primary
//    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource, PageInterceptor pageHelper) throws Exception {
//        logger.info("load SpringBootVFS");
//        /**DefaultVFS在获取jar上存在问题，使用springboot只能修改*/
//        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/hjkj/safe/attend/mapper/*.xml"));
//        sqlSessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis/mybatis-config.xml"));
//        return sqlSessionFactoryBean.getObject();
//    }


    @Bean
    public ServletRegistrationBean druidServlet() {
        logger.info("init Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // IP白名单
        servletRegistrationBean.addInitParameter("allow", "*");
        // IP黑名单(共同存在时，deny优先于allow)
//        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
        //控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "hjkj");
        servletRegistrationBean.addInitParameter("loginPassword", "hjkj");
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        //创建过滤器
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.setName("druidWebStatFilter");
        //设置过滤器过滤路径
        filterRegistrationBean.addUrlPatterns("/*");
        //忽略过滤的形式
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }


    @SuppressWarnings("unchecked")
    private <T> T createDataSource(DataSourceProperties properties) {
        return (T) properties.initializeDataSourceBuilder().type((Class<? extends DataSource>) DruidDataSource.class).build();
    }


}