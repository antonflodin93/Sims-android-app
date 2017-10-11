package se.miun.android_app.Api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Gives an instance of retrofit
public class ApiClient {

    // Base url where the api is located
    private static final String BASE_URL = "http://193.10.119.34:8080/WS/webapi/";
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient()
    {

        // If a instance has not been set
        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Set the instance of retrofit using the base url
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }

}
