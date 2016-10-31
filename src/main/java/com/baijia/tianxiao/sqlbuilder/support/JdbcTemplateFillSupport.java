/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.support;

import com.baijia.tianxiao.sqlbuilder.annotation.Entity;
import com.baijia.tianxiao.sqlbuilder.annotation.Table;
import com.baijia.tianxiao.sqlbuilder.exception.NoSuitableJdbcTemplateException;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * @author cxm
 * @version 1.0
 * @title JdbcTemplateInit
 * @desc TODO
 * @date 2015年12月3日
 */
public class JdbcTemplateFillSupport<T> implements ApplicationContextAware, InitializingBean {

    @Getter
    protected Class<T> entityClass;

    protected ApplicationContext context;

    private JdbcTemplate template;
    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     *
     */
    public JdbcTemplateFillSupport() {
    }

    protected NamedParameterJdbcTemplate getNamedJdbcTemplate() {
        if (namedParameterJdbcTemplate == null) {
            if (this.template != null) {
                this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.template);
            } else if (this.dataSource != null) {
                this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            } else {
                throw new NoSuitableJdbcTemplateException(this.entityClass);
            }
        }
        return this.namedParameterJdbcTemplate;
    }

    protected JdbcTemplate getJdbcTemplate() {
        throw new UnsupportedOperationException("need to be implement.");
    }

    protected DataSource getDataSource() {
        throw new UnsupportedOperationException("need to be implement.");
    }

    private boolean fillJdbcTemplate(String beanName, String catelog) {
        if (this.context == null) {
            return false;
        }
        this.template = DataSourceLookUpUtil.lookupJdbcTemplate(context, beanName, catelog);
        return this.template != null;
    }

    private boolean fillDataSource(String beanName, String catelog) {
        if (this.context == null) {
            return false;
        }
        this.dataSource = DataSourceLookUpUtil.lookupDataSource(context, beanName, catelog);
        return this.dataSource != null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Entity entity = this.entityClass.getAnnotation(Entity.class);
        Table table = this.entityClass.getAnnotation(Table.class);
        String beanName = entity != null ? entity.dataSourceBeanName() : null;
        String catelog = table != null ? table.catalog() : null;
        boolean init = fillJdbcTemplate(beanName, catelog);
        if (!init) {
            try {
                this.template = getJdbcTemplate();
            } catch (UnsupportedOperationException e) {

            }
        }
        if (this.template == null) {
            fillDataSource(beanName, catelog);
            if (this.dataSource == null) {
                try {
                    this.dataSource = getDataSource();
                } catch (UnsupportedOperationException e) {

                }
            }
        }
    }

}
