package com.hao.money.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hao.money.R;
import com.hao.money.util.KeyboardUtil;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;
import com.hao.money.view.fragment.BaseFragment;
import com.hao.money.view.fragment.Main_JL_Fragment;
import com.hao.money.view.fragment.Main_JZ_Fragment;
import com.hao.money.view.fragment.Main_mine_Fragment;

import java.util.List;

/**
 * 主页的activity
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private RadioGroup rg_button;//首页的按钮组
    private android.support.v4.app.FragmentManager fm;//framgnet的管理器
    private EditText et_initMoney;
    private Button bt_initMoney;
    private Main_mine_Fragment main_mine_Fragment;
    private float money;
    private static final String SUMMONEY = "sumMoney";//保存在xml文件中我的身价
    private Main_JZ_Fragment main_JZ_Fragment;

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

        SharedPreferences info = getSharedPreferences("info", 0);
        float m = info.getFloat(SUMMONEY, -1);//获取保存的数据
        if (m == -1) {
            initMoney();//初始化身上的钱
        }
        money = m == -1 ? 0 : m;//获取我当前的金额

        //设置记账为选中
        RadioButton rb = (RadioButton) findViewById(R.id.rb_mine);
        rb.setChecked(true);

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
                    main_JZ_Fragment = new Main_JZ_Fragment();
                    fragment = main_JZ_Fragment;
                    break;
                case R.id.rb_jl:
                    fragment = new Main_JL_Fragment();
                    break;
                default:
                    main_mine_Fragment = new Main_mine_Fragment(money);
                    fragment = main_mine_Fragment;
                    break;
            }
            fragment.setClickId(checkedId);
            fm.beginTransaction().add(R.id.fl_content, fragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        String str = et_initMoney.getText().toString();//用户输入的钱数
        if (TextUtils.isEmpty(str) || TestUtil.testMoney(str)) {
            KeyboardUtil.closeKeyboard(this);//关闭软键盘

            float money = TextUtils.isEmpty(str) ? 0 : Float.parseFloat(str);//用户输入的金额

            //把用户输入的金额写入到xml文件中
            SharedPreferences info = getSharedPreferences("info", 0);
            SharedPreferences.Editor editor = info.edit();
            editor.putFloat(SUMMONEY, money).commit();

            Prompt.hideDialog();
            main_mine_Fragment.refreashMoney(money);//刷新我的金额
        } else {
            Prompt.showToast(this, "请正确输入金额");
        }
    }


    /**
     * 两次返回键退出应用
     */
    private long exitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        Prompt.showToast(this, "再按一次返回键退出");
                        exitTime = System.currentTimeMillis();
                    } else {
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                    return true;
                }
                return super.onKeyDown(keyCode, event);
            default:
                return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            main_JZ_Fragment.setRemark(data.getStringExtra("remark"));//选择历史后填充到统计的输入框中
        }
    }
}
