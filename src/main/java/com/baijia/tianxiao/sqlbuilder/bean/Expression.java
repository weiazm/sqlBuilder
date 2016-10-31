package com.baijia.tianxiao.sqlbuilder.bean;

import java.util.Map;

/**
 * @author cxm
 * @version 1.0
 * @title Condition
 * @desc TODO
 * @date 2015年8月10日
 */
public interface Expression extends SqlElement {

    Map<String, Object> getParamNameValueMap();

    String checkAndGetColumn(Map<String, String> fieldMapColumn, Map<String, String> columnMapField);

}
