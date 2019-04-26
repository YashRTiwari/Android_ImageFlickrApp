package com.example.flickrdummyapp.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.flickrdummyapp.DataLoaderInterface;
import com.example.flickrdummyapp.R;
import com.example.flickrdummyapp.adapters.RecentPhotoRVAdapter;
import com.example.flickrdummyapp.databinding.FragmentSearchPhotoBinding;
import com.example.flickrdummyapp.paging_lib.PhotoViewModel;
import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.Photo;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPhotoFragment extends Fragment implements DataLoaderInterface {

    FragmentSearchPhotoBinding binding;
    private RecentPhotoRVAdapter adapter;
    private static final String TAG = "SearchPhotoFragment";

    public SearchPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_photo, container, false);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        binding.rv.setLayoutManager(manager);
        binding.rv.setHasFixedSize(true);

        adapter = new RecentPhotoRVAdapter(getContext());

        binding.rv.setVisibility(View.GONE);
        binding.llPg.setVisibility(View.GONE);

        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);

                PhotoViewModel photoViewModel = new PhotoViewModel(getContext(), binding.edtSearch.getText().toString(), this);
                binding.rv.setVisibility(View.GONE);
                binding.llPg.setVisibility(View.VISIBLE);
                photoViewModel.photoPagedList.observe(this, new Observer<PagedList<Photo>>() {
                    @Override
                    public void onChanged(PagedList<Photo> photos) {

                        if (photos != null) {
                            Log.d(TAG, "onChanged: "+photos.size());
                            adapter.submitList(photos);
                            adapter.notifyDataSetChanged();
                        }else{
                            noDataFound();
                        }

                    }
                });

                binding.rv.setAdapter(adapter);
                return true;
            }
            return false;
        });
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
        binding.tv.setVisibility(View.VISIBLE);
        binding.tv.setText("Sorry, looks like its out of this world!!");
    }
}
