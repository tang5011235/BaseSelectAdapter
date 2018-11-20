package com.example.tang5.baserecyclerview;

public class TestBean implements ISelect {
    private boolean isSelected;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSeleted) {
        this.isSelected = isSeleted;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "isSelected=" + isSelected +
                ", data='" + data + '\'' +
                '}';
    }
}
