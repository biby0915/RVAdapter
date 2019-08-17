package com.zby.recyclerviewadapter.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * @author ZhuBingYang
 * @date 2019-08-12
 */
public class InsetColorItemDecoration extends ColorItemDecoration {
    private int mDefaultColor;
    private IInsetColorDecoration mDecoration;

    public InsetColorItemDecoration(int defaultColor, IInsetColorDecoration decoration) {
        super(decoration);
        mDefaultColor = defaultColor;
        mDecoration = decoration;
    }

    @Override
    public void drawItemDecoration(Canvas c, int position, Rect rect) {
        mPaint.setColor(mDefaultColor);
        c.drawRect(rect, mPaint);

        mPaint.setColor(mDecoration.getColor(position));
        InsetValue value = mDecoration.getInset(position, new InsetValue());
        rect.left += value.left;
        rect.right -= value.right;
        c.drawRect(rect, mPaint);
    }
}
