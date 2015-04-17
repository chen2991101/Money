package com.hao.money.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.RadioButton;

import com.hao.money.view.fragment.Main_JL_Fragment;
import com.hao.money.view.fragment.Main_JL_Fragment_;
import com.hao.money.view.fragment.Main_JZ_Fragment;
import com.hao.money.view.fragment.Main_JZ_Fragment_;
import com.hao.money.view.fragment.Main_mine_Fragment;
import com.hao.money.view.fragment.Main_mine_Fragment_;

/**
 * Created by hao on 15/4/17.
 */
public class MyFragmentPager extends FragmentPagerAdapter {
    private Main_mine_Fragment main_mine_Fragment;
    private Main_JZ_Fragment main_JZ_Fragment;
    private Main_JL_Fragment main_jl_fragment;

    public MyFragmentPager(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Main_mine_Fragment_();
                break;
            case 1:
                fragment = new Main_JZ_Fragment_();
                break;
            case 2:
                fragment = new Main_JL_Fragment_();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("num", position);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getCount() {
        return 3;
    }
}
