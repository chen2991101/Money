package com.hao.money.view;

import android.app.Activity;
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
    private TextView tv_title, tv_back;


    public MyTitle(final Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyTitle);

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.include_head_hasback, this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(a.getString(R.styleable.MyTitle_name));


        boolean hasBack = a.getBoolean(R.styleable.MyTitle_hasBack, false);
        //如果有返回键需要设置返回
        if (hasBack) {
            tv_back = (TextView) view.findViewById(R.id.tv_back);//返回键
            tv_back.setVisibility(VISIBLE);
            tv_back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).finish();
                }
            });
        }
        a.recycle();
    }

    public MyTitle(Context context) {
        super(context);
    }
}
