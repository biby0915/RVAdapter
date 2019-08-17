package com.zby.recyclerviewadapter.item;

import com.zby.recyclerviewadapter.BaseRvAdapter;
import com.zby.recyclerviewadapter.ViewHolder;

/**
 * @author ZhuBingYang
 * @date 2019-08-16
 */
public interface ViewProvider<T, V extends ViewHolder> {
    int getLayoutResId();

    void convert(BaseRvAdapter adapter, V holder, T data, int position);
}
