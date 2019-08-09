package com.zby.recyclerviewadapter.loadmore;

import android.view.View;

import com.zby.recyclerviewadapter.ViewHolder;

/**
 * @author ZhuBingYang
 * @date 2019-08-04
 */
public abstract class LoadMoreView {
    public static final int LOAD_STATUS_DEFAULT = 1;
    public static final int LOAD_STATUS_LOADING = 2;
    public static final int LOAD_STATUS_FAIL = 3;
    public static final int LOAD_STATUS_END = 4;

    private int mLoadMoreStatus = LOAD_STATUS_DEFAULT;

    public void convert(ViewHolder holder) {
        View mLoadingView = getLoadingView(holder);
        View mLoadFailView = getLoadFailView(holder);
        View mLoadEndView = getLoadEndView(holder);

        switch (mLoadMoreStatus) {
            case LOAD_STATUS_LOADING:
                setVisibility(mLoadingView, View.VISIBLE);
                setVisibility(mLoadFailView, View.GONE);
                setVisibility(mLoadEndView, View.GONE);
                break;
            case LOAD_STATUS_FAIL:
                setVisibility(mLoadingView, View.GONE);
                setVisibility(mLoadFailView, View.VISIBLE);
                setVisibility(mLoadEndView, View.GONE);
                break;
            case LOAD_STATUS_END:
                setVisibility(mLoadingView, View.GONE);
                setVisibility(mLoadFailView, View.GONE);
                setVisibility(mLoadEndView, View.VISIBLE);
                break;
            case LOAD_STATUS_DEFAULT:
                setVisibility(mLoadingView, View.GONE);
                setVisibility(mLoadFailView, View.GONE);
                setVisibility(mLoadEndView, View.GONE);
                break;
        }
    }

    private void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }


    public void setLoadMoreStatus(int loadMoreStatus) {
        this.mLoadMoreStatus = loadMoreStatus;
    }

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public abstract int getLayoutId();

    public abstract View getLoadingView(ViewHolder holder);

    public abstract View getLoadFailView(ViewHolder holder);

    public abstract View getLoadEndView(ViewHolder holder);
}
