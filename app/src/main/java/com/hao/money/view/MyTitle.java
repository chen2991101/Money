package com.hao.money.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hao.money.R;

/**
 * Created by hao on 15/4/15.
 */
public class MyTitle extends RelativeLayout {
    private View view;
    private TextView tv_title;


    public MyTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyTitle);


        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.include_head, this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(a.getString(R.styleable.MyTitle_name));
        a.recycle();
    }

    public MyTitle(Context context) {
        super(context);
    }

    public void setTitleName(String name) {
        tv_title.setText(name);
    }
}
