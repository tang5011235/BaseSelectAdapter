package com.example.tang5.baserecyclerview;

import java.util.HashSet;
import java.util.Set;

public class SelectAdapterConfigeration {
	/**
	 * 选中条目的列表
	 */
	private Set<Integer> selectedList = new HashSet<>();
	/**
	 * 选中事件操作
	 */
	private OnSelectListener onSelectListener;
	/**
	 * 点击事件操作，先于执行刷新
	 */
	private OnTagClickListener onTagClickListener;
	/**
	 * 最大选中个数  -1表示全选  其他数值表示限制个数
	 */
	private double selectedMax = -1;
	/**
	 * 出发事件id
	 */
	private int selectViewId = -1;
	/**
	 * 当为当选的时候强行选择一个
	 */
	private boolean forceHasOne = true;
	/**
	 * e
	 * 未关联状态  ---点击item时候不将点击事件传递给checkbox 而是checkbox自身处理自己的事件
	 */
	private boolean isNotAssociate = true;

/*	private int layoutResId;
	private List<T> data;*/

	private SelectAdapterConfigeration(Builder builder) {
		onSelectListener = builder.onSelectListener;
		onTagClickListener = builder.onTagClickListener;
		selectedMax = builder.selectedMax;
		selectViewId = builder.selectViewId;
		forceHasOne = builder.forceHasOne;
		isNotAssociate = builder.isNotAssociate;
	}

	public interface OnSelectListener {
		void onSelected(Set<Integer> selectPosSet);
	}

	public interface OnTagClickListener<B extends BaseSelectAdapter2.SelecteViewHolder> {
		boolean onTagClick(B b, int position);
	}

	public Set<Integer> getSelectedList() {
		return selectedList;
	}

	public OnSelectListener getOnSelectListener() {
		return onSelectListener;
	}

	public OnTagClickListener getOnTagClickListener() {
		return onTagClickListener;
	}

	public double getSelectedMax() {
		return selectedMax;
	}

	public int getSelectViewId() {
		return selectViewId;
	}

	public boolean isForceHasOne() {
		return forceHasOne;
	}

	public boolean isNotAssociate() {
		return isNotAssociate;
	}


	public void setOnSelectListener(OnSelectListener onSelectListener) {
		this.onSelectListener = onSelectListener;
	}

	public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
		this.onTagClickListener = onTagClickListener;
	}

	public void setSelectedMax(double selectedMax) {
		this.selectedMax = selectedMax;
	}

	public void setSelectViewId(int selectViewId) {
		this.selectViewId = selectViewId;
	}

	public void setForceHasOne(boolean forceHasOne) {
		this.forceHasOne = forceHasOne;
	}

	public void setNotAssociate(boolean notAssociate) {
		isNotAssociate = notAssociate;
	}

	public static final class Builder {
		private OnSelectListener onSelectListener;
		private OnTagClickListener onTagClickListener;
		private double selectedMax;
		private int selectViewId;
		private boolean forceHasOne;
		private boolean isNotAssociate;

		public Builder() {
		}


		public Builder withOnSelectListener(OnSelectListener val) {
			onSelectListener = val;
			return this;
		}

		public Builder withOnTagClickListener(OnTagClickListener val) {
			onTagClickListener = val;
			return this;
		}

		public Builder withSelectedMax(double val) {
			selectedMax = val;
			return this;
		}

		public Builder withSelectViewId(int val) {
			selectViewId = val;
			return this;
		}

		public Builder withForceHasOne(boolean val) {
			forceHasOne = val;
			return this;
		}

		public Builder withIsNotAssociate(boolean val) {
			isNotAssociate = val;
			return this;
		}

		public <B extends BaseSelectAdapter2.SelecteViewHolder> SelectAdapterConfigeration but(OnSelectListener onSelectListener,
		                                                                                       OnTagClickListener<B> onTagClickListener,
		                                                                                       int selectedMax) {
			return this.withForceHasOne(true)
					.withIsNotAssociate(false)
					.withOnSelectListener(onSelectListener)
					.withOnTagClickListener(onTagClickListener)
					.withSelectedMax(selectedMax)
					.build();
		}

		public SelectAdapterConfigeration build() {
			return new SelectAdapterConfigeration(this);
		}
	}
}