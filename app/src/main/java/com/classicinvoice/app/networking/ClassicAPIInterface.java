package com.classicinvoice.app.networking;


import com.classicinvoice.app.models.getBooks.GetBooks;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClassicAPIInterface {

//=====================================================================

    @FormUrlEncoded
    @POST("1kO9PYCm195ZjCb20i-1YqxcmvWRv3M0R2RmY70ac1-0/formResponse")
    Call<Void> postData(@Field("entry.864438255") String bookName,
                        @Field("entry.1980849487") String availability); // postData


//=====================================================================

    @GET("{SheetId}/values/{Range}")
    Call<GetBooks> getData(@Path("SheetId") String sheetId,
                           @Path("Range") String range,
                           @Query("key") String apiKey);  // getData

}
