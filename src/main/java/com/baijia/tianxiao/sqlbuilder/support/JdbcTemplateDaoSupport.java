/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.support;

import com.baijia.tianxiao.sqlbuilder.SingleSqlBuilder;
import com.baijia.tianxiao.sqlbuilder.bean.Order;
import com.baijia.tianxiao.sqlbuilder.bean.SaveInfo;
import com.baijia.tianxiao.sqlbuilder.dto.BuildSqlConditionResult;
import com.baijia.tianxiao.sqlbuilder.dto.PageDto;
import com.baijia.tianxiao.sqlbuilder.exception.NoColumnFoundException;
import com.baijia.tianxiao.sqlbuilder.exception.NonUniqueResultException;
import com.baijia.tianxiao.sqlbuilder.schema.ColumnDefine;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import com.baijia.tianxiao.util.query.BatchQueryCallback;
import com.baijia.tianxiao.util.query.ListBatchQueryTemplate;
import com.baijia.tianxiao.util.query.MapBatchQueryTemplate;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author cxm
 * @version 1.0
 * @title JdbcTemplateDaoSupport
 * @desc jdbc template 查询支持
 * @date 2015年12月2日
 */
@Slf4j
public class JdbcTemplateDaoSupport<T> extends JdbcTemplateFillSupport<T> implements CommonDao<T> {

    // private static final String GENERATED_KEY = "GENERATED_KEY";

    private static final String isDelColumn = "isDel";
    private PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
    @Getter
    private ColumnDefine idColumn;

    @SuppressWarnings("unchecked")
    public JdbcTemplateDaoSupport() {
        ParameterizedType superType = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) superType.getActualTypeArguments()[0];
        init();
    }

    public JdbcTemplateDaoSupport(Class<T> entityClass) {
        this.entityClass = entityClass;
        init();
    }

    private void init() {
        SingleSqlBuilder<T> builder = SingleSqlBuilder.create(this.entityClass);
        this.idColumn = builder.getIdColumn();
    }

    protected SingleSqlBuilder<T> createSqlBuilder(String... queryProps) {
        return SingleSqlBuilder.create(this.entityClass, queryProps);
    }

    @Override
    public <PK extends Serializable> T getById(PK id, String... queryProps) {
        SingleSqlBuilder<T> builder = createSqlBuilder(queryProps);
        builder.eq(builder.getIdColumn().getFieldName(), id);
        return uniqueResult(builder);
    }

    @Override
    public <PK extends Serializable> List<T> getByIds(Collection<PK> ids, final String... queryProps) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        ListBatchQueryTemplate<PK, T> query = new ListBatchQueryTemplate<>();
        return query.batchQuery(ids, new BatchQueryCallback<PK, List<T>>() {
            @Override
            public List<T> doQuery(Collection<PK> querySet) {
                SingleSqlBuilder<T> builder = createSqlBuilder(queryProps);
                builder.in(builder.getIdColumn().getFieldName(), querySet);
                return queryList(builder);
            }
        });
    }

    @Override
    public <PK extends Serializable> List<T> getByIdsAndOrderByParam(Collection<PK> ids, final String orderByProp,
                                                                     final String... queryProps) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        ListBatchQueryTemplate<PK, T> query = new ListBatchQueryTemplate<>();
        return query.batchQuery(ids, new BatchQueryCallback<PK, List<T>>() {
            @Override
            public List<T> doQuery(Collection<PK> querySet) {
                SingleSqlBuilder<T> builder = createSqlBuilder(queryProps);
                builder.in(builder.getIdColumn().getFieldName(), querySet);

                builder.desc(orderByProp);
                return queryList(builder);
            }
        });
    }

    @Override
    public List<T> getAll(String... queryProps) {
        return this.getAll(null, queryProps);
    }

    @Override
    public List<T> getByPage(PageDto page, String... queryProps) {
        SingleSqlBuilder<T> builder = createSqlBuilder(queryProps);
        builder.setPage(page);
        return queryList(builder);
    }

    @Override
    public <PK extends Serializable> int delById(PK id) {
        SingleSqlBuilder<T> builder = createSqlBuilder();
        builder.eq(builder.getIdColumn().getFieldName(), id);
        return this.delete(builder);
    }

    public int delete(SingleSqlBuilder<T> builder) {
        String deleteSql = builder.toDeleteSqlByCondition();
        Map<String, Object> params = builder.collectConditionValue();
        log.debug("delete sql:{}, params:{}", deleteSql, params);
        return getNamedJdbcTemplate().update(builder.toDeleteSqlByCondition(), builder.collectConditionValue());
    }

    @Override
    public <PK extends Serializable> int delByIds(Collection<PK> pks) {
        SingleSqlBuilder<T> builder = createSqlBuilder();
        Map<String, Object> condition = Maps.newHashMap();
        condition.put(builder.getIdColumn().getFieldName(), pks);
        return this.delByCondition(condition);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int delByCondition(Map<String, Object> condition) {
        if (MapUtils.isNotEmpty(condition)) {
            SingleSqlBuilder<T> builder = createSqlBuilder();
            for (String key : condition.keySet()) {
                Object value = condition.get(key);
                if (value instanceof Collection) {
                    builder.in(key, (Collection<? extends Serializable>) value);
                } else if (value instanceof Serializable) {
                    builder.eq(key, (Serializable) value);
                } else {
                    throw new IllegalArgumentException("can not reconize value:" + value);
                }
            }
            return this.delete(builder);
        } else {
            log.warn("can not delete with no conditon");
            return -1;
        }

    }

    @Override
    public void save(T obj, String... saveProp) {
        this.save(obj, null, saveProp);
    }

    @Override
    public void saveAll(final List<T> objs, boolean needPk, String... saveProp) {
        if (needPk) {
            saveAll(objs, saveProp);
        } else {
            final SingleSqlBuilder<T> builder = createSqlBuilder();
            Map<String, Object> params = ColumnUtil.collectBatchInsertValue(objs, false);
            final String sql = builder.toBatchInsertSql(objs.size(), saveProp);
            log.debug("saveAll sql::{},params:{}", sql, params);
            getNamedJdbcTemplate().update(sql, params);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveAll(final List<T> objs, String... saveProp) {
        if (CollectionUtils.isEmpty(objs)) {
            log.warn("save result is empty.");
            return;
        }
        for (T obj : objs) {
            this.save(obj, saveProp);
        }

        // final SingleSqlBuilder<T> builder = createSqlBuilder();
        // Map<String, Object> params = ColumnUtil.collectBatchInsertValue(objs);
        // final String sql = builder.toBatchInsertSql(objs.size(), saveProp);
        //
        // SqlParameterSource paramSource = new MapSqlParameterSource(params);
        // ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
        // String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        // final Object[] valueArr = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
        // List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
        // final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse,
        // declaredParameters);
        // final PreparedStatementCreator psCreator = pscf.newPreparedStatementCreator(valueArr);
        //
        // log.debug("batch insert sql:{},params:{}", sqlToUse, valueArr);
        // getNamedJdbcTemplate().getJdbcOperations().execute(new ConnectionCallback<Integer>() {
        // @Override
        // public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
        // PreparedStatement ps = psCreator.createPreparedStatement(con);
        // int rows = 0;
        // try {
        // rows = ps.executeUpdate();
        //
        // PreparedStatement lastIdPs = con.prepareStatement("select last_insert_id()");
        // ResultSet rs = lastIdPs.executeQuery();
        // try {
        // if (rs.next()) {
        // Long id = rs.getLong(1);
        // for (T obj : objs) {
        // Field field = obj.getClass().getDeclaredField(builder.getIdColumn().getFieldName());
        // field.setAccessible(true);
        // Class<?> type = field.getType();
        // if (type.equals(Integer.TYPE) || type.getSimpleName().equals("Integer")) {
        // field.set(obj, id.intValue());
        // } else {
        // field.set(obj, id);
        // }
        // id++;
        // }
        // }
        // } catch (Exception e) {
        // log.error("set value catch error:", e);
        // } finally {
        // JdbcUtils.closeResultSet(rs);
        // JdbcUtils.closeStatement(lastIdPs);
        // }
        // } catch (Exception e) {
        // log.error("set value catch error:", e);
        // } finally {
        // JdbcUtils.closeStatement(ps);
        // }
        // return rows;
        // }
        // });

        // KeyHolder keyHolder = new GeneratedKeyHolder();
        // keyHolder.getKeyList().clear();
        // getNamedJdbcTemplate().update(sql, new MapSqlParameterSource(params), keyHolder);
        // TODO 批量插入在线上返回的ID不正确.线上奇葩数据库,只能如上自己写
        // if (CollectionUtils.isNotEmpty(keyHolder.getKeyList()) && objs.size() == keyHolder.getKeyList().size()) {
        // log.debug("batch insert return keys:{}", keyHolder.getKeyList());
        // int i = 0;
        //
        // for (T obj : objs) {
        // try {
        // Number id = (Number) keyHolder.getKeyList().get(i).get(GENERATED_KEY);
        // BeanUtils.setProperty(obj, builder.getIdColumn().getFieldName(), id);
        // } catch (InvalidDataAccessApiUsageException | IllegalAccessException | InvocationTargetException e) {
        // e.printStackTrace();
        // }
        // i++;
        // }
        // }
    }

    @Override
    public void save(T obj, Boolean saveNullValue, String... saveProp) {
        this.save(obj, saveNullValue, false, saveProp);
    }

    @Override
    public void saveWithDefaultVal(T obj, String... saveProp) {
        this.save(obj, false, true, saveProp);
    }

    @Override
    public void save(T obj, Boolean saveNullValue, boolean useDefaultVal, String... saveProp) {
        SingleSqlBuilder<T> builder = createSqlBuilder();
        builder.setSetDefaultVal(useDefaultVal);
        boolean saveNull = saveNullValue != null ? saveNullValue : !builder.getTableDefine().isDynamicInsert();
        SaveInfo saveInfo = ColumnUtil.getSaveInfoFromObjs(obj, saveNull, builder.isSetDefaultVal());
        if (ArrayUtils.isEmpty(saveProp)) {
            saveProp = saveInfo.getColumns().toArray(new String[]{});
        }
        String insertSql = builder.toInsertSql(saveProp);
        log.debug("insert sql:{},params:{}", insertSql, saveInfo.getParamMap());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedJdbcTemplate().update(insertSql, new MapSqlParameterSource(saveInfo.getParamMap()), keyHolder);
        try {
            log.debug("save obj:{},return id:{}", obj, keyHolder.getKey());
            BeanUtils.setProperty(obj, builder.getIdColumn().getFieldName(), keyHolder.getKey());
        } catch (InvalidDataAccessApiUsageException | IllegalAccessException | InvocationTargetException e) {
            log.error("set id column catch error:", e);
        }
    }

    @Override
    public int update(T obj, String... updateProp) {
        return this.update(obj, null, updateProp);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int update(Map<String, Object> updateCondtion, String... updateProps) {
        long current = System.currentTimeMillis();
        SingleSqlBuilder<T> builder = createSqlBuilder();
        Preconditions.checkArgument(MapUtils.isNotEmpty(updateCondtion), "update condition is empty");
        Preconditions.checkArgument(ArrayUtils.isNotEmpty(updateProps), "update properties is empty");

        Map<String, Object> paramMap = new HashMap<>();
        Set<String> propSet = Sets.newHashSet(updateProps);
        for (String key : updateCondtion.keySet()) {
            Object value = updateCondtion.get(key);
            if (propSet.contains(key)) {
                if (value instanceof Collection || value instanceof Object[]) {
                    throw new IllegalArgumentException("update value:" + value + " is illegal");
                }
                paramMap.put(key, value);
                propSet.remove(key);
                continue;
            }
            if (value instanceof Collection) {
                builder.in(key, (Collection<? extends Serializable>) value);
            } else if (value instanceof Serializable) {
                builder.eq(key, (Serializable) value);
            } else {
                throw new IllegalArgumentException("can not reconize value:" + value);
            }
        }
        if (!propSet.isEmpty()) {
            throw new IllegalArgumentException("no value for update key:" + propSet);
        }
        String updateSql = builder.toUpdateSql(updateProps);
        paramMap.putAll(builder.collectConditionValue());
        log.debug("update sql:{}, params:{},cost:{}ms", updateSql, paramMap, System.currentTimeMillis() - current);
        return getNamedJdbcTemplate().update(updateSql, paramMap);
    }

    @Override
    public int update(Map<String, Object> updateCondtion, Boolean updateNull, T obj, String... updateProp) {
        return this.update(updateCondtion, updateNull, false, obj, updateProp);
    }

    @Override
    public int updateWithDefaultVal(T obj, String... updateProp) {
        return this.update(null, false, true, obj, updateProp);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int update(Map<String, Object> updateCondtion, Boolean updateNull, boolean useDefaultVal, T obj,
                      String... updateProp) {
        long current = System.currentTimeMillis();
        SingleSqlBuilder<T> builder = createSqlBuilder();
        builder.setSetDefaultVal(useDefaultVal);
        if (MapUtils.isNotEmpty(updateCondtion)) {
            for (String key : updateCondtion.keySet()) {
                Object value = updateCondtion.get(key);
                if (value instanceof Collection) {
                    builder.in(key, (Collection<? extends Serializable>) value);
                } else if (value instanceof Serializable) {
                    builder.eq(key, (Serializable) value);
                } else {
                    throw new IllegalArgumentException("can not reconize value:" + value);
                }
            }
        } else {
            try {
                builder.eq(builder.getIdColumn().getFieldName(),
                        (Serializable) (propertyUtilsBean.getProperty(obj, builder.getIdColumn().getFieldName())));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new UnsupportedOperationException("can not update by empty id value obj:" + obj);
            }
        }
        boolean canUpdateNull = updateNull != null ? updateNull : !builder.getTableDefine().isDynamicUpdate();
        SaveInfo saveInfo = ColumnUtil.getSaveInfoFromObjs(obj, canUpdateNull, builder.isSetDefaultVal());
        if (ArrayUtils.isEmpty(updateProp)) {
            updateProp = saveInfo.getColumns().toArray(new String[]{});
        }
        saveInfo.getParamMap().putAll(builder.collectConditionValue());
        String updateSql = builder.toUpdateSql(updateProp);
        log.debug("update sql:{}, params:{},cost:{}ms", updateSql, saveInfo.getParamMap(),
                System.currentTimeMillis() - current);
        return getNamedJdbcTemplate().update(updateSql, saveInfo.getParamMap());
    }

    @Override
    public int update(Map<String, Object> updateCondtion, T obj, String... updateProp) {
        return this.update(updateCondtion, null, obj, updateProp);
    }

    @Override
    public int update(T obj, Boolean updateNullValue, String... updateProp) {
        return this.update(null, updateNullValue, obj, updateProp);
    }

    @Override
    public void saveOrUpdate(T obj, String... updateProp) {
        Object identify = null;
        try {
            identify = propertyUtilsBean.getProperty(obj, this.idColumn.getFieldName());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.warn("get identify value from obj cat error:", e);
        }
        if (identify != null) {
            update(obj, updateProp);
        } else {
            save(obj, updateProp);
        }
    }

    @Override
    public void saveOrUpdateWithDefaultVal(T obj, String... updateProp) {
        Object identify = null;
        try {
            identify = propertyUtilsBean.getProperty(obj, this.idColumn.getFieldName());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.warn("get identify value from obj cat error:", e);
        }
        if (identify != null) {
            updateWithDefaultVal(obj, updateProp);
        } else {
            saveWithDefaultVal(obj, updateProp);
        }
    }

    public <R> List<R> queryForList(@NonNull final SingleSqlBuilder<T> builder, final Class<R> clazzR) {
        return buildResult(builder, new Function<SingleSqlBuilder<T>, List<R>>() {
            @Override
            public List<R> apply(SingleSqlBuilder<T> arg0) {
                return getNamedJdbcTemplate().queryForList(builder.toSql(), builder.collectConditionValue(), clazzR);
            }
        });

    }

    protected <R> List<R> buildResult(@NonNull SingleSqlBuilder<T> builder,
                                      Function<SingleSqlBuilder<T>, List<R>> function) {
        long current = System.currentTimeMillis();
        if (fillPageParam(builder, 0)) {
            List<R> result = function.apply(builder);
            printLog(builder, current, result.size());
            if (builder.getPage() != null) {
                builder.getPage().setCurPageCount(result.size());
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public <R> List<R> queryList(@NonNull SingleSqlBuilder<T> builder, Class<R> clazzR) {
        return queryList(builder, clazzR, false);
    }

    public <R> List<R> queryList(@NonNull final SingleSqlBuilder<T> builder, final Class<R> clazzR,
                                 final boolean forUpdate) {
        return buildResult(builder, new Function<SingleSqlBuilder<T>, List<R>>() {
            @Override
            public List<R> apply(SingleSqlBuilder<T> arg0) {
                return getNamedJdbcTemplate().query(builder.toSql(forUpdate), builder.collectConditionValue(),
                        new BeanPropertyRowMapper<R>(clazzR));
            }
        });
    }

    public List<T> queryList(@NonNull SingleSqlBuilder<T> builder) {
        return queryList(builder, false);
    }

    public List<T> queryList(@NonNull final SingleSqlBuilder<T> builder, final boolean forUpdate) {
        return buildResult(builder, new Function<SingleSqlBuilder<T>, List<T>>() {
            @Override
            public List<T> apply(SingleSqlBuilder<T> arg0) {
                return getNamedJdbcTemplate().query(builder.toSql(forUpdate), builder.collectConditionValue(),
                        new BeanPropertyRowMapper<T>(entityClass));
            }
        });
    }

    public List<Map<String, Object>> query2ListMap(@NonNull final SingleSqlBuilder<T> builder) {
        return buildResult(builder, new Function<SingleSqlBuilder<T>, List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> apply(SingleSqlBuilder<T> arg0) {
                return getNamedJdbcTemplate().queryForList(builder.toSql(), builder.collectConditionValue());
            }
        });
    }

    public <R> R queryForObject(@NonNull SingleSqlBuilder<T> builder, Class<R> clazzR) {
        long current = System.currentTimeMillis();
        List<R> results = getNamedJdbcTemplate().queryForList(builder.toSql(), builder.collectConditionValue(), clazzR);
        printLog(builder, current, results.size());
        if (CollectionUtils.isEmpty(results)) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            throw new NonUniqueResultException(builder.toSql());
        }
    }

    public T uniqueResult(@NonNull final SingleSqlBuilder<T> builder) {
        return uniqueResult(builder, false);
    }

    public T uniqueResult(@NonNull final SingleSqlBuilder<T> builder, boolean forUpdate) {
        List<T> results = queryList(builder, forUpdate);
        if (CollectionUtils.isEmpty(results)) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            throw new NonUniqueResultException(builder.toSql());
        }
    }

    public <R> R uniqueResult(@NonNull final SingleSqlBuilder<T> builder, Class<R> clazz) {
        List<R> results = queryList(builder, clazz);
        if (CollectionUtils.isEmpty(results)) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            throw new NonUniqueResultException(builder.toSql());
        }
    }

    protected void printLog(SingleSqlBuilder<T> builder, Long current, Object o) {
        log.debug("sql:{},map value:{},result:{},cost:{}ms", builder.toSql(), builder.collectConditionValue(), o,
                System.currentTimeMillis() - current);
    }

    private boolean fillPageParam(final SingleSqlBuilder<T> sqlBuilder, int currentSize) {
        if (sqlBuilder.getPage() != null) {
            Number number = getNamedJdbcTemplate().queryForObject(sqlBuilder.toCountSql(),
                    sqlBuilder.collectConditionValue(), Long.class);
            sqlBuilder.getPage().setCount((number != null ? number.intValue() : 0));
            sqlBuilder.getPage().setCurPageCount(currentSize);
            return sqlBuilder.getPage().getCount() > 0;
        }
        return true;
    }

    @Override
    public <PK extends Serializable> int updateColumnValueById(PK id, String column, Object value) {
        if (column == null) {
            column = isDelColumn;
        }
        Preconditions.checkArgument(hasDelstatusColumn(column), "has no field named ｛｝!", column);
        Map<String, Object> updateCondtion = Maps.newHashMap();
        updateCondtion.put("id", id);
        updateCondtion.put(column, value);
        return this.update(updateCondtion, column);
    }

    @Override
    public <PK extends Serializable> int updateColumnValueByIds(Collection<PK> ids, String column, Object value) {
        if (column == null) {
            column = isDelColumn;
        }
        Preconditions.checkArgument(hasDelstatusColumn(column), "has no field named ｛｝!", column);
        Map<String, Object> updateCondtion = Maps.newHashMap();
        updateCondtion.put("id", ids);
        updateCondtion.put(column, value);
        return this.update(updateCondtion, column);
    }

    /**
     * 是否有对应列
     *
     * @return
     */

    private boolean hasDelstatusColumn(String fieldName) {
        try {
            if (this.entityClass.getDeclaredField(fieldName) != null) {
                return true;
            }
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e) {
        }
        return false;
    }

    @Override
    public <PK extends Serializable, V> Map<PK, V> queryPkValueMap(Collection<PK> ids, final String valField) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return new MapBatchQueryTemplate<PK, PK, V>().batchQuery(ids, new BatchQueryCallback<PK, Map<PK, V>>() {
            @Override
            public Map<PK, V> doQuery(Collection<PK> querySet) {
                SingleSqlBuilder<T> builder = createSqlBuilder(idColumn.getFieldName(), valField);
                builder.in(idColumn.getFieldName(), querySet);
                List<T> list = queryList(builder);
                Map<PK, V> result = Maps.newHashMap();
                if (!list.isEmpty()) {
                    for (T t : list) {
                        try {
                            @SuppressWarnings("unchecked")
                            PK id = (PK) propertyUtilsBean.getProperty(t, idColumn.getFieldName());
                            @SuppressWarnings("unchecked")
                            V val = (V) propertyUtilsBean.getProperty(t, valField);
                            result.put(id, val);
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            log.warn("get identify value from obj cat error:", e);
                        }
                    }
                }
                return result;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public int countByCondition(Map<String, Object> countCondtion, String countField, boolean distinct) {
        SingleSqlBuilder<T> builder = createSqlBuilder();
        if (StringUtils.isBlank(countField)) {
            countField = this.idColumn.getFieldName();
        }
        if (distinct) {
            builder.distinctCount(countField);
        } else {
            builder.count(countField);
        }
        for (String key : countCondtion.keySet()) {
            try {
                ColumnUtil.getColumnName(key, builder.getContext());
            } catch (NoColumnFoundException e) {
                continue;
            }
            Object value = countCondtion.get(key);

            if (value instanceof Collection) {
                builder.in(key, (Collection<? extends Serializable>) value);
            } else if (value instanceof Serializable) {
                builder.eq(key, (Serializable) value);
            } else {
                throw new IllegalArgumentException("can not reconize value:" + value);
            }
        }
        return queryForObject(builder, Integer.class);
    }

    @SuppressWarnings("unchecked")
    protected void fillBuilderByCondition(Map<String, Object> condition, SingleSqlBuilder<T> builder, PageDto page) {
        builder.setPage(page);
        if (MapUtils.isNotEmpty(condition)) {
            for (String key : condition.keySet()) {
                try {
                    ColumnUtil.getColumnName(key, builder.getContext());
                } catch (NoColumnFoundException e) {
                    continue;
                }
                Object value = condition.get(key);

                if (value instanceof Collection) {
                    builder.in(key, (Collection<? extends Serializable>) value);
                } else if (value instanceof Serializable) {
                    builder.eq(key, (Serializable) value);
                } else {
                    throw new IllegalArgumentException("can not reconize value:" + value);
                }
            }
        }
    }

    @Override
    public <K extends Serializable> Map<K, Integer> groupCount(Map<String, Object> countCondition, String countField,
                                                               final String groupField, boolean distinct, final Class<K> keyClass) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(groupField), "count field is illegal");
        long current = System.currentTimeMillis();
        SingleSqlBuilder<T> builder = createSqlBuilder();
        if (StringUtils.isBlank(countField)) {
            countField = this.idColumn.getFieldName();
        }
        if (distinct) {
            builder.distinctCount(countField, "cnt");
        } else {
            builder.count(countField, "cnt");
        }
        builder.select(groupField);
        fillBuilderByCondition(countCondition, builder, null);
        builder.group(groupField);
        final Map<K, Integer> result = Maps.newHashMap();
        this.getNamedJdbcTemplate().query(builder.toSql(), builder.collectConditionValue(), new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                result.put(rs.getObject(groupField, keyClass), rs.getInt("cnt"));
            }
        });
        printLog(builder, current, result);
        return result;
    }

    @Override
    public List<T> queryByCondition(Map<String, Object> condition, PageDto page, String... queryProps) {
        SingleSqlBuilder<T> builder = createSqlBuilder(queryProps);
        fillBuilderByCondition(condition, builder, page);
        return queryList(builder);
    }

    @Override
    public <R> List<R> queryListBySqlCondition(String select, String countSelect, BuildSqlConditionResult sqlCondition,
                                               Class<R> clazz) {
        String countSql = null;
        PageDto page = sqlCondition.getPage();
        if (page != null) {
            countSql = sqlCondition.toSql(countSelect);
        }
        return queryListBySqlConditionAndCountSql(countSelect, countSql, sqlCondition, clazz);
    }

    @Override
    public <R> R queryObjectBySqlCondition(String select, BuildSqlConditionResult sqlCondition, Class<R> clazz) {
        List<R> list = queryListBySqlCondition(select, null, sqlCondition, clazz);
        if (list == null) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new NonUniqueResultException("non unique result");
        }
    }

    @Override
    public <R> List<R> queryListBySqlConditionAndCountSql(String select, String countSql,
                                                          BuildSqlConditionResult sqlCondition, Class<R> clazz) {
        String querySql = sqlCondition.toSql(select);
        PageDto page = sqlCondition.getPage();
        if (page != null) {
            Preconditions.checkArgument(StringUtils.isNoneBlank(countSql), "count sql is empty");
            log.debug("query  count sql:{},params:{}", countSql, sqlCondition.getQueryParams());
            int count =
                    this.getNamedJdbcTemplate().queryForObject(countSql, sqlCondition.getQueryParams(), Integer.class);
            page.setCount(count);
            if (count == 0) {
                return Collections.emptyList();
            }
        }
        sqlCondition.setPage(page);
        log.debug("query  sql:{},params:{}", querySql, sqlCondition.getQueryParams());
        List<R> results = this.getNamedJdbcTemplate().query(querySql, sqlCondition.getQueryParams(),
                new BeanPropertyRowMapper<R>(clazz));
        return results;
    }

    @Override
    public List<T> queryByCondition(Map<String, Object> condition, Order order, PageDto page, String... queryProps) {
        SingleSqlBuilder<T> builder = createSqlBuilder(queryProps);
        fillBuilderByCondition(condition, builder, page);
        if (order != null) {
            builder.setOrder(order);
        }
        return queryList(builder);
    }

    @Override
    public List<T> queryReport(Map<String, Object> queryCondition, String[] queryProps, Order order,
                               String... measures) {
        Preconditions.checkArgument(ArrayUtils.isNotEmpty(queryProps), "查询的维度为空");
        Preconditions.checkArgument(ArrayUtils.isNotEmpty(measures), "查询的指标为空");
        SingleSqlBuilder<T> builder = createSqlBuilder();
        for (String measure : measures) {
            builder.sum(measure, measure);
        }
        if (MapUtils.isNotEmpty(queryCondition)) {
            this.fillBuilderByCondition(queryCondition, builder, null);
        }
        if (order != null) {
            builder.setOrder(order);
        }
        builder.groupByNames(queryProps);
        return queryList(builder);
    }

    @Override
    public List<T> getAll(Order order, String... props) {
        SingleSqlBuilder<T> builder = createSqlBuilder();
        if (order != null) {
            builder.setOrder(order);
        }
        return queryList(builder);
    }

}
