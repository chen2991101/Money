package com.hao.money.view.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.hao.money.R;
import com.hao.money.adapter.MyFragmentPager;
import com.hao.money.util.Prompt;
import com.hao.money.view.MyApplication;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

/**
 * 主页的activity
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    @ViewById
    RadioButton rb_mine, rb_jz, rb_jl;
    @ViewById
    ViewPager vp_pager;
    @App
    MyApplication application;
    @SystemService
    TelephonyManager TelephonyMgr;
    private android.support.v4.app.FragmentManager fm;//framgnet的管理器
    public static boolean refreshMain = false;
    public static boolean refreshJl = false;

    /**
     * 初始化方法
     */
    @AfterViews
    public void init() {
        fm = getSupportFragmentManager();
        MyFragmentPager mfp = new MyFragmentPager(fm);
        vp_pager.setAdapter(mfp);
        vp_pager.setOffscreenPageLimit(2);
        rb_mine.setSelected(true);
        vp_pager.setOnPageChangeListener(this);

     /*   //设置记账为选中
        RadioButton rb = (RadioButton) findViewById(R.id.rb_mine);
        rb.setChecked(true);*/

        /**
         * 绑定联网改变的事件
         */
  /*      IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        MyReceiver_ receiver = new MyReceiver_();
        registerReceiver(receiver, filter);*/
    }

    @CheckedChange({R.id.rb_mine, R.id.rb_jz, R.id.rb_jl})
    public void checkedChanged(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            switch (button.getId()) {
                case R.id.rb_mine:
                    vp_pager.setCurrentItem(0);
                    break;
                case R.id.rb_jz:
                    vp_pager.setCurrentItem(1);
                    break;
                case R.id.rb_jl:
                    vp_pager.setCurrentItem(2);
                    break;
            }
        }
    }


    /**
     * 两次返回键退出应用
     */
    private long exitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
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
            //main_JZ_Fragment.setRemark(data.getStringExtra("remark"));//选择历史后填充到统计的输入框中
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                rb_mine.setChecked(true);
                break;
            case 1:
                rb_jz.setChecked(true);
                break;
            case 2:
                rb_jl.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
