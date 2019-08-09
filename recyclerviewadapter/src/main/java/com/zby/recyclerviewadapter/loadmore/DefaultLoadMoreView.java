package com.zby.recyclerviewadapter.loadmore;

import android.view.View;

import com.zby.recyclerviewadapter.R;
import com.zby.recyclerviewadapter.ViewHolder;

/**
 * @author ZhuBingYang
 * @date 2019-08-04
 */
public class DefaultLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.rv_adapter_load_more;
    }

    @Override
    public View getLoadingView(ViewHolder holder) {
        return holder.getView(R.id.rv_adapter_load_loading);
    }

    @Override
    public View getLoadFailView(ViewHolder holder) {
        return holder.getView(R.id.rv_adapter_load_fail);
    }

    @Override
    public View getLoadEndView(ViewHolder holder) {
        return holder.getView(R.id.rv_adapter_load_end);
    }
}
