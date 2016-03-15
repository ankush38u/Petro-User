package com.paprbit.module.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.paprbit.module.R;
import com.paprbit.module.retrofit.gson_pojo.Place;
import com.paprbit.module.retrofit.gson_pojo.PlaceListResult;
import com.paprbit.module.retrofit.utility.FetchAddressIntentService;
import com.paprbit.module.retrofit.utility.GpsTracker;
import com.paprbit.module.retrofit.utility.ServiceGenerator;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        // Sender,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GpsTracker gpsTracker;
    ResideMenu resideMenu;
    Context mContext;
    ResideMenuItem pumpitem;
    ResideMenuItem fillitem;
    ResideMenuItem requestsitem;
    ResideMenuItem expenseItem;
    ResideMenuItem complaintitem;
    Handler handler;
    Marker marker;
    Location mLastLocation;
    AddressResultReceiver mResultReceiver;
    String mAddressOutput = "";
    GoogleApiClient mGoogleApiClient;
    boolean mAddressRequested = true;
    private LocationRequest mLocationRequest;
    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        handler = new Handler();
        setupActionBar();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //  gpsTracker = new GpsTracker(this);
        setupResideMenu();


        //using google location api
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mResultReceiver = new AddressResultReceiver(handler);
        }

    }

    private void setupActionBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setDisplayShowTitleEnabled(false);
            //  getSupportActionBar().setIcon(R.drawable.gs);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
       /* if (gpsTracker != null) {
            Log.d("stop", "yes");
            gpsTracker.stopUsingGPS();
        }*/

    }

    private void setupResideMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.image_bg);
        resideMenu.attachToActivity(this);

        // create menu items;
        String titles[] = {"NearBy Pumps", "Fill Petrol", "My Requests", "Monthly Expenses", "Complaint Admin"};
        int icon[] = {R.drawable.petrolpump, R.drawable.fill_petrol, R.drawable.old_requests, R.drawable.expenses, R.drawable.complaint};

        pumpitem = new ResideMenuItem(this, icon[0], titles[0]);
        pumpitem.setOnClickListener(this);
        resideMenu.addMenuItem(pumpitem, ResideMenu.DIRECTION_LEFT);

        fillitem = new ResideMenuItem(this, icon[1], titles[1]);
        fillitem.setOnClickListener(this);
        resideMenu.addMenuItem(fillitem, ResideMenu.DIRECTION_LEFT);

        requestsitem = new ResideMenuItem(this, icon[2], titles[2]);
        requestsitem.setOnClickListener(this);
        resideMenu.addMenuItem(requestsitem, ResideMenu.DIRECTION_LEFT);

        expenseItem = new ResideMenuItem(this, icon[3], titles[3]);
        expenseItem.setOnClickListener(this);
        resideMenu.addMenuItem(expenseItem, ResideMenu.DIRECTION_LEFT);

        complaintitem = new ResideMenuItem(this, icon[4], titles[4]);
        complaintitem.setOnClickListener(this);
        resideMenu.addMenuItem(complaintitem, ResideMenu.DIRECTION_LEFT);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

    }

    protected void startFetchAddressService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       /* if (gpsTracker.canGetLocation()) {
            updateLocationWithName(gpsTracker.getLatitude(), gpsTracker.getLongitude(),"My Location");
        } else return;*/
    }

    /*@Override
    public void sendData(double latitude, double longitude) {
        updateLocation(latitude, longitude);
    }*/


    private void updateLocationWithName(double latitude, double longitude, String locationAddress) {
        if (mMap != null) {
            LatLng currentLoc = new LatLng(latitude, longitude);
            mMap.clear();
            if (marker != null) marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(currentLoc).title(locationAddress));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            searchNearByPumpsUsingPlacesApi(latitude, longitude);
        }
    }

    private void searchNearByPumpsUsingPlacesApi(double latitude, double longitude) {
        //to do and add those
        String location;
        location = String.valueOf(latitude) + "," + String.valueOf(longitude);
        Call<PlaceListResult> call = ServiceGenerator.getService().getPumps(location);
        call.enqueue(new Callback<PlaceListResult>() {
            @Override
            public void onResponse(Response<PlaceListResult> response, Retrofit retrofit) {
                double lat = 0;
                double lng = 0;
                if (response.isSuccess()) {
                    Toast.makeText(MainActivity.this, response.body().status, Toast.LENGTH_SHORT).show();
                    if (response.body().status.equalsIgnoreCase("OK")) {
                        List<Place> placeList = response.body().getResults();
                        if (placeList.size() > 0) {
                            //get each place object from placelist and show on map
                            for (Place p : placeList) {
                                //    Toast.makeText(MainActivity.this,p.toString(), Toast.LENGTH_SHORT).show();
                                String name = p.getName();
                                lat = p.geometry.location.lat;
                                lng = p.geometry.location.lng;
                                if (p.getOpening_hours() != null && p.getOpening_hours().isOpen_now()) {
                                    //use open now bitmap else use simple bitmap white
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pump_marker_tinted)));

                                } else {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.pump_marker)));
                                }

                            }
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
                            mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        if (v == pumpitem) {
            recreate();
        }
        if (v == fillitem) {
            startActivity(new Intent(MainActivity.this, FillPetrol.class));
        }
        if (v == requestsitem) {
            startActivity(new Intent(MainActivity.this, MyRequests.class));
        }
        if (v == expenseItem) {
            startActivity(new Intent(MainActivity.this, MonthlyExpenses.class));
        }
        if (v == complaintitem) {
            startActivity(new Intent(MainActivity.this, MessageAdmin.class));
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //connection established to google play services
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        //get its address by reverse geocoding using google location api then set it on map
        if (mLastLocation != null) {
            Log.d("location: ", String.valueOf(mLastLocation.getLatitude()) + " " + String.valueOf(mLastLocation.getLongitude()));
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available,
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (mAddressRequested) {
                startFetchAddressService();
            }
        } else {
            Toast.makeText(MainActivity.this, "Cannot get Location updates", Toast.LENGTH_SHORT).show();
        }
        int minTime = 5000;
        int fastestTime = 3000;
        int distanceThreshold = 5; //5 meter
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(minTime);
        mLocationRequest.setFastestInterval(fastestTime);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(distanceThreshold);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        //got location now
        //do stuff
        Toast.makeText(MainActivity.this, "changed loc", Toast.LENGTH_SHORT).show();
        if (Geocoder.isPresent() && mAddressRequested) {
            startFetchAddressService();
        }
    }


    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        Handler handler;

        public AddressResultReceiver(Handler handler) {
            super(handler);
            this.handler = handler;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.

            mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);

            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {
                displayAddressOutput(mAddressOutput);
            } else if (resultCode == FetchAddressIntentService.Constants.FAILURE_RESULT) {
                //address not found dut to some error only update location with my location
                displayAddressOutput("My Location");
            }
        }
    }

    private void displayAddressOutput(final String address) {
        if (mLastLocation != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateLocationWithName(mLastLocation.getLatitude(), mLastLocation.getLongitude(), address);
                }
            });

        }

    }
}

