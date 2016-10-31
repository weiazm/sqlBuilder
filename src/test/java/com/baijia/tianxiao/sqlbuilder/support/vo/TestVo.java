/**
 * Baijiahulian.com Inc. Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.baijia.tianxiao.sqlbuilder.support.vo;

import com.baijia.tianxiao.sqlbuilder.annotation.*;
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
@Entity(dynamicInsert = true, dynamicUpdate = true, name = "test")
@Table(catalog = "test")
@Data
@NoArgsConstructor
public class TestVo {

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

    public TestVo(Number number, String test) {
        this.number = number;
        this.test = test;
    }

}
