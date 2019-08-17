package com.zby.recyclerviewadapter;

import com.zby.recyclerviewadapter.item.ViewProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-08-04
 */
public class BaseProviderRvAdapter<T, V extends ViewHolder> extends BaseRvAdapter<T, V> {
    private List<Class<?>> mRegisterClasses;
    private HashMap<Class<?>, ViewProvider> mProviders;

    public BaseProviderRvAdapter(List<T> mDataList) {
        super(mDataList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void convert(V holder, T data, int position) {
        mProviders.get(data.getClass()).convert(BaseProviderRvAdapter.this, holder, data, position);
    }

    public void registerProvider(Class clazz, ViewProvider provider) {
        if (mProviders == null) {
            mProviders = new HashMap<>();
        }
        if (mRegisterClasses == null) {
            mRegisterClasses = new ArrayList<>();
        }

        if (mRegisterClasses.contains(clazz)) {
            throw new IllegalArgumentException("can not add twice");
        }

        mRegisterClasses.add(clazz);
        mProviders.put(clazz, provider);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return mProviders.get(mRegisterClasses.get(viewType)).getLayoutResId();
    }

    @Override
    protected int getDataItemViewType(int position) {
        return mRegisterClasses.indexOf(getDataList().get(position).getClass());
    }
}
