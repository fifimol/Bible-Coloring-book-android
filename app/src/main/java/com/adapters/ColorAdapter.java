package com.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.biblestory.color.R;
import com.utils.Constants;

public class ColorAdapter extends BaseAdapter {
    Activity context;
    private LayoutInflater inflater;
    private int selected;

    static class ViewHolder {
        ImageView image;
        View view;

        ViewHolder() {
        }
    }

    public ColorAdapter(Activity context) {
        this.context = context;
        this.inflater =LayoutInflater.from (context);
    }

    @SuppressLint("WrongConstant")
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = this.inflater.inflate(R.layout.color_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) view.findViewById(R.id.imageBack);
            viewHolder.view = view.findViewById(R.id.view);
            view.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.image.setColorFilter(Constants.getColor(position));
        if (this.selected == position) {
            holder.view.setVisibility(View.GONE);
        } else {
            holder.view.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public int getCount() {
        return Constants.colors.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
