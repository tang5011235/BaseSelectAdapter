package com.example.tang5.baserecyclerview;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MyAdapter extends BaseSelectAdapter<TestBean> {
    public MyAdapter(int layoutResId, @Nullable List<TestBean> data, int selectViewId) {
        super(layoutResId, data, selectViewId);
    }

    @Override
    public void customerConver(final BaseViewHolder helper, final TestBean item) {
        final EditText edittext = helper.getView(R.id.tv);
       /* helper.setText(R.id.tv, new StringBuilder("第").append(item.getData())
                .append("条数据"));*/
        TextWatcher textWatcher;
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setData(s.toString());
                for (TestBean datum : mData) {
                    Log.e(TAG, "afterTextChanged: " + datum.getData());
                }
            }
        };

        if (edittext.getTag() != null && edittext.getTag() instanceof TextWatcher) {
            edittext.removeTextChangedListener((TextWatcher) edittext.getTag());
        }
        edittext.addTextChangedListener(textWatcher);
        helper.setText(R.id.tv, item.getData() + "");
        edittext.setTag(textWatcher);
    }

}
