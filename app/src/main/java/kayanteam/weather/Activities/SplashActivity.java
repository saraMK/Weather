package kayanteam.weather.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import kayanteam.weather.Maps.GPSTracker;
import kayanteam.weather.R;

public class SplashActivity extends AppCompatActivity {

    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        gpsTracker = new GPSTracker(this);
        getintent();
    }

    private void getintent() {
        if (gpsTracker.checkLocationPermission())
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the LoginActivityAct-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this, MapActivity.class);
                    startActivity(mainIntent);
                    finish();

                }
            }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GPSTracker.getGpsRequest()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getintent();
            }
        }
    }

}
