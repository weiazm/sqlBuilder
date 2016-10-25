package com.baijia.tianxiao.sqlbuilder.bean;

import java.util.Map;

/**
 * @title Condition
 * @desc TODO
 * @author cxm
 * @date 2015年8月10日
 * @version 1.0
 */
public interface Expression extends SqlElement {

    Map<String, Object> getParamNameValueMap();

    String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField);

}
