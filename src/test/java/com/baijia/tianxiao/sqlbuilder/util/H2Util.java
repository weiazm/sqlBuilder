package com.baijia.tianxiao.sqlbuilder.util;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.h2.tools.RunScript;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * H2内存数据库测试专用工具
 * 
 * @author xiaoming.chen
 *
 */
@Slf4j
public class H2Util {

    /**
     * 
     */
    private static final String DEFAULT_H2_PASSWORD = "password";
    /**
     * 
     */
    private static final String DEFAULT_H2_USERNAME = "sa";

    /**
     * 返回默认用户名和密码的h2数据源
     * 
     * @return h2数据源
     */
    public static DataSource getH2MemDataSource() {
        return getH2MemDataSource(DEFAULT_H2_USERNAME, DEFAULT_H2_PASSWORD);
    }

    /**
     * 创建指定用户名和密码的h2内存数据库
     * 
     * @param username 用户名
     * @param password 密码
     * @return h2数据源
     */
    public static DataSource getH2MemDataSource(String username, String password) {
        log.debug("get H2 data source.");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        if (StringUtils.isNoneBlank(username)) {
            dataSource.setUsername(username);
        }
        if (StringUtils.isNoneBlank(password)) {
            dataSource.setPassword(password);
        }
        dataSource.setUrl("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1");
        log.info("get H2 datasource by username:" + username);
        return dataSource;
    }

    public static DataSource initAndGetH2MemDataSource(String username, String password) {
        DataSource ds = getH2MemDataSource(username, password);
        Connection conn = null;
        try {
            conn = ds.getConnection();
            initSqlFile(ds.getConnection(), "schema.sql");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ds;
    }

    public static DataSource initAndGetH2MemDataSource() {
        return initAndGetH2MemDataSource(DEFAULT_H2_USERNAME, DEFAULT_H2_PASSWORD);
    }

    /**
     * 将sql文件导入到h2数据库中，SQL文件例子：/conf/H2Test.sql
     * 
     * @param conn h2数据库连接
     * @param location 文件路径，一般放在 test的 resources文件夹的conf目录下
     * @throws SQLException 抛出SQL异常
     */
    public static void initSqlFile(Connection conn, String location) throws SQLException {
        // for example:
        // RunScript.execute(conn, new
        // InputStreamReader(H2Util.class.getResourceAsStream("/conf/H2Test.sql")));
        RunScript.execute(conn, new InputStreamReader(H2Util.class.getClassLoader().getResourceAsStream(location)));
    }

    /**
     * 清理内存数据库
     * 
     * @param conn 内存数据库连接
     */
    public static void clearMemDB(Connection conn) {
        String clearSql = "truncate test.test";
        try {
            conn.createStatement().executeUpdate(clearSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {

        DataSource ds = getH2MemDataSource(DEFAULT_H2_USERNAME, "pass");
        initSqlFile(ds.getConnection(), "/schema.sql");

        String sql = "select * from test.test";
        ResultSet rs = ds.getConnection().createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getInt(1));
        }
        // clearMemDB(ds.getConnection());
        ds.getConnection().close();

        rs = ds.getConnection().createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getInt(1));
        }
        System.out.println(ds.getConnection());
    }

}