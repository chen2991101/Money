package com.hao.money.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.hao.money.dao.BaseHelper;
import com.hao.money.entity.Record;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.annotations.SystemService;

import java.sql.SQLException;
import java.util.List;

/**
 * 自己实现的广播接受者
 * Created by Administrator on 2014-12-23.
 */
@EReceiver
public class MyReceiver extends BroadcastReceiver {
    @SystemService
    ConnectivityManager connectivityManager;
    @SystemService
    TelephonyManager telephonyManager;

    @OrmLiteDao(helper = BaseHelper.class, model = Record.class)
    Dao<Record, Intent> recordDao;

    @Override
    public void onReceive(Context context, Intent intent) {
    }

    /**
     * 网络状态改变的监听事件
     *
     * @param intent 意图
     */
    @ReceiverAction("android.net.conn.CONNECTIVITY_CHANGE")
    public void connectivityReceiver(Intent intent) {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            try {
                List<Record> list = recordDao.queryForEq("isUpload", false);//还没有上传的数据
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
