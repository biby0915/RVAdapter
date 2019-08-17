package com.zby.recyclerviewadapter.decoration;

import android.support.annotation.ColorInt;

/**
 * @author ZhuBingYang
 * @date 2019-08-12
 */
public interface IColorDecoration {
    int getHeight(int position);

    @ColorInt
    int getColor(int position);
}
