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
public abstract class BaseSelectAdapter<T, B extends BaseSelectAdapter.SelecteViewHolder> extends BaseQuickAdapter<T, B> {
	/**
	 * 选中条目的列表
	 */
	private Set<Integer> mSelectedList = new HashSet<>();
	/**
	 * 选中事件操作
	 */
	private OnSelectListener mOnSelectListener;
	/**
	 * 点击事件操作，先于执行刷新
	 */
	private OnTagClickListener mOnTagClickListener;
	/**
	 * 最大选中个数  -1表示全选  其他数值表示限制个数
	 */
	private double mSelectedMax = -1;
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

	public interface OnSelectListener {
		void onSelected(Set<Integer> selectPosSet);
	}

	public interface OnTagClickListener<B extends BaseSelectAdapter.SelecteViewHolder> {
		boolean onTagClick(B b, int position);
	}


	public BaseSelectAdapter(int layoutResId, @Nullable List<T> data, int selectViewId) {
		super(layoutResId, data);

		//设置默认值
		if (selectViewId != 0) {
			this.selectViewId = selectViewId;
		} else {
			this.selectViewId = R.id.checkbox;
		}
	}

	public BaseSelectAdapter(int layoutResId, @Nullable List<T> data) {
		this(layoutResId, data, 0);
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
		if (mSelectedList.contains(helper.getAdapterPosition())) {
			helper.setChecked(selectViewId, true);
		} else {
			helper.setChecked(selectViewId, false);
		}
		if (isNotAssociate) {
			helper.mCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//执行点击事件
					if (mOnTagClickListener != null) {
						mOnTagClickListener.onTagClick(helper, helper.getAdapterPosition());
					}
					doSelect(helper, helper.mCheckBox, helper.getAdapterPosition());
				}
			});
		} else {
			helper.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//执行点击事件
					if (mOnTagClickListener != null) {
						mOnTagClickListener.onTagClick(helper, helper.getAdapterPosition());
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
			if (mSelectedMax == 1 && mSelectedList.size() == 1) {
				Iterator<Integer> iterator = mSelectedList.iterator();
				Integer preIndex = iterator.next();

				setChildChecked(helper, position, child);
				mSelectedList.remove(preIndex);
				mSelectedList.add(position);
				//内部通过handle刷新
				notifyItemChanged(preIndex);
				Log.e(TAG, "doSelect: " + System.currentTimeMillis());
			} else {
				if (mSelectedMax > 0 && mSelectedList.size() >= mSelectedMax) {
					//当操作限制范围的时候 将超出的设置为不选中状态
					setChildUnChecked(helper, position, child);
					return;
				}
				setChildChecked(helper, position, child);
				mSelectedList.add(position);
			}
		} else {
			//取消选中按钮逻辑
			if (forceHasOne && mSelectedMax == 1) {
				
			}else {
				setChildUnChecked(helper, position, child);
				mSelectedList.remove(position);
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
		if (mOnSelectListener != null) {
			mOnSelectListener.onSelected(mSelectedList);
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
		Log.d("thf", "onSelectedViews " + mSelectedList.toString() + "," + position);
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
		mSelectedList.clear();
		if (set != null) {
			mSelectedList.addAll(set);
		}
		notifyDataSetChanged();
	}


	/**
	 * 设置最大选中条目
	 *
	 * @param selectedMax
	 */
	public void setSelectedMax(double selectedMax) {
		mSelectedMax = selectedMax;
	}

	/**
	 * 获取被选中的条目
	 *
	 * @return 返回选中的条目
	 */
	public Set<Integer> getSelectedList() {
		return mSelectedList;
	}


	/**
	 * 设置选中监听事件
	 *
	 * @param onSelectListener
	 */
	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}


	/**
	 * 设置条目点击回调事件
	 *
	 * @param onTagClickListener
	 */
	public void setOnTagClickListener(OnTagClickListener<B> onTagClickListener) {
		mOnTagClickListener = onTagClickListener;
	}

	/**
	 * 全选
	 */
	public void selectAll() {
		mSelectedList.clear();
		for (int i = 0; i < mData.size(); i++) {
			mSelectedList.add(i);
		}
		notifyDataSetChanged();
	}

	/**
	 * 全部不选
	 */
	public void unSelectAll() {
		mSelectedList.clear();
		notifyDataSetChanged();
	}

	/**
	 * 反选
	 */
	public void inverseSelection() {
		HashSet<Integer> copySet = new HashSet<>();
		for (int i = 0; i < mData.size(); i++) {
			if (!mSelectedList.contains(i)) {
				copySet.add(i);
			}
		}
		mSelectedList.clear();
		mSelectedList.addAll(copySet);
		notifyDataSetChanged();
	}

	public void setForceHasOne(boolean forceHasOne) {
		this.forceHasOne = forceHasOne;
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

	public void setNotAssociate(boolean notAssociate) {
		isNotAssociate = notAssociate;
	}
}
