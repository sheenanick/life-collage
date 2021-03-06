package com.doandstevenson.lifecollage.data.remote;

import com.doandstevenson.lifecollage.data.model.CollageListResponse;
import com.doandstevenson.lifecollage.data.model.CollageResponse;
import com.doandstevenson.lifecollage.data.model.LogInResponse;
import com.doandstevenson.lifecollage.data.model.NewCollageRequest;
import com.doandstevenson.lifecollage.data.model.NewPictureRequest;
import com.doandstevenson.lifecollage.data.model.PictureResponse;
import com.doandstevenson.lifecollage.data.model.ServerResponse;
import com.doandstevenson.lifecollage.data.model.SignUpRequest;
import com.doandstevenson.lifecollage.data.model.UpdateCollageRequest;
import com.doandstevenson.lifecollage.data.model.UpdateUserRequest;
import com.doandstevenson.lifecollage.data.model.UserResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Sheena on 2/8/17.
 */

public interface LifeCollageApiService {
    String ENDPOINT = "https://int-feb-17-api.developmentnow.net/api/";

    //AUTH
    @POST("public/auth/register")
    Observable<LogInResponse> signUp(@Body SignUpRequest signUpRequest);

    @FormUrlEncoded
    @POST("public/auth/login")
    Observable<LogInResponse> logIn(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("public/auth/refresh")
    Observable<LogInResponse> refresh(@Field("refresh_token") String refresh_token);


    //USER
    @GET("public/user")
    Observable<ArrayList<UserResponse>> getUsers(@Query("username") String username);

    @GET("private/user")
    Observable<UserResponse> getUser();

    @DELETE("private/user")
    Observable<ServerResponse> deleteUser();

    @PUT("private/user")
    Observable<UserResponse> updateUser(@Body UpdateUserRequest request);


    //COLLAGE
    @GET("public/collage/all")
    Observable<ArrayList<CollageResponse>> getAllCollages();

    @GET("public/collage/latestPic/{userId}")
    Observable<ArrayList<CollageListResponse>> getCollages(@Path("userId") int userId);

    @GET("public/collage/{collageId}")
    Observable<CollageResponse> getCollageById(@Path("collageId") int collageId);

    @POST("private/collage")
    Observable<CollageResponse> newCollage(@Body NewCollageRequest newCollageRequest);

    @PUT("private/collage")
    Observable<CollageResponse> updateCollage(@Body UpdateCollageRequest request);

    @PUT("private/collage/updateOwner/{collageId}")
    Observable<CollageResponse> updateCollageOwner(@Path("collageId") int collageId);

    @DELETE("private/collage/{collageId}")
    Observable<CollageResponse> deleteCollageById(@Path("collageId") int collageId);


    //PICTURE
    @GET("public/picture/all/collageId/{collageId}")
    Observable<ArrayList<PictureResponse>> getAllPictures(@Path("collageId") int collageId);

    @GET("public/picture/collageId/{collageId}")
    Observable<PictureResponse> getLastPicture(@Path("collageId") int collageId);

    @POST("private/picture/collageId/{collageId}")
    Observable<PictureResponse> postPicture(@Path("collageId") int collageId, @Body NewPictureRequest request);


    class ServiceCreator {
        public static LifeCollageApiService newService() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
            httpClient.readTimeout(30, TimeUnit.SECONDS);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();
            return retrofit.create(LifeCollageApiService.class);
        }

        public static LifeCollageApiService newPrivateService(final String accessToken) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    okhttp3.Request original = chain.request();

                    okhttp3.Request request = original.newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();
            return retrofit.create(LifeCollageApiService.class);
        }
    }
}
