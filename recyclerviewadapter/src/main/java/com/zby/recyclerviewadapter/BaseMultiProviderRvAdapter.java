package com.zby.recyclerviewadapter;

import com.zby.recyclerviewadapter.entity.MultiItemEntity;

import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-08-06
 */
public class BaseMultiProviderRvAdapter<T extends MultiItemEntity, V extends ViewHolder> extends BaseProviderRvAdapter<T, V> {
    public BaseMultiProviderRvAdapter(List<T> mDataList) {
        super(mDataList);
    }

    @Override
    public int getViewType(T t) {
        return t.getItemType();
    }
}
