/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.support;

import com.baijia.tianxiao.sqlbuilder.bean.Order;
import com.baijia.tianxiao.sqlbuilder.dto.BuildSqlConditionResult;
import com.baijia.tianxiao.sqlbuilder.dto.PageDto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author cxm
 * @version 1.0
 * @title JdbcTemplateCommonDao
 * @desc TODO
 * @date 2015年12月2日
 */
public interface CommonDao<T> {

    /**
     * 根据主键查询对象
     *
     * @param id
     * @param queryProps
     * @return
     */
    <PK extends Serializable> T getById(PK id, String... queryProps);

    /**
     * 根据主键列表获取记录列表
     *
     * @param ids
     * @param queryProps
     * @return
     */
    <PK extends Serializable> List<T> getByIds(Collection<PK> ids, String... queryProps);

    /**
     * 根据主键列表获取记录列表
     *
     * @param ids
     * @param queryProps
     * @return
     */
    <PK extends Serializable> List<T> getByIdsAndOrderByParam(Collection<PK> ids, String orderByProp,
                                                              String... queryProps);

    /**
     * 载入表中的全部数据
     *
     * @param props
     * @return
     */
    List<T> getAll(String... props);

    /**
     * 载入表中的全部数据
     *
     * @param props
     * @return
     */
    List<T> getAll(Order order, String... props);

    /**
     * 根据主键删除对象
     *
     * @param obj
     */
    <PK extends Serializable> int delById(PK id);

    <PK extends Serializable> int delByIds(Collection<PK> ids);

    /**
     * 根据条件删除
     *
     * @param condition
     */
    int delByCondition(Map<String, Object> condition);

    /**
     * 保存对象到数据库,支持entity上配置的动态插入,即不插入NULL值
     *
     * @param obj
     * @param saveProp
     */
    void save(T obj, String... saveProp);

    /**
     * @param obj
     * @param saveNullValue
     * @param saveProp
     */
    void save(T obj, Boolean saveNullValue, String... saveProp);

    /**
     * 批量保存对象,不支持动态插入,推荐指定字段进行保存,不指定全部保存
     *
     * @param objs
     * @param saveProp
     */
    void saveAll(List<T> objs, String... saveProp);

    /**
     * 根据注解ID更新对象
     *
     * @param obj
     * @param updateProp
     */
    int update(T obj, String... updateProp);

    /**
     * @param updateCondtion
     * @param obj
     * @param updateProp
     */
    int update(Map<String, Object> updateCondtion, T obj, String... updateProp);

    /**
     * @param obj
     * @param updateNullValue
     * @param updateProp
     */
    int update(T obj, Boolean updateNullValue, String... updateProp);

    /**
     * @param updateCondtion
     * @param updateNull
     * @param obj
     * @param updateProp
     */
    int update(Map<String, Object> updateCondtion, Boolean updateNull, T obj, String... updateProp);

    /**
     * @param obj
     * @param updateProp
     */
    void saveOrUpdate(T obj, String... updateProp);

    /**
     * @param updateCondtion
     * @param updateProps
     * @return
     */
    int update(Map<String, Object> updateCondtion, String... updateProps);

    /**
     * 批量保存,如果需要主键的话,由于drds的问题,走的是单个保存
     *
     * @param objs
     * @param needPk
     * @param saveProp
     */
    void saveAll(List<T> objs, boolean needPk, String... saveProp);

    /**
     * 更新对应主键id的某一列的值
     *
     * @param id
     * @param column
     * @param value
     */
    <PK extends Serializable> int updateColumnValueById(PK id, String column, Object value);

    /**
     * 更新对应主键ids的某一列的值
     *
     * @param id
     * @param column
     * @param value
     */
    <PK extends Serializable> int updateColumnValueByIds(Collection<PK> ids, String column, Object value);

    /**
     * 根据字段查找出映射,适用于查找ID和某一字段的映射关系
     *
     * @param ids
     * @param valColumn
     * @return
     */
    <PK extends Serializable, V> Map<PK, V> queryPkValueMap(Collection<PK> ids, String valColumn);

    /**
     * 根据条件算count值
     *
     * @param countCondition
     * @param countField
     * @param distinct
     * @return
     */
    int countByCondition(Map<String, Object> countCondition, String countField, boolean distinct);

    /**
     * group count 字段,现在只支持单字段group
     *
     * @param countCondition
     * @param countField
     * @param groupField
     * @param distinct
     * @param keyClass
     * @return
     */
    <K extends Serializable> Map<K, Integer> groupCount(Map<String, Object> countCondition, String countField,
                                                        String groupField, boolean distinct, Class<K> keyClass);

    /**
     * 通用查询,conditon中的key为PO的属性或者表的字段
     *
     * @param condition
     * @param page       分页信息
     * @param queryProps 返回的结果集的字段列表
     * @return
     */
    List<T> queryByCondition(Map<String, Object> condition, PageDto page, String... queryProps);

    List<T> queryByCondition(Map<String, Object> condition, Order order, PageDto page, String... queryProps);

    /**
     * 如果有group by,请手动拼接sql,并使用方法 queryListBySqlConditionAndCountSql
     *
     * @param select
     * @param count
     * @param sqlCondition
     * @param clazz
     * @return
     */
    <R> List<R> queryListBySqlCondition(String select, String countSelect, BuildSqlConditionResult sqlCondition,
                                        Class<R> clazz);

    /**
     * 动态指定count的sql
     *
     * @param select
     * @param countSql
     * @param sqlCondition
     * @param clazz
     * @return
     */
    <R> List<R> queryListBySqlConditionAndCountSql(String select, String countSql, BuildSqlConditionResult sqlCondition,
                                                   Class<R> clazz);

    <R> R queryObjectBySqlCondition(String select, BuildSqlConditionResult sqlCondition, Class<R> clazz);

    /**
     * @param page
     * @param queryProps
     * @return
     */
    List<T> getByPage(PageDto page, String... queryProps);

    /**
     * @param obj
     * @param saveNullValue
     * @param useDefaultVal
     * @param saveProp
     */
    void save(T obj, Boolean saveNullValue, boolean useDefaultVal, String... saveProp);

    /**
     * @param obj
     * @param saveProp
     */
    void saveWithDefaultVal(T obj, String... saveProp);

    /**
     * @param obj
     * @param updateProp
     * @return
     */
    int updateWithDefaultVal(T obj, String... updateProp);

    /**
     * @param updateCondtion
     * @param updateNull
     * @param useDefaultVal
     * @param obj
     * @param updateProp
     * @return
     */
    int update(Map<String, Object> updateCondtion, Boolean updateNull, boolean useDefaultVal, T obj,
               String... updateProp);

    /**
     * @param queryCondition
     * @param queryProps
     * @param measures
     * @return
     */
    List<T> queryReport(Map<String, Object> queryCondition, String[] queryProps, Order order, String... measures);

    /**
     * @param obj
     * @param updateProp
     */
    void saveOrUpdateWithDefaultVal(T obj, String... updateProp);

}
