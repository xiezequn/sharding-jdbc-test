package com.dangdang.ddframe.rdb.sharding.example.jdbc.entity;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: xiezq
 * Date: 2017/1/11
 * Depiction:
 */
public class Function implements Serializable {
    private float avg;
    private int count;
    private int distinctCount;
    private long sum;

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDistinctCount() {
        return distinctCount;
    }

    public void setDistinctCount(int distinctCount) {
        this.distinctCount = distinctCount;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }
}
