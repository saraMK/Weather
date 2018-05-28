package kayanteam.weather.Maps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import kayanteam.weather.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by kholoud on 4/18/2017.
 */
public class GPSTracker {

    private final Context mContext;
    private final static int GPS_REQUEST = 2;

    // flag for GPS status
    private boolean isGPSEnabled = false;

    // flag for network status
    private boolean isNetworkEnabled = false;

    private boolean canGetLocation = false;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    private boolean isAlertdiologOpen = false;

    public GPSTracker(Context context) {
        this.mContext = context;

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    public boolean getLocation() {
        try {


            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
//      GPS STATUS>>
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//      Network STATUS>>
            // isNetworkEnabled = isNetworkAvailable();

            if (!isGPSEnabled) {
                if (!isAlertdiologOpen)
                    showSettingsAlert();
                return false;
            } else {
                this.canGetLocation = true;
                return true;
            }


        } catch (Exception e) {

            Log.e("Error : Location",
                    "Impossible to connect to LocationManager", e);
            return false;
        }

    }


    /**
     * Function to check if best network provider
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     */
    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(mContext.getResources().getString(R.string.gps_Settings));

        alertDialog.setMessage(mContext.getResources().getString(R.string.gps_Message));

        alertDialog.setPositiveButton(mContext.getResources().getString(R.string.gps_Settings_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ((Activity) mContext).startActivityForResult(intent, GPS_REQUEST);
                dialog.cancel();


            }
        });

        alertDialog.show();
        isAlertdiologOpen = true;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        GPS_REQUEST);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        GPS_REQUEST);
            }


            return false;
        } else {

            return true;
        }
    }

    public static int getGpsRequest() {
        return GPS_REQUEST;
    }

    public void setAlertdiologOpen(boolean alertdiologOpen) {
        isAlertdiologOpen = alertdiologOpen;
    }
}
