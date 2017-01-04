package com.hsl_4.mixviewsimple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hsl_4.mix.MixRecyclerViewAdapter;
import com.hsl_4.mix.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class GridActivity extends AppCompatActivity {

    private RecyclerView           recyclerView;
    private MixRecyclerViewAdapter adapter;
    private List<String>           list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        adapter = new MixRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.inject(String.class, new Simple());
        adapter.inject(HeaderBean.class, new Header());
        adapter.inject(FooterBean.class, new Footer());
        adapter.addHeader(new HeaderBean("头布局"));
        adapter.addFooter(new FooterBean("底布局"));
        list = new ArrayList<>();
        adapter.openLoadMore(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                list.clear();
                adapter.addSpanItem("继续插3",3);
                for (int i = 0; i < 10; i++) {
                    list.add("加载" + i);
                }
                adapter.addData(list);
                adapter.addSpanItem("继续插2",2);
            }
        });
        for (int i = 0; i < 20; i++) {
            list.add("第一次" + i);
        }
        adapter.setData(list);
        adapter.addSpanItem("查一下2",2);
    }
}
