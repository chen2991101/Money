package com.hao.money.entity;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Administrator on 2014-12-22.
 */
public class BaseEntity implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;//id

    @DatabaseField
    private long createDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
