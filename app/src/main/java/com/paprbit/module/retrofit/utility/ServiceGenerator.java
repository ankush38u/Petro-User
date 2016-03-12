package com.paprbit.module.retrofit.utility;

import android.content.Context;

import com.paprbit.module.R;
import com.paprbit.module.retrofit.service.ApiService;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ankush38u on 12/26/2015.
 */
public class ServiceGenerator {
    // creates ApiService instance as singleton
    private static ApiService service = null;
    private static Retrofit retrofit = null;


    public static ApiService getService() {
        //service to use without session
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://goyalsales.com/test/app-services/user/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if (service == null) service = retrofit.create(ApiService.class);

        return service;
    }


    private static ApiService service2 = null;
    private static Retrofit retrofit2 = null;

    public static ApiService getService(final Context context) {
//service to use with session

        if (retrofit2 == null) {
            final OkHttpClient httpClient = new OkHttpClient();
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Cookie", "PHPSESSID=" + Storage.getStringFromPrefs(context, context.getString(R.string.session_id)))
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            });

            retrofit2 = new Retrofit.Builder()
                    .baseUrl("http://goyalsales.com/test/app-services/user/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        if (service2 == null) service2 = retrofit2.create(ApiService.class);

        return service2;
    }


}
