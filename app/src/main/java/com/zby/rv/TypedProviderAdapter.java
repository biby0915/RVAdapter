package com.zby.rv;

import com.zby.recyclerviewadapter.BaseTypedProviderRvAdapter;

import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-08-06
 */
public class TypedProviderAdapter extends BaseTypedProviderRvAdapter {
    public TypedProviderAdapter(List<Object> mDataList) {
        super(mDataList);
    }

    @Override
    public int getViewType(Object multiItemEntity) {
        return multiItemEntity instanceof Entity ? 1 : 2;
    }
}
