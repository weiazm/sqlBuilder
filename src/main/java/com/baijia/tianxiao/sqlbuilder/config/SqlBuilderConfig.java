package com.baijia.tianxiao.sqlbuilder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 导入数据源配置文件
 *
 * @author cxm
 * @version 1.0
 * @title SqlBuilderConfig
 * @desc TODO
 * @date 2015年12月17日
 */
@Configuration
@PropertySource("classpath:db_config.properties")
public class SqlBuilderConfig {

    /**
     * 获取默认jdbctemplate bean 的key
     */
    public final static String DEFAULT_JDBC_TEMPLATE_BEAN_KEY = "default.jdbctemplate.name";

    /**
     * 获取默认datasource bean 的key
     */
    public final static String DEFAULT_DATASOURCE_BEAN_KEY = "default.datasource.name";

    public final static String JDBC_TEMPLATE_BEAN_KEY_TEMPLATE = "jdbctemplate.%s.name";

    public final static String DATASOURCE_BEAN_KEY_TEMPLATE = "datasource.%s.name";

}
