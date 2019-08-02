package com.zby.recyclerviewadapter.loadmore;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ZhuBingYang
 * @date 2019-07-31
 */
public class LoadMoreAdapter extends RecyclerView.Adapter {

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        static final int STATUS_DEFAULT = 0;
        static final int STATUS_LOADING = 1;
        static final int STATUS_END = 3;
        static final int STATUS_FAIL = 2;
        private final View iconView;
        private final View loadingView;
        private final View endView = null;
        private int mLoadMoreStatus;
        private boolean mLoadMoreEndGone;

        private ObjectAnimator rotateAnimator;

        private LoadMoreViewHolder(View itemView) {
            super(itemView);
            loadingView = new View(itemView.getContext());
            iconView = new View(itemView.getContext());
            rotateAnimator = ObjectAnimator.ofFloat(iconView, View.ROTATION, 0, 720);
            rotateAnimator.setDuration(1000);
            rotateAnimator.setInterpolator(new FastOutSlowInInterpolator());
            rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        }

        void update() {
            switch (mLoadMoreStatus) {
                case STATUS_LOADING: {
                    loadingView.setVisibility(View.VISIBLE);
                    if (!rotateAnimator.isRunning()) {
                        rotateAnimator.cancel();
                        rotateAnimator.start();
                    }
                    break;
                }
                default: {
                    loadingView.setVisibility(View.GONE);
                    rotateAnimator.end();
                    break;
                }
            }
        }

//        static RecyclerView.ViewHolder getInstance(ViewGroup parent) {
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            return new LoadMoreViewHolder(inflater.inflate(R.layout.link_refresh_layout_loadmore_view, parent, false));
//        }

        boolean isLoadEndMoreGone() {
            //noinspection ConstantConditions
            return endView == null || mLoadMoreEndGone;
        }

        void setLoadMoreEndGone(boolean gone) {
            this.mLoadMoreEndGone = gone;
        }

        void setLoadMoreStatus(int mLoadMoreStatus) {
            this.mLoadMoreStatus = mLoadMoreStatus;
        }

        int getLoadMoreStatus() {
            return mLoadMoreStatus;
        }
    }

    private boolean mNextLoadEnable = false;
    private boolean mLoadMoreEnable = false;
    private boolean mLoading = false;
    private LoadMoreViewHolder mLoadMoreView;
    private boolean mEnableLoadMoreEndClick = false;

    private static final int LOADING_VIEW = 0x00000222;

    private RecyclerView.Adapter mInnerAdapter;
    private RequestLoadMoreListener mRequestLoadMoreListener;

    public LoadMoreAdapter(@NonNull RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;

//        mLoadMoreView = (LoadMoreViewHolder) LoadMoreViewHolder.getInstance(layout.getRecyclerView());
        mLoadMoreView.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreView.getLoadMoreStatus() == LoadMoreViewHolder.STATUS_FAIL) {
                    notifyLoadMoreToLoading();
                }
                if (mEnableLoadMoreEndClick && mLoadMoreView.getLoadMoreStatus() == LoadMoreViewHolder.STATUS_END) {
                    notifyLoadMoreToLoading();
                }
            }
        });

//        disableLoadMoreIfNotFullPage(layout.getRecyclerView());
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }


    private int mPreLoadNumber = 1;

    public void setRequestLoadMoreListener(RequestLoadMoreListener listener) {
        if (listener == null) {
            mRequestLoadMoreListener = null;
            mNextLoadEnable = false;
            mLoadMoreEnable = false;
            mLoading = false;
        } else {
            mRequestLoadMoreListener = listener;
            mNextLoadEnable = true;
            mLoadMoreEnable = true;
            mLoading = false;
        }
    }

    private void disableLoadMoreIfNotFullPage(RecyclerView recyclerView) {
        setEnableLoadMore(false);
        if (recyclerView == null) {
            return;
        }
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) {
            return;
        }
        if (manager instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFullScreen(linearLayoutManager)) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    int pos = getTheBiggestNumber(positions) + 1;
                    if (pos != getItemCount()) {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        }
    }

    private boolean isFullScreen(LinearLayoutManager llm) {
        return (llm.findLastCompletelyVisibleItemPosition() + 1) != getItemCount() ||
                llm.findFirstCompletelyVisibleItemPosition() != 0;
    }

    private int getTheBiggestNumber(int[] numbers) {
        int tmp = -1;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (int num : numbers) {
            if (num > tmp) {
                tmp = num;
            }
        }
        return tmp;
    }


    /**
     * Load more view count
     *
     * @return 0 or 1
     */
    public int getLoadMoreViewCount() {
        if (mRequestLoadMoreListener == null || !mLoadMoreEnable) {
            return 0;
        }
        if (!mNextLoadEnable && mLoadMoreView.isLoadEndMoreGone()) {
            return 0;
        }
        if (mInnerAdapter.getItemCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Gets to load more locations
     *
     * @return
     */
    public int getLoadMoreViewPosition() {
        return mInnerAdapter.getItemCount();
    }

    /**
     * @return Whether the Adapter is actively showing load
     * progress.
     */
    public boolean isLoading() {
        return mLoading;
    }


    /**
     * Refresh end, no more data
     */
    public void loadMoreEnd() {
        loadMoreEnd(false);
    }

    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    public void loadMoreEnd(boolean gone) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = false;
        mLoadMoreView.setLoadMoreEndGone(gone);
        if (gone) {
            notifyItemRemoved(getLoadMoreViewPosition());
        } else {
            mLoadMoreView.setLoadMoreStatus(LoadMoreViewHolder.STATUS_END);
            notifyItemChanged(getLoadMoreViewPosition());
        }
    }

    /**
     * Refresh complete
     */
    public void loadMoreComplete() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mNextLoadEnable = true;
        mLoadMoreView.setLoadMoreStatus(LoadMoreViewHolder.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Refresh failed
     */
    public void loadMoreFail() {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        mLoading = false;
        mLoadMoreView.setLoadMoreStatus(LoadMoreViewHolder.STATUS_FAIL);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Set the enabled state of load more.
     *
     * @param enable True if load more is enabled, false otherwise.
     */
    public void setEnableLoadMore(boolean enable) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnable = enable;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newLoadMoreCount == 1) {
                mLoadMoreView.setLoadMoreStatus(LoadMoreViewHolder.STATUS_DEFAULT);
                notifyItemInserted(getLoadMoreViewPosition());
            }
        }
    }

    /**
     * Returns the enabled status for load more.
     *
     * @return True if load more is enabled, false otherwise.
     */
    public boolean isLoadMoreEnable() {
        return mLoadMoreEnable;
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + getLoadMoreViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        int adapterCount = mInnerAdapter.getItemCount();
        if (position < adapterCount) {
            return super.getItemViewType(position);
        } else {
            return LOADING_VIEW;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOADING_VIEW) {
            return mLoadMoreView;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }


    /**
     * The notification starts the callback and loads more
     */
    private void notifyLoadMoreToLoading() {
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreViewHolder.STATUS_LOADING) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreViewHolder.STATUS_DEFAULT);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * Load more without data when settings are clicked loaded
     *
     * @param enable
     */
    public void enableLoadMoreEndClick(boolean enable) {
        mEnableLoadMoreEndClick = enable;
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     * {@link #setFullSpan(RecyclerView.ViewHolder)}
     *
     * @param holder
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == LOADING_VIEW) {
            setFullSpan(holder);
        }
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    private void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    private SpanSizeLookup mSpanSizeLookup;

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    /**
     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (mSpanSizeLookup == null) {
                        return isFixedViewType(type) ? gridManager.getSpanCount() : 1;
                    } else {
                        return (isFixedViewType(type)) ? gridManager.getSpanCount() : mSpanSizeLookup.getSpanSize(gridManager, position);
                    }
                }
            });
        }
    }

    private boolean isFixedViewType(int type) {
        return type == LOADING_VIEW;
    }


    /**
     * To bind different types of holder and solve different the bind events
     *
     * @param holder
     * @param position
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Do not move position, need to change before LoadMoreView binding
        autoLoadMore(position);
        if (holder.getItemViewType() == LOADING_VIEW) {
            mLoadMoreView.update();
        } else {
            mInnerAdapter.onBindViewHolder(holder, position);
        }
    }

    private void autoLoadMore(int position) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        if (position < getItemCount() - mPreLoadNumber) {
            return;
        }
        if (mLoadMoreView.getLoadMoreStatus() != LoadMoreViewHolder.STATUS_DEFAULT) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreViewHolder.STATUS_LOADING);
        if (!mLoading) {
            mLoading = true;
            mRequestLoadMoreListener.onLoadMoreRequested();
        }
    }

    public interface RequestLoadMoreListener {
        void onLoadMoreRequested();
    }

    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (mRequestLoadMoreListener != null) {
                mNextLoadEnable = true;
                mLoadMoreEnable = true;
                mLoading = false;
                mLoadMoreView.setLoadMoreStatus(LoadMoreViewHolder.STATUS_DEFAULT);
            }
            LoadMoreAdapter.this.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            LoadMoreAdapter.this.notifyItemRangeChanged(positionStart, itemCount);

        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            LoadMoreAdapter.this.notifyItemRangeChanged(positionStart, itemCount, payload);

        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            LoadMoreAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            LoadMoreAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);

        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            LoadMoreAdapter.this.notifyItemMoved(fromPosition, toPosition);
        }
    };
}