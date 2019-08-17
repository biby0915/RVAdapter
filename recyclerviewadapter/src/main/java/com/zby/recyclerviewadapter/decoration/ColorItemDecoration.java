package com.zby.recyclerviewadapter.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author ZhuBingYang
 * @date 2019-08-12
 */
public class ColorItemDecoration extends RecyclerView.ItemDecoration {
    private IColorDecoration mColorDecoration;
    protected Paint mPaint;

    public ColorItemDecoration(IColorDecoration colorDecoration) {
        mColorDecoration = colorDecoration;
        mPaint = new Paint();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int position;
        Rect rect = new Rect();

        for (int i = 0; i < parent.getChildCount(); i++) {

            View view = parent.getChildAt(i);

            position = parent.getChildAdapterPosition(view);
            rect.top = view.getTop() - mColorDecoration.getHeight(position);
            rect.left = parent.getPaddingLeft();
            rect.bottom = view.getTop();
            rect.right = parent.getWidth() - parent.getPaddingRight();

            drawItemDecoration(c, position, rect);
        }
    }

    public void drawItemDecoration(Canvas c, int position, Rect rect) {
        mPaint.setColor(mColorDecoration.getColor(position));
        c.drawRect(rect.left, rect.top, rect.right, rect.bottom, mPaint);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, mColorDecoration.getHeight(parent.getChildAdapterPosition(view)), 0, 0);
    }
}
