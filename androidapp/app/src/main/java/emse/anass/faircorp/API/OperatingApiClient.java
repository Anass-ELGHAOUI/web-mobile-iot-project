package emse.anass.faircorp.API;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OperatingApiClient {

    public static final String IP_SERVER = "https://walid-ouchtiti.cleverapps.io/";

    public static final String BASE_URL = IP_SERVER;

    public static Map<String, String> getHeader(){

        String token = null;
        //if(Methods.getUser() != null){
        //    token = Utils.getUser().getToken();
        //}

        Map<String, String> map = new HashMap<>();
        //map.put("Authorization", "Bearer " + token);
        //map.put("Content-Type", "application/json");

        return map;
    }


    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    // the timeout of each request is 120 seconds
    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build();

}
