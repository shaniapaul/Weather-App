package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static java.lang.Double.parseDouble;
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    String APIKEY = "AIzaSyAITYHTlV2GSYUNP8zVMbfPy1iDf8qkaCo";
    String APIDark = "ca39a8fa608cfb403d226a71b0bee3c5";
    String address = "";
    String latitude = "";
    String longitude = "";
    double temperature = 0, humid = 0, wind = 0, precip = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //get input address from previous stage
        address = getIntent().getStringExtra("KEY");

        //call geocoding function
        geoCoding(address);
        darkSky();
        //map view
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);

//        mapFunction();
    }

    public void darkSky() {
        findCoords http = new findCoords();
        String url = String.format("https://api.darksky.net/forecast/%1$s/%2$s,%3$s", APIDark, latitude, longitude);
        http.changeURL(url);
        http.execute();

        String output = "";
        try{
            output = http.get();
        } catch (Exception e){
            e.printStackTrace();
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(output);

            //parse the JSON Response from darkSKY
            JSONObject response = ((JSONObject)jsonObject).getJSONObject("currently");
            temperature = response.getDouble("temperature");
            humid = response.getDouble("humidity");
            wind = response.getDouble("windSpeed");
            precip = response.getDouble("precipProbability");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayTemp();
    }

    public void displayTemp() {
        //get the textview for each section
        TextView tempView = findViewById(R.id.tempVal);
        TextView humidView = findViewById(R.id.humidVal);
        TextView windView = findViewById(R.id.windVal);
        TextView precipView = findViewById(R.id.precipVal);

        //set values from globals
        //headingView.setText(getString(R.string.random_heading, count));
        tempView.setText(String.valueOf(temperature));
        humidView.setText(String.valueOf(humid));
        windView.setText(String.valueOf(wind));
        precipView.setText(String.valueOf(precip));
    }

    //geocoding thread function
    public void geoCoding(String input) {
        //maybe some global variable issues
        address = input.replace(" ", "+");

        //get type from outside class to connect
        findCoords http = new findCoords();
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%1$s&key=%2$s", address, APIKEY);
        http.changeURL(url);
        http.execute();

        //output holds formatted response from api
//        String output = http.APIData(url);
        String output = null;
        try {
            output = http.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //get latitude and longitude
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(output);

            //parse the JSON Response
            latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").get("lat").toString();
            longitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").get("lng").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(latitude);
        System.out.println(longitude);



//        GetCoordinates thread = new GetCoordinates();
//        thread.execute(address);
//        thread.process();
    }

//    private class GetCoordinates extends AsyncTask<String, Void, String> {
//
//        String output = "";
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            try {
//                String address = strings[0];
//                findCoords http = new findCoords();
//                //Geocode formatting from Google Geocoding API
//                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%1$s&key=%2$s", address, APIKEY);
//
//
//                output = http.APIData(url);
//
//                //get latitude and longitude
//                JSONObject jsonObject = new JSONObject(output);
//
//                //parse the JSON Response
//                latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
//                        .getJSONObject("location").get("lat").toString();
//                longitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
//                        .getJSONObject("location").get("lng").toString();
////                return output;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return output;
//        }

//        public void process() {
//            try {
//                if (output.equals("")) {
//                    throw new Exception();
//                } else {
//                    try {
//                        JSONObject jsonObject = new JSONObject(output);
//
//                        //parse the JSON Response
//                        latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
//                                .getJSONObject("location").get("lat").toString();
//                        longitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
//                                .getJSONObject("location").get("lng").toString();
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    //    public void mapFunction(){
//        Toast mapToast = Toast.makeText(getBaseContext(), "Shania is kinda cool", Toast.LENGTH_SHORT);
//        mapToast.show();
//    }
    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(parseDouble(latitude), parseDouble(longitude));
        Marker pin = map.addMarker(new MarkerOptions().position(sydney).title("Marker"));
        float zoom = 15.0f;
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom));
        map.getUiSettings().setZoomControlsEnabled(true);

    }

    //Google public github resource
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

