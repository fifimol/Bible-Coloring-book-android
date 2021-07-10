package com.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap.Config;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
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

import com.support.permission.PermissionUtils;
import com.support.permission.PermissionUtils.OnRequestedPermissionListener;
import com.utils.Constants;
import java.io.File;
import java.util.ArrayList;

public class MyArtworkAdapter extends Adapter<MyArtworkAdapter.MyViewHolder> {
    private ArrayList<String> albumList;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private OnDeleteClickListener onDeleteListener;
    private final DisplayImageOptions options = new Builder().resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).bitmapConfig(Config.ARGB_4444).build();

    public interface OnDeleteClickListener {
        void onDeleteClicked(int i);
    }

    public class MyViewHolder extends ViewHolder {
        ImageView btnDelete;
        ImageView btnSave;
        ImageView btnShare;
        ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.btnSave = (ImageView) view.findViewById(R.id.myArtWork_btnSave);
            this.btnShare = (ImageView) view.findViewById(R.id.myArtWork_btnShare);
            this.btnDelete = (ImageView) view.findViewById(R.id.myArtWork_btnDelete);
        }
    }

    public MyArtworkAdapter(ArrayList<String> albumList) {
        this.albumList = albumList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_artwork_card, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, int position) {
        this.imageLoader.displayImage("file://" + Constants.getMyArtworksDirectory(holder.thumbnail.getContext()) + File.separator + ((String) this.albumList.get(position)), holder.thumbnail, this.options);
        holder.btnSave.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Activity activity = (Activity) holder.btnSave.getContext();
                final MyViewHolder myViewHolder = holder;
                PermissionUtils.requestPermission(activity, 9, new OnRequestedPermissionListener() {
                    public void onPermissionResult(int responsePermission, boolean isPermissionGranted) {
                        if (isPermissionGranted) {
                            Constants.saveBitmapToGallery(myViewHolder.btnSave.getContext(), MyArtworkAdapter.this.imageLoader.loadImageSync("file://" + Constants.getMyArtworksDirectory(myViewHolder.thumbnail.getContext()) + File.separator + ((String) MyArtworkAdapter.this.albumList.get(myViewHolder.getAdapterPosition())), MyArtworkAdapter.this.options));
                            new AlertDialog.Builder(myViewHolder.btnSave.getContext()).setMessage("Your artwork has been saved to the gallery.").setPositiveButton("OK", null).show();
                        }
                    }
                });
            }
        });
        holder.btnShare.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Constants.shareBitmap(holder.btnSave.getContext(), MyArtworkAdapter.this.imageLoader.loadImageSync("file://" + Constants.getMyArtworksDirectory(holder.thumbnail.getContext()) + File.separator + ((String) MyArtworkAdapter.this.albumList.get(holder.getAdapterPosition())), MyArtworkAdapter.this.options), holder.btnSave.getContext().getString(R.string.message_to_share), holder.btnSave.getContext().getString(R.string.intent_message));
            }
        });
        holder.btnDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MyArtworkAdapter.this.onDeleteListener != null) {
                    MyArtworkAdapter.this.onDeleteListener.onDeleteClicked(holder.getAdapterPosition());
                }
            }
        });
    }

    public int getItemCount() {
        return this.albumList.size();
    }

    public void setOnDeleteListener(OnDeleteClickListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }
}
