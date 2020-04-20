package id.putraprima.retrofit.api.services;


import java.util.List;
import java.util.Map;

import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.PasswordRequest;
import id.putraprima.retrofit.api.models.ProfileRequest;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.models.ResepList;
import id.putraprima.retrofit.api.models.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface{
    @GET("/")
    Call<AppVersion> getAppVersion();

    @POST("api/auth/login")
    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<RegisterResponse> doRegister(@Body RegisterRequest registerRequest);

    @GET("/api/auth/me")
    Call<Data<UserInfo>> doMe(@Header("Authorization") String authToken);

    @PATCH("/api/account/profile")
    Call<Data<ProfileResponse>> doUpdProf(@Header ("Authorization") String authToken, @Body ProfileRequest request);

    @PATCH("/api/account/password")
    Call<Data<ProfileResponse>> doUpdPass(@Header ("Authorization") String authToken, @Body PasswordRequest request);

    @GET("/api/recipe")
    Call<Data<List<ResepList>>> doRecipe();

    @GET("/api/recipe")
    Call<Data<List<ResepList>>> doNextPage(@Query("page") int page);

    @Multipart
    @POST("/api/recipe")
    Call<ResponseBody> doUpload(@Part MultipartBody.Part photo, @PartMap Map<String, RequestBody> text);

    //
//    @GET("/api/recipe?page=1")
//    Call<Data<List<ResepList>>> doRecipe();
//
//    @GET("/api/recipe?page=2")
//    Call<Data<List<ResepList>>> doNextPage();


}