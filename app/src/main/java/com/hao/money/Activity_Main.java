package com.hao.money;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

/**
 * 主页的activity
 */
public class Activity_Main extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg_button;//首页的按钮组
    private android.support.v4.app.FragmentManager fm;//framgnet的管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        rg_button = (RadioGroup) findViewById(R.id.rg_button);
        rg_button.setOnCheckedChangeListener(this);

        //设置记账为选中
        RadioButton rb = (RadioButton) findViewById(R.id.rb_jz);
        rb.setChecked(true);
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
                    fragment = new Fragment_main_jz();
                    break;
                case R.id.rb_jl:
                    fragment = new Fragment_main_jl();
                    break;
                case R.id.rb_mine:
                    fragment = new Fragment_main_mine();
                    break;
            }
            fragment.setClickId(checkedId);
            fm.beginTransaction().add(R.id.fl_content, fragment).commit();
        }
    }
}
