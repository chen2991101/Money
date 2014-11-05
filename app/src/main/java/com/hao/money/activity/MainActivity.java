package com.hao.money.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hao.money.R;
import com.hao.money.fragment.BaseFragment;
import com.hao.money.fragment.Main_JL_Fragment;
import com.hao.money.fragment.Main_JZ_Fragment;
import com.hao.money.fragment.Main_mine_Fragment;
import com.hao.money.util.KeyboardUtil;
import com.hao.money.util.Prompt;

import java.util.List;

/**
 * 主页的activity
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private RadioGroup rg_button;//首页的按钮组
    private android.support.v4.app.FragmentManager fm;//framgnet的管理器
    private EditText et_initMoney;
    private Button bt_initMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        fm = getSupportFragmentManager();

        rg_button = (RadioGroup) findViewById(R.id.rg_button);
        rg_button.setOnCheckedChangeListener(this);

        //设置记账为选中
        RadioButton rb = (RadioButton) findViewById(R.id.rb_mine);
        rb.setChecked(true);

        SharedPreferences info = getSharedPreferences("info", 0);
        float money = info.getFloat("sumMoney", -1);//获取保存的数据
        if (money == -1) {
            initMoney();//初始化身上的钱
        }
    }

    /**
     * 第一次进入程序后让用户输入初始化的金额
     */
    private void initMoney() {
        View view = getLayoutInflater().inflate(R.layout.dialog_initmoney, null);
        et_initMoney = (EditText) view.findViewById(R.id.et_initMoney);//初始化金额数量
        bt_initMoney = (Button) view.findViewById(R.id.bt_initMoney);//确认初始化金额按钮
        bt_initMoney.setOnClickListener(this);//设置点击事件
        Prompt.showDialog(this, view, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        List<Fragment> fragments = fm.getFragments();//获取所有的fragment
        boolean b = true;
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (checkedId == ((BaseFragment) fragment).getClickId()) {
                    fm.beginTransaction().show(fragment).commit();
                    b = false;
                } else {
                    fm.beginTransaction().hide(fragment).commit();
                }
            }
        }

        //添加对应的fragment
        if (b) {
            BaseFragment fragment = null;
            switch (checkedId) {
                case R.id.rb_jz:
                    fragment = new Main_JZ_Fragment();
                    break;
                case R.id.rb_jl:
                    fragment = new Main_JL_Fragment();
                    break;
                case R.id.rb_mine:
                    fragment = new Main_mine_Fragment();
                    break;
            }
            fragment.setClickId(checkedId);
            fm.beginTransaction().add(R.id.fl_content, fragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        String str = et_initMoney.getText().toString();//用户输入的钱数
        if (TextUtils.isEmpty(str) || str.matches("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$")) {
            KeyboardUtil.closeKeyboard(this, et_initMoney);//关闭软键盘
            Prompt.closeDialog();
        } else {
            Prompt.showTost(this, "请正确输入金额");
        }
    }
}
