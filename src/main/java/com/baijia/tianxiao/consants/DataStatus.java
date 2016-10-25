package com.baijia.tianxiao.consants;

/**
 * Created by wengshengli on 15/12/8.
 */
public enum DataStatus {

    NORMAL(0), DELETE(1);
    private int value;

    private DataStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
