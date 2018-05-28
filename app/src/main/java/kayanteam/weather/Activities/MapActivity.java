package kayanteam.weather.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import kayanteam.weather.Maps.CreatMap;
import kayanteam.weather.Maps.GPSTracker;
import kayanteam.weather.R;
import kayanteam.weather.Fragments.SearchFragment;
import kayanteam.weather.Listenrs.SetLocationOnMap;
import kayanteam.weather.Listenrs.TriggerLocationMap;

public class MapActivity extends AppCompatActivity implements SetLocationOnMap {

    private CreatMap creatMap;
    private TriggerLocationMap triggerLocationMap;
    private Toolbar toolbar;
    private SearchView searchView;
    private SearchFragment searchFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // listenr to know if map is avaiable
        triggerLocationMap = new TriggerLocationMap(this);

        // set map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        creatMap = new CreatMap(this, mapFragment, triggerLocationMap);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) toolbar.findViewById(R.id.search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));
        getSupportActionBar().setTitle("");
        // search fragment
        searchFrag = new SearchFragment();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("onQueryTextSubmit", "close");
                if (searchFrag.isVisible())
                    getSupportFragmentManager().popBackStack();
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchFrag.isVisible())
                    getSearchFragment();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!searchFrag.isVisible())
                    getSearchFragment();
                Log.d("onQueryTextSubmit", newText);
                searchFrag.filter(newText);
                return false;
            }
        });
    }

    private void getSearchFragment() {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).
                replace(R.id.inflate_frag, searchFrag)
                .commit();
    }

    //  for google map
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GPSTracker.getGpsRequest()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                creatMap.reStarthandler();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPSTracker.getGpsRequest()) {
            creatMap.reStarthandler();
        }

    }

    @Override
    public void getMyMap(GoogleMap map) {
        Log.d("map","map set");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
