package com.hao.money.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 历史记录的entity
 * Created by hao on 2014/12/20.
 */
@DatabaseTable(tableName = "tb_history")
public class History {

    public History(String name, boolean type) {
        this.name = name;
        this.type = type;
    }

    public History() {
    }

    @DatabaseField(generatedId = true)
    private int id;//id

    @DatabaseField
    private String name;//历史名称

    @DatabaseField
    private int count;//使用次数

    @DatabaseField
    private boolean type;//类型
    @DatabaseField
    private long billDate;
    @DatabaseField
    private long createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public long getBillDate() {
        return billDate;
    }

    public void setBillDate(long billDate) {
        this.billDate = billDate;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
