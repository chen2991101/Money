package com.hao.money.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

/**
 * 记录
 * Created by hao on 2014/12/20.
 */
@DatabaseTable(tableName = "tb_record")
public class Record {

    public Record(boolean type, BigDecimal money, String address, String latitude, String longitude, String remark, boolean isUpload,long billDate,long createDate) {
        this.type = type;
        this.money = money;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.remark = remark;
        this.isUpload = isUpload;
        this.billDate=billDate;
        this.createDate=createDate;
    }

    public Record() {
    }

    @DatabaseField(generatedId = true)
    private int id;//id
    @DatabaseField
    private boolean type;
    @DatabaseField
    private BigDecimal money;
    @DatabaseField
    private String address;
    @DatabaseField
    private String latitude;
    @DatabaseField
    private String longitude;
    @DatabaseField
    private String remark;
    @DatabaseField
    private long billDate;
    @DatabaseField
    private long createDate;
    @DatabaseField
    private boolean isUpload;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
}
