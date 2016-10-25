package com.baijia.tianxiao.sqlbuilder.support.dao;

import com.baijia.tianxiao.sqlbuilder.SingleSqlBuilder;
import com.baijia.tianxiao.sqlbuilder.support.JdbcTemplateDaoSupport;
import com.baijia.tianxiao.sqlbuilder.support.vo.TestVo;
import java.util.Collection;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestVoDaoImpl extends JdbcTemplateDaoSupport<TestVo> {

    private JdbcTemplate testJdbcTemplate;

    /**
     * 构造函数的参数只是为测试方便加的,正式不要加参数
     * 
     * @param jdbcTemplate
     */
    public TestVoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.testJdbcTemplate = jdbcTemplate;
    }

    public int del(SingleSqlBuilder<TestVo> builder) {
        return getNamedJdbcTemplate().update(builder.toDeleteSqlByCondition(), builder.collectConditionValue());
    }

    public int count() {
        SingleSqlBuilder<TestVo> builder = createSqlBuilder();
        builder.count("id");

        return queryForObject(builder, Integer.class);
    }

    @Override
    protected JdbcTemplate getJdbcTemplate() {
        return this.testJdbcTemplate;
    }

    public List<TestVo> tupleIn(List<String> proper, Collection<List<Object>> values) {
        SingleSqlBuilder<TestVo> builder = createSqlBuilder();
        builder.tupleIn(proper, values);
        return queryList(builder);
    }

}