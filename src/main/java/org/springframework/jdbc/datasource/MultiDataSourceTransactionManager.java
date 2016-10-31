/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2016 All Rights Reserved.
 */
package org.springframework.jdbc.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cxm
 * @version 1.0
 * @title 自定义的本地多数据源事务管理, package不能乱改
 * @desc TODO
 * @date 2016年1月23日
 */
@Slf4j
public class MultiDataSourceTransactionManager extends AbstractPlatformTransactionManager
        implements ResourceTransactionManager, InitializingBean, ApplicationContextAware {

    private static final long serialVersionUID = -5647129903917537197L;
    private static Map<DataSource, DataSourceTransactionManager> dataSourceTransactionManagerMap;

    // private Map<String, DataSource> dataSourceMap;
    private ApplicationContext context;

    /**
     * Create a new DataSourceTransactionManager instance. A DataSource has to be set to be able to use it.
     *
     * @see #setDataSource
     */
    public MultiDataSourceTransactionManager() {
        setNestedTransactionAllowed(true);
    }

    public Object getResourceFactory() {
        return new MultiDataSourceTransactionManager();
    }

    @Override
    protected Object doGetTransaction() {
        MultiDataSourceTransactionObject txObject = new MultiDataSourceTransactionObject();
        for (Map.Entry<DataSource, DataSourceTransactionManager> entry : dataSourceTransactionManagerMap.entrySet()) {
            txObject.getDsTransactionObjectMap().put(entry.getKey(), entry.getValue().doGetTransaction());
            if (!txObject.hasBegin) {
                txObject.setHasBegin(TransactionSynchronizationManager.hasResource(entry.getKey()));
            }
        }
        return txObject;
    }

    @Override
    protected boolean isExistingTransaction(Object transaction) {
        MultiDataSourceTransactionObject txObject = (MultiDataSourceTransactionObject) transaction;
        return txObject.isHasBegin();
    }

    /**
     * This implementation sets the isolation level but ignores the timeout.
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        // log.info("do begin:{}, definition:{},thread resource:{}", transaction, definition,
        // TransactionSynchronizationManager.getResourceMap());
        MultiDataSourceTransactionObject txObject = (MultiDataSourceTransactionObject) transaction;
        if (definition.isReadOnly()) {
            txObject.setReadOnly(true);
            return;
        }
        for (Map.Entry<DataSource, Object> entry : txObject.getDsTransactionObjectMap().entrySet()) {
            dataSourceTransactionManagerMap.get(entry.getKey()).doBegin(entry.getValue(), definition);
        }
        txObject.setHasBegin(true);
    }

    @Override
    protected Object doSuspend(Object transaction) {
        MultiDataSourceTransactionObject txObject = (MultiDataSourceTransactionObject) transaction;
        Map<DataSource, Object> connMap = new HashMap<>();
        log.info("do suspend:{}", transaction);
        if (txObject.isReadOnly()) {
            return connMap;
        }
        for (Map.Entry<DataSource, Object> entry : txObject.getDsTransactionObjectMap().entrySet()) {
            Object con = dataSourceTransactionManagerMap.get(entry.getKey()).doSuspend(entry.getValue());
            connMap.put(entry.getKey(), con);
        }
        txObject.setHasBegin(false);
        return connMap;
    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        // log.info("do resume:{}", suspendedResources);
        @SuppressWarnings("unchecked")
        Map<DataSource, Object> connMap = (Map<DataSource, Object>) suspendedResources;
        for (Map.Entry<DataSource, Object> entry : connMap.entrySet()) {
            dataSourceTransactionManagerMap.get(entry.getKey()).doResume(transaction, entry.getValue());
        }
        MultiDataSourceTransactionObject txObject = (MultiDataSourceTransactionObject) transaction;
        txObject.setHasBegin(true);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        MultiDataSourceTransactionObject txObject = (MultiDataSourceTransactionObject) status.getTransaction();
        // log.info("do commit:{}", status);
        if (status.isReadOnly()) {
            return;
        }
        for (Map.Entry<DataSource, Object> entry : txObject.getDsTransactionObjectMap().entrySet()) {
            dataSourceTransactionManagerMap.get(entry.getKey())
                    .doCommit(new DefaultTransactionStatus(entry.getValue(), status.isNewTransaction(),
                            status.isNewSynchronization(), status.isReadOnly(), status.isDebug(),
                            status.getSuspendedResources()));
        }
        txObject.setHasBegin(true);

    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        MultiDataSourceTransactionObject txObject = (MultiDataSourceTransactionObject) status.getTransaction();
        // log.info("do rollback:{}", status);
        if (status.isReadOnly()) {
            return;
        }
        for (Map.Entry<DataSource, Object> entry : txObject.getDsTransactionObjectMap().entrySet()) {
            dataSourceTransactionManagerMap.get(entry.getKey())
                    .doRollback(new DefaultTransactionStatus(entry.getValue(), status.isNewTransaction(),
                            status.isNewSynchronization(), status.isReadOnly(), status.isDebug(),
                            status.getSuspendedResources()));
        }
        txObject.setHasBegin(true);
    }

    @Override
    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        MultiDataSourceTransactionObject txObject = (MultiDataSourceTransactionObject) status.getTransaction();
        // log.info("set rollback only:{}", status);
        if (status.isReadOnly()) {
            return;
        }
        for (Map.Entry<DataSource, Object> entry : txObject.getDsTransactionObjectMap().entrySet()) {
            dataSourceTransactionManagerMap.get(entry.getKey())
                    .doSetRollbackOnly(new DefaultTransactionStatus(entry.getValue(), status.isNewTransaction(),
                            status.isNewSynchronization(), status.isReadOnly(), status.isDebug(),
                            status.getSuspendedResources()));
        }

    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        MultiDataSourceTransactionObject txObject = (MultiDataSourceTransactionObject) transaction;
        if (txObject.isReadOnly()) {
            return;
        }
        for (Map.Entry<DataSource, Object> entry : txObject.getDsTransactionObjectMap().entrySet()) {
            dataSourceTransactionManagerMap.get(entry.getKey()).doCleanupAfterCompletion(entry.getValue());
        }
        // log.info("do clean up after completion:{}", TransactionSynchronizationManager.getResourceMap());

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, DataSource> dataSourceMap = this.context.getBeansOfType(DataSource.class);
        dataSourceTransactionManagerMap = new HashMap<>(dataSourceMap.size());
        for (DataSource ds : dataSourceMap.values()) {
            dataSourceTransactionManagerMap.put(ds, new DataSourceTransactionManager(ds));
        }
    }

    /**
     * DataSource transaction object, representing a ConnectionHolder. Used as transaction object by
     * DataSourceTransactionManager.
     */
    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    private static class MultiDataSourceTransactionObject {
        private Map<DataSource, Object> dsTransactionObjectMap = new HashMap<>();

        private boolean hasBegin;

        private boolean readOnly;

    }

}
