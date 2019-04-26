package com.example.flickrdummyapp.retrofit;



import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitInterface {

    @GET
    Observable<ResponseBody> getRecentPhotos(@Url String url);

    @GET
    Observable<ResponseBody> getSearchPhotos(@Url String url);

}
