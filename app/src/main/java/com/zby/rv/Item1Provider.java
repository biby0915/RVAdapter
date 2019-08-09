package com.zby.rv;

import com.zby.recyclerviewadapter.BaseRvAdapter;
import com.zby.recyclerviewadapter.ViewHolder;
import com.zby.recyclerviewadapter.entity.MultiItemEntity;
import com.zby.recyclerviewadapter.item.ViewProvider;

/**
 * @author ZhuBingYang
 * @date 2019-08-05
 */
public class Item1Provider implements ViewProvider<MultiItemEntity, ViewHolder> {
    @Override
    public int getLayoutResId() {
        return R.layout.entity1;
    }

    @Override
    public int getViewType() {
        return 1;
    }

    @Override
    public void convert(BaseRvAdapter adapter, ViewHolder holder, MultiItemEntity data, int position) {
        holder.setText(R.id.tv, position + " item1");
    }
}
