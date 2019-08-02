package com.zby.rv;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.zby.recyclerviewadapter.BaseRvAdapter;
import com.zby.recyclerviewadapter.ViewHolder;

import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-08-01
 */
public class Adapter extends BaseRvAdapter<String, ViewHolder> {
    public Adapter(int layoutId, List dataList) {
        super(layoutId, dataList);
    }

    @Override
    public void convert(ViewHolder holder, String data, int position) {
        TextView tv = holder.getView(R.id.tv);
        tv.setText("wertyuiop");
        ImageView iv = holder.getView(R.id.image);
        iv.setBackgroundColor(Color.MAGENTA);
    }
}
