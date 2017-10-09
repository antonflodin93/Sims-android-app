package se.miun.android_app;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "193.10.119.34:8080/WS/webapi";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient()
    {
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
