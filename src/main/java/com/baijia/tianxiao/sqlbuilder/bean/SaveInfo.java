package com.baijia.tianxiao.sqlbuilder.bean;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class SaveInfo {
    private Set<String> columns = Sets.newHashSet();

    private Map<String, Object> paramMap = Maps.newHashMap();
}