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
	private MyAdapter2 mAdapter2;
	private SelectAdapterConfigeration mConfigeration;

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
		mConfigeration = new SelectAdapterConfigeration.Builder()
				.withSelectedMax(1)
				.withForceHasOne(false)
				.withIsNotAssociate(false)
				.build();
		mAdapter2 = new MyAdapter2(R.layout.adapter_item_base_single, mData, mConfigeration);
		mAdapter2.bindToRecyclerView(recyclerView);
		/*mLinearLayout = findViewById(R.id.ll);
		mAdapter = new MyAdapter(R.layout.adapter_item_base_single, mData);
		mAdapter.setSelectedMax(1);
		mAdapter.setSelectedList(1);
		mAdapter.setForceHasOne(true);
		mAdapter.setNotAssociate(false);
		mAdapter.bindToRecyclerView(recyclerView);*/
	}


	public void select_all(View view) {
		mAdapter2.selectAll();
	}

	public void unselect_all(View view) {
		mAdapter2.unSelectAll();
	}

	public void inverse_selection(View view) {
		mAdapter2.inverseSelection();
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
				mAdapter2.unSelectAll();
				mConfigeration.setSelectedMax(1);
				mLinearLayout.setVisibility(View.GONE);
				break;
			case R.id.more:
				mAdapter2.unSelectAll();
				mConfigeration.setSelectedMax(-1);
				mLinearLayout.setVisibility(View.VISIBLE);
				break;
			case R.id.limit:
				mAdapter2.unSelectAll();
				mConfigeration.setSelectedMax(3);
				mLinearLayout.setVisibility(View.GONE);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
