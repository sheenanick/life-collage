package com.doandstevensen.lifecollage.data.remote;

import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.NewCollageRequest;
import com.doandstevensen.lifecollage.data.model.SignUpRequest;
import com.doandstevensen.lifecollage.data.model.LogInResponse;
import com.doandstevensen.lifecollage.data.model.UserResponse;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Sheena on 2/8/17.
 */

public interface LifeCollageApiService {
    String ENDPOINT = "https://int-feb-17-api.developmentnow.net/api/";

    //PUBLIC
    @POST("public/auth/register")
    Observable<LogInResponse> signUp(@Body SignUpRequest signUpRequest);

    @FormUrlEncoded
    @POST("public/auth/login")
    Observable<LogInResponse> logIn(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/public/auth/refresh")
    Observable<LogInResponse> refresh(@Field("refresh_token") String refreshToken);


    //PRIVATE
    @GET("private/user")
    Observable<UserResponse> getUser();

    @POST("private/collage")
    Observable<CollageResponse> newCollage(@Body NewCollageRequest newCollageRequest);

    @GET("private/collage/user")
    Observable<ArrayList<CollageResponse>> getCollages(@Query("all") boolean getAllUsers);

    @GET("private/collage/{collageId}")
    Observable<CollageResponse> getCollageById(@Path("collageId") int collageId);


    class ServiceCreator {
        public static LifeCollageApiService newService() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
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
