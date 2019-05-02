package com.example.flickrdummyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


import com.example.flickrdummyapp.databinding.ActivityMainBinding;
import com.example.flickrdummyapp.fragments.RecentPhotosFragment;
import com.example.flickrdummyapp.fragments.SearchPhotoFragment;
import com.example.flickrdummyapp.paging_lib.PhotoDataSource;
import com.example.flickrdummyapp.retrofit.RetrofitClient;
import com.example.flickrdummyapp.retrofit.RetrofitInterface;
import com.google.android.material.snackbar.Snackbar;

import static com.example.flickrdummyapp.utils.CommonUtils.isNetworkAvailable;


public class MainActivity extends AppCompatActivity{

    private RetrofitInterface apiInterface;
    private final String TAG = getClass().getName();
    private ActivityMainBinding binding;
    private boolean networkAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setTitle("Recent");
        RecentPhotosFragment fragment = new  RecentPhotosFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();




        binding.bottomNav.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()){

                case R.id.search:
                    setTitle("Search");
                    SearchPhotoFragment search = new  SearchPhotoFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, search).commit();
                    break;
                case R.id.recent:
                    setTitle("Recent");
                    RecentPhotosFragment fragment1 = new  RecentPhotosFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment1).commit();
                    break;
            }


            return false;
        });

        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);

    }




}
