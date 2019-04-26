package com.example.flickrdummyapp.paging_lib;

import android.content.Context;
import android.util.Log;

import com.example.flickrdummyapp.DataLoaderInterface;
import com.example.flickrdummyapp.retrofit.RetrofitClient;
import com.example.flickrdummyapp.retrofit.RetrofitInterface;
import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.Photo;
import com.example.flickrdummyapp.retrofit.entity.PhotoListEntity.PhotosListEntity;
import com.example.flickrdummyapp.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class PhotoDataSource extends PageKeyedDataSource<Integer, Photo> {

    public static final int PAGE_SIZE = 20;
    private static final int FIRST_PAGE = 1;
    private static final String SITE_NAME = "";
    private RetrofitInterface apiInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final String TAG = getClass().getName();
    private int MAX_NO_PAGES = 0;
    List<Photo> data = new ArrayList<>();
    private Context context;
    private String searchItem = null;
    private DataLoaderInterface dataLoaderInterface;

    PhotoDataSource(Context context, DataLoaderInterface dataLoaderInterface){
        this.context = context;
        searchItem = null;
        this.dataLoaderInterface = dataLoaderInterface;
    }

    PhotoDataSource(Context context, String searchItem, DataLoaderInterface dataLoaderInterface){
        this.context = context;
        this.searchItem = searchItem;
        this.dataLoaderInterface = dataLoaderInterface;

    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Photo> callback) {
        String url ="";
        if (searchItem == null){
            url = NetworkUtils.getRecentPhotosUrl(FIRST_PAGE);

        } else {
            url = NetworkUtils.getSearchPhotos(FIRST_PAGE, searchItem);
        }
        Log.d(TAG, "loadInitial: "+url);
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        apiInterface.getRecentPhotos(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                        String response = null;
                        try {
                            response = responseBody.string();
                            Log.d(TAG, "onNext: " + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        PhotosListEntity responseEntity = new Gson().fromJson(response, new TypeToken<PhotosListEntity>() {
                        }.getType());
                        MAX_NO_PAGES = responseEntity.getPhotos().getTotal();
                        Log.d(TAG, "onNext: MAX_NO_PAGES" + MAX_NO_PAGES);

                        data = responseEntity.getPhotos().getPhoto();
                        if (data != null) {

                            if (data.isEmpty()){
                                dataLoaderInterface.noDataFound();
                                return;
                            }
                            Log.d(TAG, "onNext: List size : " + data.size());
                            callback.onResult(data, null, FIRST_PAGE + 1);
                            dataLoaderInterface.dataLoaded();
                        } else {
                            Log.d(TAG, "onNext: No data found");
                            dataLoaderInterface.noDataFound();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        dataLoaderInterface.noDataFound();

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");

                    }
                });



    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {

        String url ="";
        if (searchItem == null){
            url = NetworkUtils.getRecentPhotosUrl(params.key);

        } else {
            url = NetworkUtils.getSearchPhotos(params.key, searchItem);
        }

        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        apiInterface.getRecentPhotos(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                        String response = null;
                        try {
                            response = responseBody.string();
                            Log.d(TAG, "onNext: " + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        PhotosListEntity responseEntity = new Gson().fromJson(response, new TypeToken<PhotosListEntity>() {
                        }.getType());
                        MAX_NO_PAGES = responseEntity.getPhotos().getTotal();
                        Log.d(TAG, "onNext: MAX_NO_PAGES" + MAX_NO_PAGES);
                        data = responseEntity.getPhotos().getPhoto();
                        if (data != null) {
                            Log.d(TAG, "onNext: List size : " + data.size());

                            if (data.isEmpty()){
                                dataLoaderInterface.noDataFound();
                                return;
                            }


                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(data, key);
                            dataLoaderInterface.dataLoaded();
                        } else {
                            Log.d(TAG, "onNext: No data found");
                            dataLoaderInterface.noDataFound();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        dataLoaderInterface.noDataFound();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");

                    }
                });

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {
        String url ="";
        if (searchItem == null){
            url = NetworkUtils.getRecentPhotosUrl(params.key);

        } else {
            url = NetworkUtils.getSearchPhotos(params.key, searchItem);
        }
        apiInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);
        apiInterface.getRecentPhotos(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                        String response = null;
                        try {
                            response = responseBody.string();
                            Log.d(TAG, "onNext: " + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        PhotosListEntity responseEntity = new Gson().fromJson(response, new TypeToken<PhotosListEntity>() {
                        }.getType());
                        MAX_NO_PAGES = responseEntity.getPhotos().getTotal();
                        Log.d(TAG, "onNext: MAX_NO_PAGES" + MAX_NO_PAGES);

                        data = responseEntity.getPhotos().getPhoto();
                        if (data != null) {

                            if (data.isEmpty()){
                                dataLoaderInterface.noDataFound();
                                return;
                            }
                            Log.d(TAG, "onNext: List size : " + data.size());
                            Integer key = (params.key < MAX_NO_PAGES) ? params.key + 1 : null;
                            callback.onResult(data, key);
                            Log.d(TAG, "loadBefore: "+data.size());
                            dataLoaderInterface.dataLoaded();
                        } else {
                            Log.d(TAG, "onNext: No data found");
                            dataLoaderInterface.noDataFound();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        dataLoaderInterface.noDataFound();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");

                    }
                });

    }





}
