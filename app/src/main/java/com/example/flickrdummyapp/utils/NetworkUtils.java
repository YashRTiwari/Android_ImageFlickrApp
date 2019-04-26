package com.example.flickrdummyapp.utils;

public class NetworkUtils {

    public static final String BASE_URL = "https://api.flickr.com/services/rest/?";

    private static String getFlickrMethodUrl(String method){
        return  String.format(BASE_URL +
                "method=%s&", method);
    }


    public static String getRecentPhotosUrl(int pageNumber){
        return  String.format(getFlickrMethodUrl(CommonKeyUtility.FLICKR_RECENT_METHOD) +
                "per_page=20&" +
                "page=%d&" +
                "api_key=%s&" +
                "format=json&" +
                "nojsoncallback=1&" +
                "extras=url_s", pageNumber,CommonKeyUtility.FLICKR_API_KEY);
    }

    public static String getSearchPhotos(int pageNumber,String searchText){
        return String.format(getFlickrMethodUrl(CommonKeyUtility.FLICKR_SEARCH_METHOD) +
                "per_page=20&" +
                "page=%d&" +
                "api_key=%s&" +
                "format=json&" +
                "nojsoncallback=1&" +
                "extras=url_s&" +
                "text=%s", pageNumber, CommonKeyUtility.FLICKR_API_KEY, searchText);
    }
}
