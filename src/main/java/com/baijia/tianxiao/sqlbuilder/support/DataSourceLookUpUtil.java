/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.support;

import com.baijia.tianxiao.sqlbuilder.config.SqlBuilderConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author cxm
 * @version 1.0
 * @title DataSourceLookUpUtil
 * @desc TODO
 * @date 2016年1月21日
 */
public class DataSourceLookUpUtil {

    private static Map<String, DataSource> dataSourceMaping;

    private static DataSource defaultDatasource;

    private static JdbcTemplate defaultJdbcTemplate;

    private static Map<String, JdbcTemplate> templateMaping;

    private static Environment environment;

    private static void init(ApplicationContext context) {
        if (environment == null) {
            environment = context.getBean(Environment.class);
        }
        if (dataSourceMaping == null) {
            dataSourceMaping = context.getBeansOfType(DataSource.class);
        }
        if (templateMaping == null) {
            templateMaping = context.getBeansOfType(JdbcTemplate.class);
        }
        if (defaultDatasource == null) {
            String defaultDsValue = environment.getProperty(SqlBuilderConfig.DEFAULT_DATASOURCE_BEAN_KEY);
            if (StringUtils.isNoneBlank(defaultDsValue)) {
                defaultDatasource = dataSourceMaping.get(defaultDsValue);
            }
        }
        if (defaultJdbcTemplate == null) {
            String defaultJtValue = environment.getProperty(SqlBuilderConfig.DEFAULT_JDBC_TEMPLATE_BEAN_KEY);
            if (StringUtils.isNoneBlank(defaultJtValue)) {
                defaultJdbcTemplate = templateMaping.get(defaultJtValue);
            }
        }
    }

    public static DataSource lookupDataSource(ApplicationContext context, String beanName, String catelog) {
        init(context);

        if (dataSourceMaping.size() == 1) {
            return context.getBean(DataSource.class);
        }
        // 根据catalog配置的数据库获取jdbcTemplate
        DataSource dataSource = null;
        if (StringUtils.isNoneBlank(catelog)) {
            String key = String.format(SqlBuilderConfig.DATASOURCE_BEAN_KEY_TEMPLATE, catelog);
            String value = environment.getProperty(key);
            if (StringUtils.isNoneBlank(value)) {
                dataSource = dataSourceMaping.get(value);
            }
            if (dataSource == null && StringUtils.isNoneBlank(beanName)) {
                dataSource = dataSourceMaping.get(beanName);
            }

        }
        return dataSource != null ? dataSource : defaultDatasource;
    }

    public static JdbcTemplate lookupJdbcTemplate(ApplicationContext context, String beanName, String catelog) {
        init(context);

        if (templateMaping.size() == 1) {
            return context.getBean(JdbcTemplate.class);
        }
        // 根据catalog配置的数据库获取jdbcTemplate
        JdbcTemplate jdbcTemplate = null;
        if (StringUtils.isNoneBlank(catelog)) {
            String key = String.format(SqlBuilderConfig.JDBC_TEMPLATE_BEAN_KEY_TEMPLATE, catelog);
            String value = environment.getProperty(key);
            if (StringUtils.isNoneBlank(value)) {
                jdbcTemplate = templateMaping.get(value);
            }
            if (jdbcTemplate == null && StringUtils.isNoneBlank(beanName)) {
                jdbcTemplate = templateMaping.get(beanName);
            }
        }
        return jdbcTemplate != null ? jdbcTemplate : defaultJdbcTemplate;
    }

}
