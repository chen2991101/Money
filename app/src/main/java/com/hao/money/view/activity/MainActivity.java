package com.hao.money.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.hao.money.R;
import com.hao.money.dao.Info_;
import com.hao.money.util.Prompt;
import com.hao.money.util.TestUtil;
import com.hao.money.view.fragment.BaseFragment;
import com.hao.money.view.fragment.Main_JL_Fragment;
import com.hao.money.view.fragment.Main_JL_Fragment_;
import com.hao.money.view.fragment.Main_JZ_Fragment;
import com.hao.money.view.fragment.Main_JZ_Fragment_;
import com.hao.money.view.fragment.Main_mine_Fragment;
import com.hao.money.view.fragment.Main_mine_Fragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Calendar;
import java.util.List;

/**
 * 主页的activity
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {
    @ViewById
    RadioButton rb_mine, rb_jz, rb_jl;
    private android.support.v4.app.FragmentManager fm;//framgnet的管理器
    private Main_mine_Fragment main_mine_Fragment;
    private Main_JZ_Fragment main_JZ_Fragment;
    public static boolean refreshMoeny = false;

    /**
     * 初始化方法
     */
    @AfterViews
    public void init() {
        fm = getSupportFragmentManager();

        //设置记账为选中
        RadioButton rb = (RadioButton) findViewById(R.id.rb_mine);
        rb.setChecked(true);

    }

    @CheckedChange({R.id.rb_mine, R.id.rb_jz, R.id.rb_jl})
    public void checkedChanged(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            int checkedId = button.getId();
            List<Fragment> fragments = fm.getFragments();//获取所有的fragment
            boolean b = true;
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (checkedId == ((BaseFragment) fragment).getClickId()) {
                        resumeView(checkedId);//恢复显示fragment时的操作
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
                        main_JZ_Fragment = new Main_JZ_Fragment_();
                        fragment = main_JZ_Fragment;
                        break;
                    case R.id.rb_jl:
                        fragment = new Main_JL_Fragment_();
                        break;
                    default:
                        main_mine_Fragment = new Main_mine_Fragment_();
                        fragment = main_mine_Fragment;
                        break;
                }
                fragment.setClickId(checkedId);
                fm.beginTransaction().add(R.id.fl_content, fragment).commit();
            }
        }
    }

    /**
     * 恢复显示fragment时的操作
     *
     * @param checkedId 当前恢复的哪一个视图
     */
    public void resumeView(int checkedId) {
        if (checkedId == R.id.rb_mine && refreshMoeny) {
            //如果添加了记录需要更新我的金额
            //main_mine_Fragment.refreashMoney();
            refreshMoeny = false;
        } else if (checkedId == R.id.rb_jz) {
            main_JZ_Fragment.initDateTime(Calendar.getInstance());
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
