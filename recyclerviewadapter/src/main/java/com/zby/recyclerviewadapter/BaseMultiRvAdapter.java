package com.zby.recyclerviewadapter;

import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;

import com.zby.recyclerviewadapter.entity.MultiItemEntity;

import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-08-04
 */
public abstract class BaseMultiRvAdapter<T extends MultiItemEntity, V extends ViewHolder> extends BaseRvAdapter<T, V> {

    private SparseIntArray mLayouts;

    public BaseMultiRvAdapter(List<T> dataList) {
        super(dataList);
    }


    public void addItemType(int viewType, @LayoutRes int layoutResId) {
        if (mLayouts == null) {
            mLayouts = new SparseIntArray();
        }

        mLayouts.put(viewType, layoutResId);
    }

    @Override
    protected int getDataItemViewType(int position) {
        T item = getDataList().get(position);
        if (item != null) {
            return item.getItemType();
        }
        return super.getDataItemViewType(position);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return mLayouts.get(viewType);
    }
}
