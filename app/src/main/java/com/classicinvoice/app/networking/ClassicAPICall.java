package com.classicinvoice.app.networking;

import com.classicinvoice.app.ClassicConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClassicAPICall {

    public static Retrofit retrofit = null;

    public static Retrofit getClient(int type) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(interceptor).build();

        if (type == 1) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(ClassicConstant.FORMS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        } else if (type == 2) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(ClassicConstant.SHEETS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;

    }


    public static ClassicAPIInterface getClassicAPIInterface(){

        return  ClassicAPICall.getClient(1).create(ClassicAPIInterface.class);

    }

    public static ClassicAPIInterface getSheetsAPIInterface(){

        return  ClassicAPICall.getClient(2).create(ClassicAPIInterface.class);

    }

}