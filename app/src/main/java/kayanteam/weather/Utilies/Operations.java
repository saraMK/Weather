package kayanteam.weather.Utilies;

import java.text.DecimalFormat;

/**
 * Created by mosta on 5/28/2018.
 */

public class Operations {

    public static final String BASE_URL="http://api.openweathermap.org/data/2.5/";
    public static final String IMAGE_PATH="https://openweathermap.org/img/w/";
    public static final String APPID="a5575f9467ae39fc56eb36d0985a37ff";


    public static String roundTwoDecimals(String d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return twoDForm.format(Double.parseDouble(d));
    }
}
