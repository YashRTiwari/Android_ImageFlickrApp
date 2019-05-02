package com.example.flickrdummyapp.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PageKeyedDataSource;
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
import android.widget.Toast;

import com.example.flickrdummyapp.DataLoaderInterface;
import com.example.flickrdummyapp.R;
import com.example.flickrdummyapp.adapters.RecentPhotoRVAdapter;
import com.example.flickrdummyapp.databinding.FragmentSearchPhotoBinding;
import com.example.flickrdummyapp.paging_lib.PhotoDataSource;
import com.example.flickrdummyapp.paging_lib.PhotoViewModel;
import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.Photo;
import com.google.android.material.snackbar.Snackbar;

import static com.example.flickrdummyapp.utils.CommonUtils.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchPhotoFragment extends Fragment implements DataLoaderInterface {

    FragmentSearchPhotoBinding binding;
    private RecentPhotoRVAdapter adapter;
    private static final String TAG = "SearchPhotoFragment";
    String searchText = null;

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

        if (!isNetworkAvailable(getContext())){
            noNetSnackbar();
        }



        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                searchText = binding.edtSearch.getText().toString();

                if (!isNetworkAvailable(getContext())){
                    checkNetwork();
                } else {
                    if (invalidateSearch(searchText)){
                        getData(searchText);
                    }
                }

                return true;
            }
            return false;
        });
        return binding.getRoot();
    }

    private void noNetSnackbar(){
        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isNetworkAvailable(getContext())){
                            Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
                        } else {
                            noNetSnackbar();
                        }


                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void checkNetwork(){

        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isNetworkAvailable(getContext())){
                            if (invalidateSearch(searchText)){
                                getData(searchText);
                            }
                        } else {
                            checkNetwork();
                        }


                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();

    }

    private boolean invalidateSearch(String searchText){

        if (searchText != null){
            if (!searchText.isEmpty()){
                getData(searchText);
                return true;
            }
        }

        Toast.makeText(getContext(), "Invalid search entry", Toast.LENGTH_SHORT).show();


        return false;
    }

    private void getData(String text){

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);

        PhotoViewModel photoViewModel = new PhotoViewModel(getContext(), text, this);
        binding.rv.setVisibility(View.GONE);
        binding.llPg.setVisibility(View.VISIBLE);
        photoViewModel.photoPagedList.observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(PagedList<Photo> photos) {

                if (photos != null) {
                    adapter.submitList(photos);
                    adapter.notifyDataSetChanged();
                }else{
                    noDataFound();
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
        binding.tv.setVisibility(View.VISIBLE);
        binding.tv.setText("Sorry, looks like its out of this world!!");
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

                            if (!searchText.equals(binding.edtSearch.getText().toString())){
                                searchText = binding.edtSearch.getText().toString();
                                if (!invalidateSearch(searchText)){
                                    return;
                                }
                            }
                            PhotoDataSource dataSource = new PhotoDataSource(getContext(),binding.edtSearch.getText().toString(),
                                    SearchPhotoFragment.this);
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
