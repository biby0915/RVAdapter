package com.zby.recyclerviewadapter.decoration;

/**
 * @author ZhuBingYang
 * @date 2019-08-12
 */
public interface IInsetColorDecoration extends IColorDecoration {
    InsetValue getInset(int position, InsetValue value);
}
