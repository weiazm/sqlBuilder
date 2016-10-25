package com.baijia.tianxiao.sqlbuilder.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

    /**
     * (Optional) The entity name. Defaults to the unqualified name of the entity class. This name is used to refer to
     * the entity in queries. The name must not be a reserved literal in the Java Persistence query language.
     */
    String name() default "";

    /** 默认不插入null值 */
    boolean dynamicInsert() default true;

    /** 默认不更新属性为null值的字段 */
    boolean dynamicUpdate() default true;

    /**
     * 当前表对应的数据源的bean的名称,如果只有一个数据源可以不配
     * 
     * @return
     */
    String dataSourceBeanName() default "";

    String jdbcTemplateBeanName() default "";

}