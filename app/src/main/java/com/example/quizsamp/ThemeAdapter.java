package com.example.quizsamp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ThemeAdapter extends BaseAdapter {
    private List<String> themeList;

    public ThemeAdapter(List<String> themeList) {
        this.themeList = themeList;
    }

    @Override
    public int getCount()
    {
        return themeList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_item, parent, false);
        } else {
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.themeName)).setText(themeList.get(position));

        return view;
    }
}
