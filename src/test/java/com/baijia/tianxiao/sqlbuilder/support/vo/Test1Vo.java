/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.support.vo;

import com.baijia.tianxiao.sqlbuilder.annotation.Column;
import com.baijia.tianxiao.sqlbuilder.annotation.Entity;
import com.baijia.tianxiao.sqlbuilder.annotation.GeneratedValue;
import com.baijia.tianxiao.sqlbuilder.annotation.Id;
import com.baijia.tianxiao.sqlbuilder.annotation.Table;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title TestVo
 * @desc TODO
 * @author cxm
 * @date 2015年12月3日
 * @version 1.0
 */
@Entity(dynamicInsert = true, dynamicUpdate = true, name = "test1")
@Table(catalog = "test1")
@Data
@NoArgsConstructor
public class Test1Vo {

    public Test1Vo(Number number, String test) {
        this.number = number;
        this.test = test;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "num")
    private Number number;

    @Column
    private String test;

    @Column(defaultVal = "0")
    private Date startTime;

    private String abc;

    public static void main(String[] args) {
        Test1Vo test = new Test1Vo();
        ColumnUtil.getSaveInfoFromObjs(test, false, false);
    }

}
