package com.zby.rv;

import com.zby.recyclerviewadapter.BaseMultiRvAdapter;
import com.zby.recyclerviewadapter.ViewHolder;
import com.zby.recyclerviewadapter.entity.MultiItemEntity;

import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-08-05
 */
public class MultiAdapter extends BaseMultiRvAdapter<MultiItemEntity, ViewHolder> {
    public MultiAdapter(List<MultiItemEntity> dataList) {
        super(dataList);
        addItemType(1, R.layout.entity1);
        addItemType(2, R.layout.entity2);
    }

    @Override
    public void convert(ViewHolder holder, MultiItemEntity data, int position) {

    }
}
