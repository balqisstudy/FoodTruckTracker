package com.example.foodtrucktracker.network;

import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final String TAG = "ApiClient";
    private static Retrofit retrofit = null;
    
    // Use the exact same URL that works for POST requests
    private static final String BASE_URL = "http://10.0.2.2:3000/api/";
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            Log.d(TAG, "Creating new Retrofit client with base URL: " + BASE_URL);
            
            // Create custom Gson with lenient parsing and type adapters
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .registerTypeAdapter(boolean.class, (JsonDeserializer<Boolean>) (json, typeOfT, context) -> {
                        try {
                            if (json.isJsonPrimitive()) {
                                JsonPrimitive primitive = json.getAsJsonPrimitive();
                                if (primitive.isBoolean()) {
                                    return primitive.getAsBoolean();
                                } else if (primitive.isNumber()) {
                                    return primitive.getAsInt() != 0;
                                } else if (primitive.isString()) {
                                    String str = primitive.getAsString().toLowerCase();
                                    return "true".equals(str) || "1".equals(str);
                                }
                            }
                            return false;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .registerTypeAdapter(Boolean.class, (JsonDeserializer<Boolean>) (json, typeOfT, context) -> {
                        try {
                            if (json.isJsonPrimitive()) {
                                JsonPrimitive primitive = json.getAsJsonPrimitive();
                                if (primitive.isBoolean()) {
                                    return primitive.getAsBoolean();
                                } else if (primitive.isNumber()) {
                                    return primitive.getAsInt() != 0;
                                } else if (primitive.isString()) {
                                    String str = primitive.getAsString().toLowerCase();
                                    return "true".equals(str) || "1".equals(str);
                                }
                            }
                            return false;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .create();
            
            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d(TAG, "HTTP: " + message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            
            // Create OkHttp client with extended timeouts and retry
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            
            // Create Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            
            Log.d(TAG, "Retrofit client created successfully");
        }
        return retrofit;
    }
    
    public static FoodTruckApiService getFoodTruckApiService() {
        return getClient().create(FoodTruckApiService.class);
    }
}
