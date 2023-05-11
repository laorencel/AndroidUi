package com.laorencel.uilibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * 简单封装，在代码中使用时就不需要再设置Typeface了
 */
@SuppressLint("AppCompatCustomView")
public class FontIconTextView extends TextView {
    public FontIconTextView(Context context) {
        super(context);
    }

    public FontIconTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FontIconTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FontIconTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        //设置字体图标
        Typeface font = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        this.setTypeface(font);

        //正常的写法（只是代码中，xml中不需要）
//        Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
//        textView.setTypeface(font);
//        textView.setText(getResources().getString(R.string.font_icon_add));
        //使用FontIconTextView的写法
//        textView.setText(getResources().getString(R.string.font_icon_add));
    }
}
