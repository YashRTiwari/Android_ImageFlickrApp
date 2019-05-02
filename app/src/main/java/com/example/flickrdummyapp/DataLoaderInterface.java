package com.example.flickrdummyapp;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.Photo;

public interface DataLoaderInterface {

    public void dataLoaded();
    public void noDataFound();
    public void noInternetConnection(Object params,Object callback, int ID);

}
