package kayanteam.weather.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mosta on 5/25/2018.
 */

public class Apis {
    public interface GEtOnMap{
        @GET("find?cnt=10&units=metric&")
        Call<CitisModel>getData(@Query("lat")String lat,@Query("lon")String lon,@Query("APPID")String APPID);
    }

    public interface GEtallCities{
        @GET("box/city?bbox=24,22,36,31,30&type=accurate&units=metric")
        Call<CitisModel>getData(@Query("APPID")String APPID);
    }
}
