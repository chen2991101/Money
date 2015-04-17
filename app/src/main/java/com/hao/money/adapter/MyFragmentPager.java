package com.hao.money.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hao.money.view.fragment.Main_JL_Fragment_;
import com.hao.money.view.fragment.Main_JZ_Fragment_;
import com.hao.money.view.fragment.Main_mine_Fragment_;

/**
 * Created by hao on 15/4/17.
 */
public class MyFragmentPager extends FragmentPagerAdapter {

    public MyFragmentPager(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Main_mine_Fragment_();
            case 1:
                return new Main_JZ_Fragment_();
            case 2:
                return new Main_JL_Fragment_();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 3;
    }
}
