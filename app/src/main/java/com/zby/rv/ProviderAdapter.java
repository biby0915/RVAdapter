package com.zby.rv;

import com.zby.recyclerviewadapter.BaseProviderRvAdapter;

import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-08-06
 */
public class ProviderAdapter extends BaseProviderRvAdapter {
    public ProviderAdapter(List<Object> mDataList) {
        super(mDataList);
    }

    @Override
    public int getViewType(Object multiItemEntity) {
        return multiItemEntity instanceof Entity ? 1 : 2;
    }
}
