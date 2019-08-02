package com.zby.rv;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zby.recyclerviewadapter.BaseRvAdapter;
import com.zby.recyclerviewadapter.listener.OnItemChildClickListener;
import com.zby.recyclerviewadapter.listener.OnItemClickListener;
import com.zby.recyclerviewadapter.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 20; i++) {
            list.add("");
        }

        RecyclerView rv = findViewById(R.id.rv);


        Adapter adapter = new Adapter(R.layout.item, list);
        View view = new View(getApplicationContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        view.setBackgroundColor(Color.CYAN);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.RED);
            }
        });
        adapter.setHeaderView(view);

        View v = new View(getApplicationContext());
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        v.setBackgroundColor(Color.GRAY);
        adapter.setEmptyLayout(v);

        View f = new View(getApplicationContext());
        f.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        f.setBackgroundColor(Color.BLUE);
        adapter.setFooterView(f);

        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRvAdapter adapter, View v, int position) {
                System.out.println(2 + "  " + position);
            }
        });

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseRvAdapter adapter, View view, int position) {
                System.out.println(3 + "  " + position);
                return true;
            }
        });

        adapter.addOnItemChildClickListener(R.id.tv, new OnItemChildClickListener() {
            @Override
            public void onItemClick(BaseRvAdapter adapter, View v, int position) {
                System.out.println("child" + "  " + position + "  " + v);
            }
        });

        adapter.addOnItemChildClickListener(R.id.image, new OnItemChildClickListener() {
            @Override
            public void onItemClick(BaseRvAdapter adapter, View v, int position) {
                System.out.println("child" + "  " + position + "  " + v);
            }
        });
    }
}
