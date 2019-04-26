package com.example.flickrdummyapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.flickrdummyapp.R;
import com.example.flickrdummyapp.databinding.RvRowRecentPhotosBinding;
import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.Photo;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class RecentPhotoRVAdapter extends PagedListAdapter<Photo, RecentPhotoRVAdapter.ViewHolder> {

    private Context context;
    private static final String TAG = "RecentPhotoRVAdapter";


    private static DiffUtil.ItemCallback<Photo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.equals(newItem);
        }
    };

    public RecentPhotoRVAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RvRowRecentPhotosBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.rv_row_recent_photos, parent,false);
        return new RecentPhotoRVAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = getItem(position);
        if (photo != null) {
            Glide.with(context).load(photo.getUrlS()).into(holder.binding.image);
            Log.d(TAG, "onBindViewHolder: "+photo.getUrlS());
        } else {
            Log.d(TAG, "onBindViewHolder: photo no found");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RvRowRecentPhotosBinding binding;
        public ViewHolder(RvRowRecentPhotosBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
