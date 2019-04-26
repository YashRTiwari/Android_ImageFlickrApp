package com.example.flickrdummyapp.paging_lib;

import android.content.Context;

import com.example.flickrdummyapp.DataLoaderInterface;
import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.Photo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class PhotoViewModel extends ViewModel {

    public LiveData<PagedList<Photo>> photoPagedList;
    public LiveData<PageKeyedDataSource<Integer, Photo>> liveDataSource;
    private Context context;
    private DataLoaderInterface dataLoaderInterface;

    public PhotoViewModel(Context context, DataLoaderInterface dataLoaderInterface){
        this.context = context;
        this.dataLoaderInterface = dataLoaderInterface;
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory(context, dataLoaderInterface);
        liveDataSource = photoDataSourceFactory.getPhotoLiveDataSource();
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PhotoDataSource.PAGE_SIZE)
                .build();
        photoPagedList = new LivePagedListBuilder(photoDataSourceFactory, config).build();
    }

    public PhotoViewModel(Context context, String searchItem, DataLoaderInterface dataLoaderInterface){
        this.context = context;
        this.dataLoaderInterface = dataLoaderInterface;
        PhotoDataSourceFactory photoDataSourceFactory = new PhotoDataSourceFactory(context, searchItem, dataLoaderInterface);
        liveDataSource = photoDataSourceFactory.getPhotoLiveDataSource();
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PhotoDataSource.PAGE_SIZE)
                .build();
        photoPagedList = new LivePagedListBuilder(photoDataSourceFactory, config).build();
    }
}
