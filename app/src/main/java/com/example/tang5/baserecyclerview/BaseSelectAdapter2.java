package com.example.tang5.baserecyclerview;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author thf
 * 获取数据
 */
public abstract class BaseSelectAdapter2<T, B extends BaseSelectAdapter2.SelecteViewHolder> extends BaseQuickAdapter<T, B> {
	private SelectAdapterConfigeration mConfigeration;
	/**
	 * 出发事件id
	 */
	private int selectViewId = -1;
	/**
	 * 当为当选的时候强行选择一个
	 */
	private boolean forceHasOne = true;
	/**
	 * 未关联状态  ---点击item时候不将点击事件传递给checkbox 而是checkbox自身处理自己的事件
	 */
	private boolean isNotAssociate = true;

	public BaseSelectAdapter2(int layoutResId, @Nullable List<T> data, SelectAdapterConfigeration configeration) {
		super(layoutResId, data);
		mConfigeration = configeration;
		//设置默认值
		if (configeration.getSelectViewId() != 0) {
			this.selectViewId = configeration.getSelectViewId();
		} else {
			this.selectViewId = R.id.checkbox;
		}
	}

	public BaseSelectAdapter2(int layoutResId, @Nullable List<T> data) {
		this(layoutResId, data, new SelectAdapterConfigeration.Builder().but(null, null, 3));
	}

	@Override
	public B onCreateViewHolder(ViewGroup parent, int viewType) {
		B helper = super.onCreateViewHolder(parent, viewType);
		helper.setCheckBoxId(selectViewId);
		return helper;
	}

	@Override
	protected void convert(final B helper, T item) {
		Log.e(TAG, "convert: " + System.currentTimeMillis());
		if (mConfigeration.getSelectedList().contains(helper.getAdapterPosition())) {
			helper.setChecked(selectViewId, true);
		} else {
			helper.setChecked(selectViewId, false);
		}
		if (isNotAssociate) {
			helper.mCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//执行点击事件
					if (mConfigeration.getOnTagClickListener() != null) {
						mConfigeration.getOnTagClickListener().onTagClick(helper, helper.getAdapterPosition());
					}
					doSelect(helper, helper.mCheckBox, helper.getAdapterPosition());
				}
			});
		} else {
			helper.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//执行点击事件
					if (mConfigeration.getOnTagClickListener() != null) {
						mConfigeration.getOnTagClickListener().onTagClick(helper, helper.getAdapterPosition());
					}
					doSelect(helper, helper.mCheckBox, helper.getAdapterPosition());
				}
			});
		}

		//用户自定义操作
		customerConver(helper, item);
	}


	/**
	 * 选中操作逻辑
	 *
	 * @param helper
	 * @param child
	 * @param position
	 */
	private void doSelect(B helper, CheckBox child, int position) {
		boolean isChecked = !child.isChecked();
		if (isNotAssociate) {
			//如果为未关联状态需要改变checked状态 当checkbox从以种状态过度到另一种状态的时候是相反的
			isChecked = !isChecked;
		}
		if (isChecked) {
			//处理max_select=1的情况
			if (mConfigeration.getSelectedMax() == 1 && mConfigeration.getSelectedList().size() == 1) {
				Iterator<Integer> iterator = mConfigeration.getSelectedList().iterator();
				Integer preIndex = iterator.next();

				setChildChecked(helper, position, child);
				mConfigeration.getSelectedList().remove(preIndex);
				mConfigeration.getSelectedList().add(position);
				//内部通过handle刷新
				notifyItemChanged(preIndex);
				Log.e(TAG, "doSelect: " + System.currentTimeMillis());
			} else {
				if (mConfigeration.getSelectedMax() > 0 && mConfigeration.getSelectedList().size() >= mConfigeration.getSelectedMax()) {
					//当操作限制范围的时候 将超出的设置为不选中状态
					setChildUnChecked(helper, position, child);
					return;
				}
				setChildChecked(helper, position, child);
				mConfigeration.getSelectedList().add(position);
			}
		} else {
			//取消选中按钮逻辑
			if (forceHasOne && mConfigeration.getSelectedMax() == 1) {

			} else {
				setChildUnChecked(helper, position, child);
				mConfigeration.getSelectedList().remove(position);
			}
		}
	}

	/**
	 * 设置未选中状态
	 *
	 * @param helper
	 * @param position
	 * @param view
	 */
	private void setChildUnChecked(B helper, int position, View view) {
		helper.mCheckBox.setChecked(false);
		unSelected(helper, position, view);
	}

	/**
	 * 设置选中状态
	 *
	 * @param helper
	 * @param position
	 * @param view
	 */
	private void setChildChecked(B helper, int position, View view) {
		helper.mCheckBox.setChecked(true);
		onSelected(helper, position, view);
		if (mConfigeration.getOnSelectListener() != null) {
			mConfigeration.getOnSelectListener().onSelected(mConfigeration.getSelectedList());
		}
	}

	public abstract void customerConver(final BaseViewHolder helper, T item);

	/**
	 * 当选中时候调用  用于改变ui状态或则做其他操作
	 *
	 * @param helper
	 * @param position
	 * @param view
	 */
	public void onSelected(B helper, int position, View view) {
		Log.d("thf", "onSelected " + position);
		Log.d("thf", "onSelectedViews " + mConfigeration.getSelectedList().toString() + "," + position);
	}

	/**
	 * 当未选中时候调用  用于改变ui状态或则做其他操作
	 *
	 * @param helper
	 * @param position
	 * @param view
	 */
	public void unSelected(B helper, int position, View view) {
		Log.d("thf", "unSelected " + position);
	}

	/**
	 * 设置选中的list 可以通过该方法设置默认选中的条目
	 *
	 * @param poses
	 */
	public void setSelectedList(int... poses) {
		Set<Integer> set = new HashSet<>();
		for (int pos : poses) {
			set.add(pos);
		}
		setSelectedList(set);
	}

	/**
	 * 设置选中的list 可以通过该方法设置默认选中的条目
	 *
	 * @param set
	 */
	public void setSelectedList(Set<Integer> set) {
		mConfigeration.getSelectedList().clear();
		if (set != null) {
			mConfigeration.getSelectedList().addAll(set);
		}
		notifyDataSetChanged();
	}


	/**
	 * 获取被选中的条目
	 *
	 * @return 返回选中的条目
	 */
	public Set<Integer> getSelectedList() {
		return mConfigeration.getSelectedList();
	}


	/**
	 * 全选
	 */
	public void selectAll() {
		mConfigeration.getSelectedList().clear();
		for (int i = 0; i < mData.size(); i++) {
			mConfigeration.getSelectedList().add(i);
		}
		notifyDataSetChanged();
	}

	/**
	 * 全部不选
	 */
	public void unSelectAll() {
		mConfigeration.getSelectedList().clear();
		notifyDataSetChanged();
	}

	/**
	 * 反选
	 */
	public void inverseSelection() {
		HashSet<Integer> copySet = new HashSet<>();
		for (int i = 0; i < mData.size(); i++) {
			if (!mConfigeration.getSelectedList().contains(i)) {
				copySet.add(i);
			}
		}
		mConfigeration.getSelectedList().clear();
		mConfigeration.getSelectedList().addAll(copySet);
		notifyDataSetChanged();
	}


	static class SelecteViewHolder extends BaseViewHolder {
		public CheckBox mCheckBox;
		private int mCheckBoxId;

		public SelecteViewHolder(View view) {
			super(view);
		}

		public void setCheckBoxId(int checkBoxId) {
			mCheckBoxId = checkBoxId;
			mCheckBox = (CheckBox) itemView.findViewById(checkBoxId);
		}
	}

}
