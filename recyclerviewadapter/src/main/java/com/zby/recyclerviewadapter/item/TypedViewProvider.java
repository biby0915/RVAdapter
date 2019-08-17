package com.zby.recyclerviewadapter.item;

import com.zby.recyclerviewadapter.ViewHolder;

/**
 * @author ZhuBingYang
 * @date 2019-07-31
 */
public interface TypedViewProvider<T, V extends ViewHolder> extends ViewProvider<T,V>{
    int getViewType();
}
