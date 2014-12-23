package com.hao.money.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hao.money.R;
import com.hao.money.util.Prompt;
import com.hao.money.util.UrlUtil;
import com.hao.money.util.Util;
import com.hao.money.view.MyApplication;
import com.hao.money.view.fragment.BaseFragment;
import com.hao.money.view.fragment.Main_JL_Fragment;
import com.hao.money.view.fragment.Main_JL_Fragment_;
import com.hao.money.view.fragment.Main_JZ_Fragment;
import com.hao.money.view.fragment.Main_JZ_Fragment_;
import com.hao.money.view.fragment.Main_mine_Fragment;
import com.hao.money.view.fragment.Main_mine_Fragment_;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import java.util.List;

/**
 * 主页的activity
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {
    @ViewById
    RadioButton rb_mine, rb_jz, rb_jl;
    @App
    MyApplication application;
    @SystemService
    TelephonyManager TelephonyMgr;
    private android.support.v4.app.FragmentManager fm;//framgnet的管理器
    private Main_mine_Fragment main_mine_Fragment;
    private Main_JZ_Fragment main_JZ_Fragment;
    private Main_JL_Fragment main_jl_fragment;
    public static boolean refreshMain = false;
    public static boolean refreshJl = false;


    /**
     * 初始化方法
     */
    @AfterViews
    public void init() {
        fm = getSupportFragmentManager();

        //设置记账为选中
        RadioButton rb = (RadioButton) findViewById(R.id.rb_mine);
        rb.setChecked(true);

        application.addressListener = new AddressListener();
        application.mLocationClient.registerLocationListener(application.addressListener);
        application.mLocationClient.start();

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
                        fm.beginTransaction().show(fragment).commit();
                        resumeView(checkedId);//恢复显示fragment时的操作
                        b = false;
                    } else {
                        fm.beginTransaction().hide(fragment).commit();
                    }
                }
            }

            //添加对应的fragment
            if (b) {
                BaseFragment fragment;
                switch (checkedId) {
                    case R.id.rb_jz:
                        main_JZ_Fragment = new Main_JZ_Fragment_();
                        fragment = main_JZ_Fragment;
                        break;
                    case R.id.rb_jl:
                        main_jl_fragment = new Main_JL_Fragment_();
                        fragment = main_jl_fragment;
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
        if (checkedId == R.id.rb_mine && refreshMain) {
            //如果添加了记录需要更新我的金额
            main_mine_Fragment.refreashMoney();
            refreshMain = false;
        } else if (checkedId == R.id.rb_jl && refreshJl) {
            main_jl_fragment.refreash();
            refreshJl = false;
        } else if (checkedId == R.id.rb_jz) {
            main_JZ_Fragment.initDateTime();
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
            main_JZ_Fragment.setRemark(data.getStringExtra("remark"));//选择历史后填充到统计的输入框中
        }
    }

    /**
     * 定位回调事件
     */
    private class AddressListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                application.mLocationClient.stop();//取消定位服务
                return;
            }
            String address = location.getAddrStr();
            if (address != null) {
                RequestParams params = new RequestParams();
                params.put("address", address);
                params.put("latitude", location.getLatitude());
                params.put("longitude", location.getLongitude());
                params.put("deviceId", TelephonyMgr.getDeviceId());//手机的唯一编码
                Util.post(UrlUtil.upLoadAddress, params, new uploadHandler());
            }
            application.mLocationClient.stop();//取消定位服务
        }
    }


    /**
     * 上传地址的回调，不做任务处理
     */
    private class uploadHandler extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {

        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        }
    }
}
