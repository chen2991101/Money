package com.hao.money.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

/**
 * 记录
 * Created by hao on 2014/12/20.
 */
@DatabaseTable(tableName = "tb_record")
public class Record extends BaseEntity {

    public Record(boolean type, BigDecimal money, String address, String latitude, String longitude, String remark, boolean isUpload, long billDate) {
        this.type = type;
        this.money = money;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.remark = remark;
        this.isUpload = isUpload;
        this.billDate = billDate;
    }

    public Record() {
    }

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
    private boolean isUpload;


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

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
}
