package com.zby.recyclerviewadapter.listener;

/**
 * @author ZhuBingYang
 * @date 2019-08-04
 */
public interface OnLoadMoreListener {
    boolean requestLoadMore(boolean retryWhenFailedTapped);
}
