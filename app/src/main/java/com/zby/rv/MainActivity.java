package com.zby.rv;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zby.recyclerviewadapter.BaseMultiTypedProviderRvAdapter;
import com.zby.recyclerviewadapter.BaseProviderRvAdapter;
import com.zby.recyclerviewadapter.BaseTypedProviderRvAdapter;
import com.zby.recyclerviewadapter.BaseRvAdapter;
import com.zby.recyclerviewadapter.ViewHolder;
import com.zby.recyclerviewadapter.decoration.IInsetColorDecoration;
import com.zby.recyclerviewadapter.decoration.InsetColorItemDecoration;
import com.zby.recyclerviewadapter.decoration.InsetValue;
import com.zby.recyclerviewadapter.entity.MultiItemEntity;
import com.zby.recyclerviewadapter.listener.OnItemChildClickListener;
import com.zby.recyclerviewadapter.listener.OnItemClickListener;
import com.zby.recyclerviewadapter.listener.OnItemLongClickListener;
import com.zby.recyclerviewadapter.listener.OnLoadMoreListener;
import com.zby.recyclerviewadapter.listener.OnScrollReachBottomListener;

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


        final Adapter adapter = new Adapter(R.layout.item, list);
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

//        rv.setAdapter(adapter);

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
            public void onItemChildClick(BaseRvAdapter adapter, View v, int position) {
                System.out.println("child" + "  " + position + "  " + v);
            }
        });

        adapter.addOnItemChildClickListener(R.id.image, new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseRvAdapter adapter, View v, int position) {
                System.out.println("child" + "  " + position + "  " + v);
            }
        });


        final List<MultiItemEntity> entities = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            entities.add(new Entity());
            entities.add(new Entity2());
        }

        MultiAdapter multiAdapter = new MultiAdapter(entities);
        multiAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRvAdapter adapter, View v, int position) {
                System.out.println(position + ":  " + ((TextView) v.findViewById(R.id.tv)).getText().toString());
            }
        });


        BaseTypedProviderRvAdapter providerRvAdapter = new BaseMultiTypedProviderRvAdapter<MultiItemEntity, ViewHolder>(entities);
        providerRvAdapter.registerProvider(new Item1Provider());
        providerRvAdapter.registerProvider(new Item2Provider());
        providerRvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRvAdapter adapter, View v, int position) {
                System.out.println(position);
            }
        });


        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean requestLoadMore(boolean retryWhenFailedTapped) {
                System.out.println("requestLoadMore  " + retryWhenFailedTapped);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            list.add("");
                        }
                        adapter.notifyDataSetChanged();
                        adapter.loadMoreEnd();
                    }
                }, 3000);
                return true;
            }
        });
//        rv.setAdapter(providerRvAdapter);
        providerRvAdapter.setScrollReachBottomOffset(3);
        providerRvAdapter.setOnScrollReachBottomListener(new OnScrollReachBottomListener() {
            @Override
            public void onBottomReached() {
                System.out.println("onBottomReached");
            }
        });

        rv.addItemDecoration(new InsetColorItemDecoration(Color.BLACK, new IInsetColorDecoration() {
            @Override
            public InsetValue getInset(int position, InsetValue value) {
                value.left = 60;
                value.right = 20;
                return value;
            }

            @Override
            public int getHeight(int position) {
                if (position == 0) {
                    return 0;
                }
                return 10;
            }

            @Override
            public int getColor(int position) {
                return Color.MAGENTA;
            }
        }));


        List pro = new ArrayList();
        for (int i = 0; i < 10; i++) {
            pro.add(new Entity());
            pro.add(new Entity2());
            pro.add(new Entity3());
        }
        BaseProviderRvAdapter baseProviderRvAdapter = new BaseProviderRvAdapter(pro);
        baseProviderRvAdapter.registerProvider(Entity.class, new Item1Provider());
        baseProviderRvAdapter.registerProvider(Entity2.class, new Item2Provider());
        baseProviderRvAdapter.registerProvider(Entity3.class, new Item3Provider());
        rv.setAdapter(baseProviderRvAdapter);
    }
}
