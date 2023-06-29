package com.laorencel.uilibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.laorencel.uilibrary.R;
import com.laorencel.uilibrary.util.EmptyUtil;

/**
 * 简单封装，在代码中使用时就不需要再设置Typeface了
 */
@SuppressLint("AppCompatCustomView")
public class FontIconTextView extends TextView {
    private Context context;
    private String typefaceAsset = "iconfont.ttf";

    public FontIconTextView(Context context) {
        super(context);
        init(context, null);
    }

    public FontIconTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontIconTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public FontIconTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setTypefaceAsset(String typefaceAsset) {
        if (!EmptyUtil.isEmpty(typefaceAsset)) {
            this.typefaceAsset = typefaceAsset;
            //设置字体图标
            Typeface font = Typeface.createFromAsset(context.getAssets(), typefaceAsset);
            this.setTypeface(font);
        }

    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context, @Nullable AttributeSet attrs) {
        this.context = context;
        if (null != attrs) {
            try (TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FontIconTextView)) {
                typefaceAsset = ta.getString(R.styleable.FontIconTextView_typefaceAsset);
//                ta.recycle();
            }
        }
        if (!EmptyUtil.isEmpty(typefaceAsset)) {
            //设置字体图标
            Typeface font = Typeface.createFromAsset(context.getAssets(), typefaceAsset);
            this.setTypeface(font);
        }

//        setText(getResources().getString(R.string.font_icon_add));

        //正常的写法（只是代码中，xml中不需要）
//        Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
//        textView.setTypeface(font);
//        textView.setText(getResources().getString(R.string.font_icon_add));
        //使用FontIconTextView的写法
//        textView.setText(getResources().getString(R.string.font_icon_add));
    }
}
