package com.zby.recyclerviewadapter;

import android.util.SparseArray;

import com.zby.recyclerviewadapter.item.ViewProvider;

import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-08-04
 */
public abstract class BaseProviderRvAdapter<T, V extends ViewHolder> extends BaseRvAdapter<T, V> {
    private SparseArray<ViewProvider> mProviders;

    public BaseProviderRvAdapter(List<T> mDataList) {
        super(mDataList);
    }

    @Override
    public void convert(V holder, T data, int position) {
        mProviders.get(holder.getItemViewType()).convert(BaseProviderRvAdapter.this, holder, data, position);
    }

    public void registerProvider(ViewProvider provider) {
        if (mProviders == null) {
            mProviders = new SparseArray<>();
        }

        mProviders.put(provider.getViewType(), provider);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return mProviders.get(viewType).getLayoutResId();
    }

    @Override
    protected int getDataItemViewType(int position) {
        return getViewType(getDataList().get(position));
    }

    public abstract int getViewType(T t);
}
