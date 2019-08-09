package com.zby.recyclerviewadapter.listener;

import android.view.View;

import com.zby.recyclerviewadapter.BaseRvAdapter;

/**
 * @author ZhuBingYang
 * @date 2019-08-02
 */
public interface OnItemChildClickListener {
    void onItemChildClick(BaseRvAdapter adapter, View v, int position);
}
