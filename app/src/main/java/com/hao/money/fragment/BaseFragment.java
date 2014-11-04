package com.hao.money.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by hao on 2014/11/3.
 */
public class BaseFragment extends Fragment {
    private int clickId;

    public int getClickId() {
        return clickId;
    }

    public void setClickId(int clickId) {
        this.clickId = clickId;
    }
}
