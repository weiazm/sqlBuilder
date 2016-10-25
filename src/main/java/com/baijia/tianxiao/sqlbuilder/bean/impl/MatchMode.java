package com.baijia.tianxiao.sqlbuilder.bean.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public abstract class MatchMode {
    private final String name;
    private static final Map<String, MatchMode> INSTANCES = new HashMap<String, MatchMode>();

    private static final List<Character> MYSQL_KEY_CHARS = Lists.newArrayList('%', '_', '\\');

    protected MatchMode(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    /**
     * Match the entire string to the pattern
     */
    public static final MatchMode EXACT = new MatchMode("EXACT") {
        public String toMatchString(String pattern) {
            return escapeKey(pattern);
        }
    };

    /**
     * Match the start of the string to the pattern
     */
    public static final MatchMode START = new MatchMode("START") {
        public String toMatchString(String pattern) {
            return escapeKey(pattern) + '%';
        }
    };

    /**
     * Match the end of the string to the pattern
     */
    public static final MatchMode END = new MatchMode("END") {
        public String toMatchString(String pattern) {
            return '%' + escapeKey(pattern);
        }
    };

    /**
     * Match the pattern anywhere in the string
     */
    public static final MatchMode ANYWHERE = new MatchMode("ANYWHERE") {
        public String toMatchString(String pattern) {
            return '%' + escapeKey(pattern) + '%';
        }
    };

    static {
        INSTANCES.put(EXACT.name, EXACT);
        INSTANCES.put(END.name, END);
        INSTANCES.put(START.name, START);
        INSTANCES.put(ANYWHERE.name, ANYWHERE);
    }

    private Object readResolve() {
        return INSTANCES.get(name);
    }

    public String escapeKey(String pattern) {
        StringBuilder sb = new StringBuilder();
        for (char c : pattern.toCharArray()) {
            if (MYSQL_KEY_CHARS.contains(c)) {
                sb.append("\\");
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * convert the pattern, by appending/prepending "%"
     */
    public abstract String toMatchString(String pattern);

}
