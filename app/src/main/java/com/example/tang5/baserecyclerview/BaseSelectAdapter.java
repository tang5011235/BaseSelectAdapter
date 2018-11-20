package com.example.tang5.baserecyclerview;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 获取数据
 */
public abstract class BaseSelectAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    private OnSelectListener mOnSelectListener;
    private Set<Integer> mSelectedView = new HashSet<>();

    private double mSelectedMax = -1;
    private int selectViewId = -1;

    public BaseSelectAdapter(int layoutResId, @Nullable List<T> data, @IntRange(from = 1) int selectViewId) {
        super(layoutResId, data);
        this.selectViewId = selectViewId;
    }

    @Override
    protected void convert(final BaseViewHolder helper, T item) {
        if (mSelectedView.contains(helper.getAdapterPosition())) {
            helper.setChecked(selectViewId, true);
        } else {
            helper.setChecked(selectViewId, false);
        }

        helper.getView(R.id.ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSelect((CheckBox) v.findViewById(R.id.checkbox), helper.getAdapterPosition());
            }
        });

        //用户自定义操作
        customerConver(helper, item);
    }

    private void doSelect(CheckBox child, int position) {
        if (!child.isChecked()) {
            //处理max_select=1的情况
            if (mSelectedMax == 1 && mSelectedView.size() == 1) {
                Iterator<Integer> iterator = mSelectedView.iterator();
                Integer preIndex = iterator.next();
                notifyItemChanged(preIndex);
                setChildChecked(position, child);

                mSelectedView.remove(preIndex);
                mSelectedView.add(position);
            } else {
                if (mSelectedMax > 0 && mSelectedView.size() >= mSelectedMax) {
                    return;
                }
                setChildChecked(position, child);
                mSelectedView.add(position);
            }
        } else {
            setChildUnChecked(position, child);
            mSelectedView.remove(position);
        }
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelected(new HashSet<Integer>(mSelectedView));
        }
    }

    private void setChildUnChecked(int position, CheckBox view) {
        view.setChecked(false);
        unSelected(position, view);
    }

    private void setChildChecked(int position, CheckBox view) {
        view.setChecked(true);
        onSelected(position, view);
    }

    public abstract void customerConver(final BaseViewHolder helper, T item);

    public void onSelected(int position, View view) {
        Log.d("thf", "onSelected " + position);
        Log.d("thf", "onSelectedViews " + mSelectedView.toString() + "," + position);
    }

    public void unSelected(int position, View view) {
        Log.d("thf", "unSelected " + position);
    }

    public void setSelectedList(int... poses) {
        Set<Integer> set = new HashSet<>();
        for (int pos : poses) {
            set.add(pos);
        }
        setSelectedList(set);
    }

    public void setSelectedList(Set<Integer> set) {
        mSelectedView.clear();
        if (set != null) {
            mSelectedView.addAll(set);
        }
        notifyDataSetChanged();
    }

    public void setSelectedMax(double selectedMax) {
        mSelectedMax = selectedMax;
    }

    public Set<Integer> getSelectedView() {
        return mSelectedView;
    }

    public interface OnSelectListener {
        void onSelected(Set<Integer> selectPosSet);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    /**
     * 全选
     */
    public void selectAll() {
        mSelectedView.clear();
        for (int i = 0; i < mData.size(); i++) {
            mSelectedView.add(i);
        }
        notifyDataSetChanged();
    }

    /**
     * 全部不
     */
    public void unSelectAll() {
        mSelectedView.clear();
        notifyDataSetChanged();
    }

    public void inverseSelection() {
        HashSet<Integer> copySet = new HashSet<>();
        for (int i = 0; i < mData.size(); i++) {
            if (!mSelectedView.contains(i)) {
                copySet.add(i);
            }
        }
        mSelectedView.clear();
        mSelectedView.addAll(copySet);
        notifyDataSetChanged();
    }
}
