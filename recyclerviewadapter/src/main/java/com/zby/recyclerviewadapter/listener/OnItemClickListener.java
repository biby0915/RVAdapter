package com.zby.recyclerviewadapter.listener;

import android.view.View;

import com.zby.recyclerviewadapter.BaseRvAdapter;

/**
 * @author ZhuBingYang
 * @date 2019-07-24
 */
public interface OnItemClickListener {
    void onItemClick(BaseRvAdapter adapter, View v, int position);
}
