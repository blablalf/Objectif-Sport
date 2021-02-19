package com.example.objectifsport.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.activities.Activity;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfMeasurement;


import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


public class DetailedActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private Activity activity;
    private long startTime, timeToSave;
    private boolean timeRunning, timeStarted;
    private boolean distanceRunning, distanceStarted;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private double totalLineDistance = 0;
    private final List<Point> pointList = new ArrayList<>();
    public boolean markerNeeded = false;
    private LatLng lastLocation;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String CIRCLE_LAYER_ID = "CIRCLE_LAYER_ID";
    private static final String LINE_LAYER_ID = "LINE_LAYER_ID";
    public static final int PERMISSION_ID = 1;

    // Variables needed to handle location permissions
    private PermissionsManager permissionsManager;

    // Variables needed to add the location engine
    private LocationEngine locationEngine;
    private final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
    // Variables needed to listen to location updates
    private DetailedActivityLocationCallback callback = new DetailedActivityLocationCallback(this);

    // Adjust private static final variables below to change the example's UI
    private static final String STYLE_URI = "mapbox://styles/mapbox/cjv6rzz4j3m4b1fqcchuxclhb";
    private static final int CIRCLE_COLOR = Color.parseColor("#FF6200EE");
    private static final int LINE_COLOR = CIRCLE_COLOR;
    private static final float CIRCLE_RADIUS = 2f;
    private static final float LINE_WIDTH = 4f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_detailed);

        activity = DataManager.getActivities().get(getIntent().getIntExtra("position", 0));

        TextView activityDescription = findViewById(R.id.activity_description);
        TextView sportName = findViewById((R.id.sport_name));
        TextView creationDate = findViewById(R.id.creation_date);

        // ne pas oublier de rendre le layout temps et distance gone dans le xml

        if (activity.getSport().getAuthorizedGoals() == 1) // Only time
            setTimeLayout();
        else if (activity.getSport().getAuthorizedGoals() == 2) // Only distance
            setDistanceLayout(savedInstanceState);
        else {
            setTimeLayout();
            setDistanceLayout(savedInstanceState);
        }

        activityDescription.setText(activity.getActivityDescription());
        sportName.setText(activity.getSport().getName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        creationDate.setText(formatter.format(activity.getCreationDate()));
    }

    private void setTimeLayout() {
        RelativeLayout timePart = findViewById(R.id.time_part);
        Chronometer chronometer = findViewById(R.id.chronometer);
        Button start = findViewById(R.id.start_pause);
        Button reset = findViewById(R.id.reset);
        TextView savedTime = findViewById(R.id.saved_time);

        timePart.setVisibility(View.VISIBLE);

        timeToSave = activity.getActivityTime();
        timeRunning = false;
        if (timeToSave == 0) timeStarted = false;
        else {
            timeStarted = true;
            start.setText(getResources().getString(R.string.resume));
        }

        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
        savedTime.setText((timeToSave == 0) ? getResources().getString(R.string.no_time_recorded) : activity.getFormattedActivityTime());

        start.setOnClickListener(v -> {
            if (timeRunning) { // chronometer was running
                chronometer.stop();
                timeRunning = false;
                timeToSave += System.currentTimeMillis() - startTime;
                chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
                start.setText(getResources().getString(R.string.resume));
                activity.setActivityTime(timeToSave);
                savedTime.setText(activity.getFormattedActivityTime());
                DataManager.save();
            } else { // chronometer was paused / not started
                if (!timeStarted) timeStarted = true;
                startTime = System.currentTimeMillis();
                chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
                chronometer.start();
                timeRunning = true;
                start.setText(getResources().getString(R.string.stop));
            }
        });

        reset.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.reset_time))
                .setMessage(getResources().getString(R.string.reset_time_message))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timeToSave = 0;
                    activity.setActivityTime(0);
                    DataManager.save();
                    savedTime.setText(getResources().getString(R.string.no_time_recorded));
                    timeStarted = false;
                    start.setText(getResources().getString(R.string.start));
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }

    private void setDistanceLayout(Bundle savedInstanceState) {
        findViewById(R.id.distance_part).setVisibility(View.VISIBLE); // set the view visible
        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Button startStop = findViewById(R.id.start_stop_tracking);
        startStop.setOnClickListener(v -> {
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                    .title(getResources().getString(R.string.start)));
        });

    }

    public void backToMyActivities(View view) {
        onBackPressed();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        //DetailedActivity.this.mapboxMap.addOnMapClickListener(DetailedActivity.this);
        mapboxMap.setStyle(new Style.Builder()
                .fromUri(STYLE_URI)

                // Add the source to the map
                .withSource(new GeoJsonSource(SOURCE_ID,
                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

                // Style and add the CircleLayer to the map
                .withLayer(new CircleLayer(CIRCLE_LAYER_ID, SOURCE_ID).withProperties(
                        circleColor(CIRCLE_COLOR),
                        circleRadius(CIRCLE_RADIUS)
                ))

                // Style and add the LineLayer to the map. The LineLayer is placed below the CircleLayer.
                .withLayerBelow(new LineLayer(LINE_LAYER_ID, SOURCE_ID).withProperties(
                        lineColor(LINE_COLOR),
                        lineWidth(LINE_WIDTH),
                        lineJoin(LINE_JOIN_ROUND)
                ), CIRCLE_LAYER_ID),

                this::enableLocationComponent); // travailler sur un style en fonction de l'heure
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {

            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted)
            if (mapboxMap.getStyle() != null)
                enableLocationComponent(mapboxMap.getStyle());
        else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Handle the map click location and re-draw the circle and line data.
     *
     * @param latLng where the map was tapped on.
     */
    private void addPointToLine(@NonNull LatLng latLng) {

        lastLocation = latLng;

        mapboxMap.getStyle(style -> {

            // Get the source from the map's style
            GeoJsonSource geoJsonSource = style.getSourceAs(SOURCE_ID);
            if (geoJsonSource != null) {

                Point point = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());

                pointList.add(point);

                int pointListSize = pointList.size();

                double distanceBetweenLastAndSecondToLastClickPoint = 0;

                // Make the Turf calculation between the last tap point and the second-to-last tap point.
                if (pointList.size() >= 2) {
                    distanceBetweenLastAndSecondToLastClickPoint = TurfMeasurement.distance(
                            pointList.get(pointListSize - 2), pointList.get(pointListSize - 1));
                }

                // Re-draw the new GeoJSON data
                if (pointListSize >= 2 && distanceBetweenLastAndSecondToLastClickPoint > 0) {

                    // Add the last TurfMeasurement#distance calculated distance to the total line distance.
                    totalLineDistance += distanceBetweenLastAndSecondToLastClickPoint;

                    // Set the specific source's GeoJSON data
                    geoJsonSource.setGeoJson(Feature.fromGeometry(LineString.fromLngLats(pointList)));
                }
            }
        });
    }

    private static class DetailedActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<DetailedActivity> activityWeakReference;

        DetailedActivityLocationCallback(DetailedActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            DetailedActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();
                if (location == null) return;

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null){
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                    LatLng lastLocation = new LatLng(result.getLastLocation());
                    activity.addPointToLine(lastLocation);
                }

            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            DetailedActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}