package se.miun.android_app;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Gives an instance of retrofit
public class ApiClient {

    // Base url where the api is located
    public static final String BASE_URL = "http://193.10.119.34:8080/WS/webapi/";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient()
    {

        // If a instance has not been set
        if (retrofit==null)
        {
            // Set the instance of retrofit using the base url
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
