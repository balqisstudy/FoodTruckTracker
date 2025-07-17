package com.example.foodtrucktracker.network;

import com.example.foodtrucktracker.models.FoodTruck;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FoodTruckApiService {
    
    // Base URL should be updated to your actual server
    String BASE_URL = "http://10.0.2.2:3000/api-foodtrucks"; // For Android emulator
    // String BASE_URL = "http://your-server-domain.com/api/"; // For real device
    
    @GET("foodtrucks")
    Call<List<FoodTruck>> getAllFoodTrucks();
    
    @GET("foodtrucks/{id}")
    Call<FoodTruck> getFoodTruckById(@Path("id") int id);
    
    @POST("foodtrucks")
    Call<FoodTruck> createFoodTruck(@Body FoodTruck foodTruck);
    
    @PUT("foodtrucks/{id}")
    Call<FoodTruck> updateFoodTruck(@Path("id") int id, @Body FoodTruck foodTruck);
    
    @DELETE("foodtrucks/{id}")
    Call<Void> deleteFoodTruck(@Path("id") int id);
    
    @GET("foodtrucks/active")
    Call<List<FoodTruck>> getActiveFoodTrucks();
}
