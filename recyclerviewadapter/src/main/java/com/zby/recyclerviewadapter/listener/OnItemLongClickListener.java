package com.zby.recyclerviewadapter.listener;

import android.view.View;

import com.zby.recyclerviewadapter.BaseRvAdapter;

/**
 * @author ZhuBingYang
 * @date 2019-07-30
 */
public interface OnItemLongClickListener {
    boolean onItemLongClick(BaseRvAdapter adapter, View view, int position);
}
