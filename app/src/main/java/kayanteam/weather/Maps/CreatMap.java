package kayanteam.weather.Maps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import kayanteam.weather.Dialogs.CreatDialog;
import kayanteam.weather.Listenrs.TriggerLocationMap;
import kayanteam.weather.R;
import kayanteam.weather.Retrofit.Apis;
import kayanteam.weather.Retrofit.CitisModel;
import kayanteam.weather.Utilies.Operations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by mosta on 1/11/2018.
 */

public class CreatMap implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {
    private Context context;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP_LOCATION";
    private LatLng mCenterLatLong;
    private Thread thread;
    private CameraPosition cameraPosition;
    private Marker temp_marker;
    private GPSTracker gpsTracker;
    private Handler handlerPremission;
    private Runnable runnablePremission;
    private Boolean notLock = true;

    private CreatDialog create_dialog;
    private Dialog dialog;

    private TriggerLocationMap triggerLocationMap;

    public CreatMap(final Context context, final SupportMapFragment mapFragment, TriggerLocationMap triggerLocationMap) {
        this.context = context;
        this.mapFragment = mapFragment;
        this.triggerLocationMap = triggerLocationMap;
        mapFragment.getMapAsync(this);

        //   check premission of location and gps
        gpsTracker = new GPSTracker(context);
        handlerPremission = new Handler();
        checkpremission();
        create_dialog = new CreatDialog();
        dialog = create_dialog.create_dialog(context);

    }

    public void checkpremission() {

        buildGoogleApiClient();
        gpsTracker.setAlertdiologOpen(false);
        runnablePremission = new Runnable() {
            public void run() {
                handlerPremission.removeCallbacks(this);
                if (gpsTracker.checkLocationPermission()) {
                    if (gpsTracker.getLocation()) {
                        mGoogleApiClient.connect();
                        //  handlerPremission.removeCallbacks(this);
                        notLock = true;
                    } else {
                        reStarthandler();
                    }
                } else {
                    reStarthandler();

                }
            }
        };
        runnablePremission.run();

    }

    public void reStarthandler() {
        if (notLock) {
            notLock = false;
            handlerPremission.postDelayed(runnablePremission, 1000);

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            Log.d(TAG, "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            if (location != null)
                changeMap(location.getLatitude(), location.getLongitude());


            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {


        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "OnMapReady");
        mMap = googleMap;
        mGoogleApiClient.connect();

        // listern to get map
        triggerLocationMap.returnMyMap(mMap);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera_postion_change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;
                if (mCenterLatLong.latitude != 0.0)
                    try {
                        Location mLocation = new Location("");
                        mLocation.setLatitude(mCenterLatLong.latitude);
                        mLocation.setLongitude(mCenterLatLong.longitude);

                        // request to get 10 cities around this location
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Operations.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create()).build();
                        Apis.GEtOnMap apis = retrofit.create(Apis.GEtOnMap.class);
                        Call<CitisModel> modelCall = apis.getData(mCenterLatLong.latitude + ""
                                , mCenterLatLong.longitude + "", Operations.APPID);
                        modelCall.enqueue(new Callback<CitisModel>() {
                            @Override
                            public void onResponse(Call<CitisModel> call, Response<CitisModel> response) {
                                Log.d("Response", response.body().getList().get(0).getCroods() + "");
                                Log.d("Response", call.request() + "");
                                mMap.clear();
                                int i = 0;
                                for (CitisModel.Model model : response.body().getList()) {
                                    set_marker(model.getCroods().getLat(), model.getCroods().getLon(), model, i, response.body().getList());
                                    i++;
                                }
                            }

                            @Override
                            public void onFailure(Call<CitisModel> call, Throwable t) {
                                Log.d("Response", t.toString() + "\t" + call.request());
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    return;
                }
            }
        });

    }


    public void changeMap(Double lat, Double lang) {
        Log.d(TAG, "Reaching_map" + mMap);


        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            latLong = new LatLng(lat, lang);


            cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(10f).tilt(30).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            //   mMap.getUiSettings().setAllGesturesEnabled(false);//disable moving map


        } else {
            Toast.makeText(context,
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void set_marker(Double latitude, Double longitude, final CitisModel.Model model, int i,
                           final List<CitisModel.Model> list) {


        BitmapDescriptor caricon = BitmapDescriptorFactory.fromBitmap(createStoreMarker(model.getMain().getTemp()));
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(caricon);
        markerOptions.draggable(true);
        markerOptions.snippet(i + "");
        temp_marker = mMap.addMarker(markerOptions);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int pos = Integer.parseInt(marker.getSnippet());
                Log.d("onMapClick", list.get(pos).getName() + "\t" + list.get(pos).getMain().getTemp());
                create_dialog.setDialogData(list.get(pos));
                dialog.show();
                return false;
            }
        });


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();


        setmGoogleApiClient(mGoogleApiClient);


    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    private Bitmap createStoreMarker(String msg) {
        View markerLayout = ((Activity) context).getLayoutInflater().inflate(R.layout.marker, null);

        ImageView markerImage = (ImageView) markerLayout.findViewById(R.id.marker_icon);
        TextView markerRating = (TextView) markerLayout.findViewById(R.id.marker_text);
        markerImage.setImageResource(R.drawable.logo);
        markerRating.setText(Operations.roundTwoDecimals(msg) + " °C");

        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }


}
