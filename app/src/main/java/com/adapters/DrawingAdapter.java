package com.adapters;

import android.graphics.Bitmap.Config;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.biblestory.color.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class DrawingAdapter extends Adapter<DrawingAdapter.MyViewHolder> {
    private String[] albumList;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    private OnItemClickListener onItemClickListener;
    private final DisplayImageOptions options = new Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).bitmapConfig(Config.RGB_565).build();

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public class MyViewHolder extends ViewHolder {
        ImageView ivRopeLeft;
        ImageView ivRopeRight;
        ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.iv_drawingItem);
            this.ivRopeLeft = (ImageView) view.findViewById(R.id.ivRopeLeft);
            this.ivRopeRight = (ImageView) view.findViewById(R.id.ivRopeRight);
        }
    }

    public DrawingAdapter(String[] albumList) {
        this.albumList = albumList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drawing_item_new, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        this.imageLoader.displayImage("assets://drawings/" + this.albumList[position], holder.thumbnail, this.options);
        if (position % 3 == 0) {
            holder.ivRopeLeft.setVisibility(View.INVISIBLE);
            holder.ivRopeRight.setVisibility(View.VISIBLE);
        } else if (position % 3 == 1) {
            holder.ivRopeLeft.setVisibility(View.VISIBLE);
            holder.ivRopeRight.setVisibility(View.VISIBLE);
        } else {
            holder.ivRopeLeft.setVisibility(View.VISIBLE);
            holder.ivRopeRight.setVisibility(View.INVISIBLE);
        }
        Log.d(getClass().getSimpleName(), new StringBuilder(String.valueOf(holder.itemView.getWidth())).append(" X ").append(holder.itemView.getWidth()).toString());
        holder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (DrawingAdapter.this.onItemClickListener != null) {
                    DrawingAdapter.this.onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public int getItemCount() {
        return this.albumList.length;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
