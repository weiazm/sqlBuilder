/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.support.vo;

import com.baijia.tianxiao.sqlbuilder.annotation.Column;
import com.baijia.tianxiao.sqlbuilder.annotation.Entity;
import com.baijia.tianxiao.sqlbuilder.annotation.GeneratedValue;
import com.baijia.tianxiao.sqlbuilder.annotation.Id;
import com.baijia.tianxiao.sqlbuilder.annotation.Table;
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
@Entity(dynamicInsert = true, dynamicUpdate = true, name = "test")
@Table(catalog = "test")
@Data
@NoArgsConstructor
public class TestVo {

    public TestVo(Number number, String test) {
        this.number = number;
        this.test = test;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "num", defaultVal = "1000")
    private Number number;

    @Column(defaultVal = "test default")
    private String test;

    @Column
    private Date startTime;

    private String abc;

}
