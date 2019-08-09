package com.zby.rv;

import com.zby.recyclerviewadapter.entity.MultiItemEntity;

/**
 * @author ZhuBingYang
 * @date 2019-08-05
 */
public class Entity implements MultiItemEntity {
    @Override
    public int getItemType() {
        return 1;
    }
}
