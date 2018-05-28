package kayanteam.weather.Listenrs;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by mosta on 3/21/2018.
 */

public class TriggerLocationMap {
    private SetLocationOnMap setLocationOnMap;

    public TriggerLocationMap(SetLocationOnMap setLocationOnMap)
    {
        this.setLocationOnMap = setLocationOnMap;
    }
    public void returnMyMap(GoogleMap mMap){
          setLocationOnMap.getMyMap( mMap);
    }

}
