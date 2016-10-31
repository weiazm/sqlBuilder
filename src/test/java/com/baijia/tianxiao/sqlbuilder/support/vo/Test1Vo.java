/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.support.vo;

import com.baijia.tianxiao.sqlbuilder.annotation.*;
import com.baijia.tianxiao.sqlbuilder.util.ColumnUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cxm
 * @version 1.0
 * @title TestVo
 * @desc TODO
 * @date 2015年12月3日
 */
@Entity(dynamicInsert = true, dynamicUpdate = true, name = "test1")
@Table(catalog = "test1")
@Data
@NoArgsConstructor
public class Test1Vo {

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

    public Test1Vo(Number number, String test) {
        this.number = number;
        this.test = test;
    }

    public static void main(String[] args) {
        Test1Vo test = new Test1Vo();
        ColumnUtil.getSaveInfoFromObjs(test, false, false);
    }

}
