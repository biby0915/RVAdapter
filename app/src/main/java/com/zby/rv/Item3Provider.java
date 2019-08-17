package com.zby.rv;

import com.zby.recyclerviewadapter.BaseRvAdapter;
import com.zby.recyclerviewadapter.ViewHolder;
import com.zby.recyclerviewadapter.item.ViewProvider;

/**
 * @author ZhuBingYang
 * @date 2019-08-16
 */
public class Item3Provider implements ViewProvider {
    @Override
    public int getLayoutResId() {
        return R.layout.entity1;
    }

    @Override
    public void convert(BaseRvAdapter adapter, ViewHolder holder, Object data, int position) {
        holder.setText(R.id.tv, position + " item3");
    }
}
