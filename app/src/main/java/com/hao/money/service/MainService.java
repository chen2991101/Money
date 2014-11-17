package com.hao.money.service;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.hao.money.dao.HistoryDao;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;

/**
 * 主界面的service
 * Created by Administrator on 2014-11-17.
 */
public class MainService {

    private Activity activity;

    public MainService(Activity activity) {
        this.activity = activity;
    }

    /**
     * 记账
     *
     * @param et_date   日期
     * @param et_time   时间
     * @param et_money  金额
     * @param et_remark 用途
     */
    public void jz(EditText et_date, EditText et_time, EditText et_money, EditText et_remark) {
        String money = et_money.getText().toString().trim();//输入的金额
        String remark = et_remark.getText().toString().trim();//备注
        //验证金额
        if (TextUtils.isEmpty(money) && !TestUtil.testMoney(money)) {
            Prompt.showToast(activity, "请输入正确的金额");
            return;
        }
        if (TextUtils.isEmpty(remark)) {
            Prompt.showToast(activity, "请输入用途");
            return;
        }
        HistoryDao historyDao = new HistoryDao();
        historyDao.add(activity, remark);
    }
}
