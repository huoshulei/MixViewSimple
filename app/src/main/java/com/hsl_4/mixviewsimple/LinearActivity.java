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

public class LinearActivity extends AppCompatActivity {

    private RecyclerView           recyclerView;
    private MixRecyclerViewAdapter adapter;
    private List<String>           list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liner);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MixRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.inject(String.class, new Simple());
        adapter.inject(HeaderBean.class, new Header());
        adapter.inject(FooterBean.class, new Footer());
        adapter.addHeader(new HeaderBean("头"));
        adapter.addFooter(new FooterBean("低布局"));
        list = new ArrayList<>();
        adapter.openLoadMore(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                list.clear();
                adapter.addData("继续插");
                for (int i = 0; i < 10; i++) {
                    list.add("加载" + i);
                }
                adapter.addData(list);
                adapter.addData("继续插");
            }
        });
        for (int i = 0; i < 20; i++) {
            list.add("首次" + i);
        }
        adapter.setData(list);
        adapter.addData("插一下");
    }
}
