package com.example.tang5.baserecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<TestBean> mData = new ArrayList<>();
    private BaseSelectAdapter mAdapter;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SelectAdapter");
        setSupportActionBar(toolbar);
        for (int i = 0; i < 50; i++) {
            TestBean testBean = new TestBean();
            testBean.setData(i + "");
            mData.add(testBean);
        }
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mLinearLayout = findViewById(R.id.ll);
        mAdapter = new MyAdapter(R.layout.adapter_item_base_single, mData, R.id.checkbox);
        mAdapter.setSelectedMax(-1);
        mAdapter.bindToRecyclerView(recyclerView);
    }


    public void select_all(View view) {
        mAdapter.selectAll();
    }

    public void unselect_all(View view) {
        mAdapter.unSelectAll();
    }

    public void inverse_selection(View view) {
        mAdapter.inverseSelection();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.single:
                mAdapter.unSelectAll();
                mAdapter.setSelectedMax(1);
                mLinearLayout.setVisibility(View.GONE);
                break;
            case R.id.more:
                mAdapter.unSelectAll();
                mAdapter.setSelectedMax(-1);
                mLinearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.limit:
                mAdapter.unSelectAll();
                mAdapter.setSelectedMax(3);
                mLinearLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
