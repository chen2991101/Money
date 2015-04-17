package com.hao.money.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hao.money.R;
import com.hao.money.service.MineService;
import com.hao.money.service.MineView;
import com.hao.money.util.Prompt;
import com.hao.money.view.MyTitle;
import com.hao.money.view.activity.MapActivity;
import com.hao.money.view.activity.MapActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * 首页统计的fragment
 * Created by hao on 2014/11/2.
 */
@SuppressLint("ValidFragment")
@EFragment(R.layout.fragment_main_mine)
public class Main_mine_Fragment extends BaseFragment implements MineView {
    @ViewById
    TextView tv_myMoney, tv_sevenOut, tv_sevenIn, tv_thirtyOut, tv_thirtyIn;
    @ViewById
    Button bt_resetMoney;
    @ViewById
    ViewPager vp_test;
    @Bean
    MineService service;
    private EditText et_setMoney;
    private Button bt_setMoney;

    /**
     * 初始化
     */
    @AfterViews
    public void init() {
        service.setIfe(this);
        service.initMoney();

    }

    @Override
    @UiThread
    public void setMoney(String money, String sevenOut, String sevenIn, String thirtyOut, String thirtyIn) {
        tv_myMoney.setText(money);
        tv_sevenOut.setText(sevenOut);
        tv_sevenIn.setText(sevenIn);
        tv_thirtyOut.setText(thirtyOut);
        tv_thirtyIn.setText(thirtyIn);
    }

    @Override
    @UiThread
    public void setOnlyMoney(String money) {
        tv_myMoney.setText(money);
    }

    @Override
    public void showSetMoney(String msg, boolean isFindAll) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_setmoney, null);
        ((TextView) view.findViewById(R.id.tv_msg)).setText(msg);//设置标题
        et_setMoney = (EditText) view.findViewById(R.id.et_setMoney);//初始化金额数量
        bt_setMoney = (Button) view.findViewById(R.id.bt_setMoney);//确认初始化金额按钮
        bt_setMoney.setOnClickListener(new ClickListener(isFindAll));//设置点击事件
        Prompt.showView(getActivity(), view);
    }

    @Click(R.id.bt_resetMoney)
    public void click() {
        //service.resetMoney();
        //TODO 测试百度地图
        startActivity(new Intent(getActivity(), MapActivity_.class));


    }

    /**
     * 刷新金额
     */
    public void refreashMoney() {
        service.refreashMoney();
    }

    private class ClickListener implements View.OnClickListener {
        private boolean isFindAll;

        public ClickListener(boolean isFindAll) {
            this.isFindAll = isFindAll;
        }

        @Override
        public void onClick(View view) {
            service.setMoney(et_setMoney.getText().toString().trim(), isFindAll);
        }
    }
}
