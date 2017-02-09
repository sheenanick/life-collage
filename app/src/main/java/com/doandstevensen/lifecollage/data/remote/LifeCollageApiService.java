package com.doandstevensen.lifecollage.data.remote;

import com.doandstevensen.lifecollage.data.model.SignUpRequest;
import com.doandstevensen.lifecollage.data.model.SignUpResponse;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Sheena on 2/8/17.
 */

public interface LifeCollageApiService {
    String ENDPOINT = "https://int-feb-17-api.developmentnow.net/api/";

    @POST("public/auth/register")
    Observable<SignUpResponse> SignUp(@Body SignUpRequest signUpRequest);


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
    }
}
