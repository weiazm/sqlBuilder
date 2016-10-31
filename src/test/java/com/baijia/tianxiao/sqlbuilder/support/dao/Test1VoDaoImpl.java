package com.baijia.tianxiao.sqlbuilder.support.dao;

import com.baijia.tianxiao.sqlbuilder.SingleSqlBuilder;
import com.baijia.tianxiao.sqlbuilder.support.JdbcTemplateDaoSupport;
import com.baijia.tianxiao.sqlbuilder.support.vo.Test1Vo;
import com.baijia.tianxiao.sqlbuilder.support.vo.TestVo;
import org.springframework.jdbc.core.JdbcTemplate;

public class Test1VoDaoImpl extends JdbcTemplateDaoSupport<Test1Vo> {

    private JdbcTemplate testJdbcTemplate;

    /**
     * 构造函数的参数只是为测试方便加的,正式不要加参数
     *
     * @param jdbcTemplate
     */
    public Test1VoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.testJdbcTemplate = jdbcTemplate;
    }

    public int del(SingleSqlBuilder<TestVo> builder) {
        return getNamedJdbcTemplate().update(builder.toDeleteSqlByCondition(), builder.collectConditionValue());
    }

    public int count() {
        SingleSqlBuilder<Test1Vo> builder = createSqlBuilder();
        builder.count("id");

        return queryForObject(builder, Integer.class);
    }

    @Override
    protected JdbcTemplate getJdbcTemplate() {
        return this.testJdbcTemplate;
    }

    public void testOrder() {
        SingleSqlBuilder<Test1Vo> builder = createSqlBuilder();
        builder.eq("id", 1);
        // builder.desc("id", "number");
        builder.asc("test", true);
        System.out.println(builder.toSql());
    }

}