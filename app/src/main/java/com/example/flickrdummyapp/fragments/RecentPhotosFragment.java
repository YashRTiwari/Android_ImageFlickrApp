package com.example.flickrdummyapp.fragments;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.flickrdummyapp.DataLoaderInterface;
import com.example.flickrdummyapp.R;
import com.example.flickrdummyapp.adapters.RecentPhotoRVAdapter;
import com.example.flickrdummyapp.databinding.FragmentRecentPhotosBinding;
import com.example.flickrdummyapp.paging_lib.PhotoViewModel;
import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.Photo;

import static androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecentPhotosFragment extends Fragment implements DataLoaderInterface {

    FragmentRecentPhotosBinding binding;
    RecentPhotoRVAdapter adapter;
    private static final String TAG = "RecentPhotosFragment";

    public RecentPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recent_photos, container, false);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        binding.rv.setLayoutManager(manager);
        binding.rv.setHasFixedSize(true);
        //PhotoViewModel photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        PhotoViewModel photoViewModel = new PhotoViewModel(getContext(), this);

        binding.rv.setVisibility(View.GONE);
        binding.llPg.setVisibility(View.VISIBLE);

        adapter = new RecentPhotoRVAdapter(getContext());
        photoViewModel.photoPagedList.observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(PagedList<Photo> photos) {

                if (photos != null) {
                    Log.d(TAG, "onChanged: "+photos.size());
                    adapter.submitList(photos);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        binding.rv.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void dataLoaded() {
        binding.rv.setVisibility(View.VISIBLE);
        binding.llPg.setVisibility(View.GONE);
    }

    @Override
    public void noDataFound() {
        binding.rv.setVisibility(View.GONE);
        binding.pg.setVisibility(View.GONE);
        binding.llPg.setVisibility(View.VISIBLE);
        binding.tv.setText("Opps, something definitely went wrong!!");
    }
}
