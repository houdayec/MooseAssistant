package labs.hantiz.mooseproject.Map;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import labs.hantiz.mooseproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private MapView mMapView;
    private GoogleMap googleMap;
    private UiSettings uiSettings;
    private Marker mMarker;
    private LocationManager locationManager;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        //Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Map");

        //Managing the user location before starting any display
        boolean permissionGranted = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permissionGranted) {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Log.d("location permission", "allowed");
        } else {
            Log.d("location permission", "asking for it");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }

        //Initializing the map
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        //Display and pin the position of the university on the map loading
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                //latitude and longitude for Metropolia
                LatLng location = new LatLng(60.2208265, 24.8046457);
                LatLng locationRailwayStation = new LatLng(60.169832654, 24.938162914);
                LatLng locationAirport = new LatLng(60.316998732, 24.957996168);

                //create marker
                MarkerOptions marker = new MarkerOptions().position(
                        location).title("My university" +
                        "");
                MarkerOptions marker2 = new MarkerOptions().position(
                        locationRailwayStation).title("Main railway station" +
                        "");
                MarkerOptions marker3 = new MarkerOptions().position(
                        locationAirport).title("Helsinki airport" +
                        "");


                //adding marker
                googleMap.addMarker(marker);
                googleMap.addMarker(marker2);
                googleMap.addMarker(marker3);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Manifest location state", "accessible");
                    googleMap.setMyLocationEnabled(true);
                    return;
                }

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(location).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                uiSettings = googleMap.getUiSettings();

                //Allow the user to zoom the map
                uiSettings.setZoomGesturesEnabled(true);

            }
        });





        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }



        return rootView;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("location", String.valueOf(location));
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        mMarker.remove();
        mMarker = googleMap.addMarker(new MarkerOptions().position(loc).title("My Location" +
                ""));
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17));
        }
    }


    //Methods to manage the location manager states
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }


    //CRUD methods of the fragment to manage the map
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }





}
