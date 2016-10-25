/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.support;

import com.baijia.tianxiao.sqlbuilder.SingleSqlBuilder;
import com.baijia.tianxiao.sqlbuilder.support.dao.Test1VoDaoImpl;
import com.baijia.tianxiao.sqlbuilder.support.dao.TestVoDaoImpl;
import com.baijia.tianxiao.sqlbuilder.support.vo.Test1Vo;
import com.baijia.tianxiao.sqlbuilder.support.vo.TestVo;
import com.baijia.tianxiao.sqlbuilder.util.H2Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @title JdbcTemplateDaoSupportTest
 * @desc TODO
 * @author cxm
 * @date 2015年12月3日
 * @version 1.0
 */
public class JdbcTemplateDaoSupportTest {

    private TestVoDaoImpl daoImpl;

    private Test1VoDaoImpl dao1Impl;

    private static JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void beforeClass() {
        jdbcTemplate = new JdbcTemplate(H2Util.initAndGetH2MemDataSource());

    }

    @Before
    public void init() throws Exception {
        daoImpl = new TestVoDaoImpl(jdbcTemplate);
        daoImpl.afterPropertiesSet();

        dao1Impl = new Test1VoDaoImpl(jdbcTemplate);
        dao1Impl.afterPropertiesSet();
    }

    /**
     * Test method for
     * {@link com.baijia.tianxiao.sqlbuilder.support.JdbcTemplateDaoSupport#getById(java.io.Serializable, java.lang.String[])}
     * .
     */
    @Test
    public void testGetById() {
        // 后面的4个字段属性不传,默认查询全部属性
        TestVo vo = daoImpl.getById(10, "id", "number", "test", "startTime");
        System.out.println(vo);
        dao1Impl.testOrder();
    }

    /**
     * Test method for
     * {@link com.baijia.tianxiao.sqlbuilder.support.JdbcTemplateDaoSupport#getByIds(java.util.Collection, java.lang.String[])}
     * .
     */
    @Test
    public void testGetByIds() {
        List<Long> ids = Lists.newArrayList(10l, 11l, 12l);
        List<TestVo> results = daoImpl.getByIds(ids);
        Assert.assertEquals(3, ids.size());
        Assert.assertEquals(10, results.get(0).getId().intValue());
        Assert.assertEquals(7777777, results.get(0).getNumber().intValue());
        Assert.assertEquals(11, results.get(1).getId().intValue());
        Assert.assertEquals(12, results.get(2).getId().intValue());

        SingleSqlBuilder<TestVo> builder = daoImpl.createSqlBuilder();
        builder.ge("id", 100);

        builder.le("id", 1000);
        System.out.println(builder.toUpdateSql("number"));
        System.out.println(builder.collectConditionValue());

    }

    /**
     * Test method for
     * {@link com.baijia.tianxiao.sqlbuilder.support.JdbcTemplateDaoSupport#saveAll(java.util.Collection, java.lang.String[])}
     * .
     */
    @Test
    public void testSaveAll() {
        List<TestVo> vos = Lists.newArrayList();
        vos.add(new TestVo(-111, "test1"));
        vos.add(new TestVo(-222, "test2"));
        vos.add(new TestVo(-333, null));
        daoImpl.saveAll(vos);
        System.out.println(vos);
        TestVo vo = daoImpl.getById(vos.get(0).getId());
        Assert.assertEquals(vo.getId(), vos.get(0).getId());

        SingleSqlBuilder<TestVo> builder = daoImpl.createSqlBuilder();
        builder.ge("id", vo.getId());
        daoImpl.del(builder);
        vo = daoImpl.getById(vos.get(0).getId());
        Assert.assertNull(vo);
    }

    /**
     * Test method for
     * {@link com.baijia.tianxiao.sqlbuilder.support.JdbcTemplateDaoSupport#save(java.lang.Object, java.lang.Boolean, java.lang.String[])}
     * .
     */
    @Test
    public void testSaveTBooleanStringArray() {
        TestVo testVo = new TestVo();
        // testVo.setNumber(99999999);
        testVo.setStartTime(new GregorianCalendar(2014, 5, 1).getTime());

        daoImpl.save(testVo, true);
        Long id = testVo.getId();
        TestVo result = daoImpl.getById(id);
        Assert.assertNotNull(result);
        Assert.assertNull(result.getNumber());

        System.out.println(result);
        Assert.assertTrue(testVo.getId() > 0);
        int row = daoImpl.delById(testVo.getId());
        Assert.assertEquals(1, row);
        result = daoImpl.getById(id);
        Assert.assertNull(result);

        // 动态忽略null值,不插入属性中为null的值
        daoImpl.save(testVo);
        result = daoImpl.getById(testVo.getId());
        System.out.println(result);
        Assert.assertEquals(0.0, result.getNumber());

    }

    /**
     * Test method for
     * {@link com.baijia.tianxiao.sqlbuilder.support.JdbcTemplateDaoSupport#update(java.util.Map, java.lang.Boolean, java.lang.Object, java.lang.String[])}
     * .
     */
    @Test
    public void testUpdateMapOfStringObjectBooleanTStringArray() {
        TestVo testVo = new TestVo();
        testVo.setId(23l);
        testVo.setTest("test no save null");

        daoImpl.update(testVo);
        TestVo result = daoImpl.getById(testVo.getId());
        System.out.println(result);
        testVo.setNumber(null);
        daoImpl.update(testVo, true);
        result = daoImpl.getById(testVo.getId());
        System.out.println(result);
        System.out.println(daoImpl.count());

        Map<String, Object> condition = new HashMap<>();
        // condition.put("id", 10);

        condition.put("id", Lists.newArrayList(4, 5));

        condition.put("test", "小小");

        // System.out.println(daoImpl.update(condition, "test"));

        daoImpl.update(condition, new TestVo(null, "修改后的东东"), "test");

        System.out.println(daoImpl.getAll());
    }

    @Test
    public void testAdd() {
        TestVo testVo = new TestVo();
        testVo.setTest("test no save null");
        testVo.setStartTime(new Date());

        daoImpl.save(testVo);

        Test1Vo test1Vo = new Test1Vo();
        test1Vo.setTest("test1 no save null");
        dao1Impl.save(test1Vo);

        System.out.println(dao1Impl.getById(test1Vo.getId()));

        Collection<List<Object>> values = Lists.newArrayList();
        values.add(Lists.<Object> newArrayList("4", "1.11"));
        values.add(Lists.<Object> newArrayList("5", "1.11"));

        List<String> propers = Lists.newArrayList("id", "num");

        List<TestVo> res = daoImpl.tupleIn(propers, values);
        System.out.println(res);

    }

    @Test
    public void del() {
        Map<String, Object> condition = Maps.newConcurrentMap();
        condition.put("id", Lists.newArrayList(1, 2));

        daoImpl.delByCondition(condition);

    }
}
