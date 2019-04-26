package com.example.flickrdummyapp.paging_lib;


import android.content.Context;

import com.example.flickrdummyapp.DataLoaderInterface;
import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.Photo;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class PhotoDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Photo>> photoLiveDataSource = new MutableLiveData<>();
    private String searchItem = null ;
    private Context context;
    private DataLoaderInterface dataLoaderInterface;

    public PhotoDataSourceFactory(Context context, DataLoaderInterface dataLoaderInterface){
        this.context = context;
        this.dataLoaderInterface = dataLoaderInterface;
    }

    public PhotoDataSourceFactory(Context context, String searchItem, DataLoaderInterface dataLoaderInterface){
        this.searchItem = searchItem;
        this.context = context;
        this.dataLoaderInterface = dataLoaderInterface;
    }

    @Override
    public DataSource create() {
        PhotoDataSource photoDataSource =null;
        if (searchItem == null) {
            photoDataSource = new PhotoDataSource(context, dataLoaderInterface);
            photoLiveDataSource.postValue(photoDataSource);
        } else {
            photoDataSource = new PhotoDataSource(context, searchItem, dataLoaderInterface);
            photoLiveDataSource.postValue(photoDataSource);
        }
        return photoDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Photo>> getPhotoLiveDataSource() {
        return photoLiveDataSource;
    }
}
