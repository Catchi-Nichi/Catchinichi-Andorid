package com.example.catchi_nichi;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    public static final String API_URL = "https://ziho-dev.com";

    @FormUrlEncoded
    @POST("user/kakao")
    Call <Post> userInfoAPI(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("user/signup")
    Call <Post> signupAPI(@FieldMap HashMap<String,Object> param);

    @FormUrlEncoded
    @POST("user/verifyPhone")
    Call <Post> certifyNumAPI(@FieldMap HashMap<String,Object> param);

    @FormUrlEncoded
    @POST("user/checkEmail")
    Call <Post>  checkEmailAPI(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("user/signin")
    Call <Post>  LoginAPI(@FieldMap HashMap<String, Object> param);

    @GET("search")
    Call <Post>  searchAPI(@Query("searchText") String searchText, @Query("order") String order, @Query("limit") Integer limit, @Query("offset" ) Integer offset , @Query("category" ) Integer category);

    @FormUrlEncoded
    @POST("user/addNick")
    Call <Post>  addNickAPI(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("search/picture/base64")
    Call <Post>  searchPicAPI (@FieldMap HashMap<String, Object> param);

}

