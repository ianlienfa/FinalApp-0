package com.example.finalapp;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class AutoScrollTextView extends AppCompatTextView {
    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoScrollTextView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        if (focused){
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }else{
            super.onFocusChanged(false, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if (focused)
            super.onWindowFocusChanged(focused);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    private void init() {
        setEllipsize(TextUtils.TruncateAt.MARQUEE);// 對應android:ellipsize="marquee"
        setMarqueeRepeatLimit(-1);// 對應android:marqueeRepeatLimit="marquee_forever"
        setSingleLine();// 等價於setSingleLine(true）
    }
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility== View.VISIBLE){
            setFocusable(true);
        }else if(visibility==View.INVISIBLE){
            setFocusable(false);
        }
    }
}
