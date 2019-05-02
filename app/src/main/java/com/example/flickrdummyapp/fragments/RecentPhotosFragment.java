package com.example.flickrdummyapp.fragments;


import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flickrdummyapp.DataLoaderInterface;
import com.example.flickrdummyapp.MainActivity;
import com.example.flickrdummyapp.R;
import com.example.flickrdummyapp.adapters.RecentPhotoRVAdapter;
import com.example.flickrdummyapp.databinding.FragmentRecentPhotosBinding;
import com.example.flickrdummyapp.paging_lib.PhotoDataSource;
import com.example.flickrdummyapp.paging_lib.PhotoViewModel;
import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.Photo;
import com.google.android.material.snackbar.Snackbar;

import static com.example.flickrdummyapp.utils.CommonUtils.isNetworkAvailable;


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
        binding.rv.setVisibility(View.GONE);
        binding.llPg.setVisibility(View.VISIBLE);
        if (!isNetworkAvailable(getContext())) {
            binding.rv.setVisibility(View.GONE);
            binding.llPg.setVisibility(View.GONE);
            checkNetwork();
        }else
            setAdapter();
        return binding.getRoot();
    }

    private void checkNetwork(){

        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isNetworkAvailable(getContext())){
                            binding.rv.setVisibility(View.GONE);
                            binding.llPg.setVisibility(View.VISIBLE);
                            setAdapter();
                        } else {
                            binding.rv.setVisibility(View.GONE);
                            binding.llPg.setVisibility(View.GONE);
                            checkNetwork();

                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();

    }


    private void setAdapter(){
        PhotoViewModel photoViewModel = new PhotoViewModel(getContext(), this);
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

    @Override
    public void noInternetConnection(Object params,Object callback, int ID) {
        binding.llPg.setVisibility(View.GONE);
        showNoInternetSnackBar(params,callback, ID);
    }

    private void showNoInternetSnackBar(Object params,Object callback, int ID){
        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isNetworkAvailable(getContext())){
                            PhotoDataSource dataSource = new PhotoDataSource(getContext(), RecentPhotosFragment.this);
                            dataSource.retry(params, callback, ID);
                        } else {
                            showNoInternetSnackBar(params, callback, ID);
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
