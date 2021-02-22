package com.example.objectifsport.activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
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
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.turf.TurfMeasurement;

import org.threeten.bp.Duration;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


public class DetailedActivityActivity extends AppCompatActivity implements OnMapReadyCallback,
        PermissionsListener {

    private Activity activity;
    private long startTime, timeToSave;
    private boolean timeRunning, timeStarted;
    private boolean distanceRunning = false;
    private boolean distanceStarted = false;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private double totalLineDistance = 0;
    private LatLng lastLocation;
    private int currentLinePosition;
    private Bundle savedInstanceState;
    private String currentGeoJsonSource;
    private ArrayList<String> layersList;

    // distance part views
    private TextView totalDistance;
    private Button startDistanceButton;
    private Button resetDistanceButton;

    // time part views
    private Button resetTimeButton;
    private Button startTimeButton;

    // Variables needed to handle location permissions
    private PermissionsManager permissionsManager;

    // Variables needed to add the location engine
    private LocationEngine locationEngine;
    // Variables needed to listen to location updates
    private final DetailedActivityLocationCallback callback = new DetailedActivityLocationCallback(this);

    // Adjust private static final variables below to change the example's UI
    private static final String STYLE_URI = "mapbox://styles/mapbox/cjv6rzz4j3m4b1fqcchuxclhb";
    private static final int LINE_COLOR = Color.parseColor("#FF6200EE");;
    private static final float LINE_WIDTH = 4f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_detailed_activity);

        activity = DataManager.getActivities().get(getIntent().getIntExtra("position", 0));

        TextView activityDescription = findViewById(R.id.activity_description);
        TextView sportName = findViewById((R.id.sport_name));
        TextView creationDate = findViewById(R.id.creation_date);

        this.savedInstanceState = savedInstanceState;

        if (activity.getSport().getAuthorizedGoals() == 1) // Only time
            setTimeLayout();
        else if (activity.getSport().getAuthorizedGoals() == 2) // Only distance
            setDistanceLayout();
        else { // all
            setTimeLayout();
            setDistanceLayout();
        }

        Button completeUncompleteButton = findViewById(R.id.complete_uncomplete);
        completeUncompleteButton.setText((activity.isAchieved())?
                getResources().getString(R.string.activity_unfinished) :
                getResources().getString(R.string.activity_complete));

        if (resetDistanceButton != null) {
            resetDistanceButton.setEnabled(!activity.isAchieved());
            startDistanceButton.setEnabled(!activity.isAchieved());
        }

        if (resetTimeButton != null) {
            resetTimeButton.setEnabled(!activity.isAchieved());
            startTimeButton.setEnabled(!activity.isAchieved());
        }

        completeUncompleteButton.setOnClickListener(v -> {
            activity.setAchieved(!activity.isAchieved());
            DataManager.save();

            completeUncompleteButton.setText((activity.isAchieved())?
                    getResources().getString(R.string.activity_unfinished) :
                    getResources().getString(R.string.activity_complete));

            if (resetDistanceButton != null) {
                resetDistanceButton.setEnabled(!activity.isAchieved());
                startDistanceButton.setEnabled(!activity.isAchieved());
            }

            if (resetTimeButton != null) {
                resetTimeButton.setEnabled(!activity.isAchieved());
                startTimeButton.setEnabled(!activity.isAchieved());
            }

        });

        activityDescription.setText(activity.getActivityDescription());
        sportName.setText(activity.getSport().getName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        creationDate.setText(formatter.format(activity.getCreationDate()));
    }

    private void setTimeLayout() {
        RelativeLayout timePart = findViewById(R.id.time_part);
        Chronometer chronometer = findViewById(R.id.chronometer);
        startTimeButton = findViewById(R.id.start_pause);
        resetTimeButton = findViewById(R.id.reset);
        TextView savedTime = findViewById(R.id.saved_time);

        timePart.setVisibility(View.VISIBLE);

        timeToSave = activity.getActivityTime();
        timeRunning = false;
        if (timeToSave == 0) timeStarted = false;
        else {
            timeStarted = true;
            startTimeButton.setText(getResources().getString(R.string.resume));
        }

        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
        savedTime.setText((timeToSave == 0) ? getResources().getString(R.string.no_time_recorded)
                : activity.getFormattedActivityTime());

        startTimeButton.setOnClickListener(v -> {
            if (timeRunning) { // chronometer was running
                chronometer.stop();
                timeRunning = false;
                timeToSave += System.currentTimeMillis() - startTime;
                chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
                startTimeButton.setText(getResources().getString(R.string.resume));
                activity.setActivityTime(timeToSave);
                savedTime.setText(activity.getFormattedActivityTime());
                DataManager.save();
            } else { // chronometer was paused / not started
                if (!timeStarted) timeStarted = true;
                startTime = System.currentTimeMillis();
                chronometer.setBase(SystemClock.elapsedRealtime() - timeToSave);
                chronometer.start();
                timeRunning = true;
                startTimeButton.setText(getResources().getString(R.string.stop));
            }
        });

        resetTimeButton.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.reset_time))
                .setMessage(getResources().getString(R.string.reset_time_message))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timeToSave = 0;
                    activity.setActivityTime(0);
                    DataManager.save();
                    savedTime.setText(getResources().getString(R.string.no_time_recorded));
                    startTimeButton.setText(getResources().getString(R.string.start));
                    timeStarted = false;
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }

    private void setDistanceLayout() {
        layersList = new ArrayList<>();
        findViewById(R.id.distance_part).setVisibility(View.VISIBLE); // set the view visible
        totalDistance = findViewById(R.id.distance_travelled);
        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        startDistanceButton = findViewById(R.id.start_stop_tracking);
        startDistanceButton.setOnClickListener(v -> {
            if (!distanceRunning || !distanceStarted) { // we start/resume
                distanceStarted = true;
                distanceRunning = true;
                startDistanceButton.setText(getResources().getString(R.string.stop));
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                        .title(getResources().getString(R.string.start)));
                currentLinePosition = activity.getTrajectories().size();
                activity.getTrajectories().add(new ArrayList<>());
                if (lastLocation != null) {
                    activity.getTrajectories().get(currentLinePosition)
                            .add(Point.fromLngLat(lastLocation.getLongitude(),
                                    lastLocation.getLatitude(),
                                    lastLocation.getAltitude()));
                }

                // Starting the line
                mapboxMap.getStyle(style -> {
                    currentGeoJsonSource = UUID.randomUUID().toString();
                    GeoJsonSource geoJsonSource = new GeoJsonSource(currentGeoJsonSource);
                    geoJsonSource.setGeoJson(Feature.fromGeometry(LineString.fromLngLats(
                            activity.getTrajectories().get(currentLinePosition))));
                    style.addSource(geoJsonSource);
                    layersList.add(geoJsonSource.getId());
                    style.addLayer(new LineLayer(geoJsonSource.getId(), geoJsonSource.getId()).withProperties(
                            lineColor(LINE_COLOR),
                            lineWidth(LINE_WIDTH),
                            lineJoin(LINE_JOIN_ROUND)));
                });
            } else { // we stop
                distanceRunning = false;
                startDistanceButton.setText(getResources().getString(R.string.resume));
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                        .title(getResources().getString(R.string.finish)));
                activity.setCompletedDistance(activity.getCompletedDistance() + totalLineDistance);
                totalLineDistance = 0; // reset distance for next itinerary
                if (lastLocation != null) {
                    activity.getTrajectories().get(currentLinePosition)
                            .add(Point.fromLngLat(lastLocation.getLongitude(),
                                    lastLocation.getLatitude(),
                                    lastLocation.getAltitude()));
                }
                String totalDistanceText = (activity.getCompletedDistance() > 1) ?
                        new DecimalFormat("#.##").format(activity.getCompletedDistance())
                                + " " + getResources().getString(R.string.distance_unit_km) :
                        (int) (activity.getCompletedDistance() * 1000)
                                + " " + getResources().getString(R.string.distance_unit_m);
                totalDistance.setText(totalDistanceText);
                DataManager.save();
            }
        });

        resetDistanceButton = findViewById(R.id.reset_tracking);
        resetDistanceButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.reset_tracking))
                    .setMessage(getResources().getString(R.string.reset_tracking_message))
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        distanceStarted = false;
                        distanceRunning = false;
                        resetDistanceButton.setText(getResources().getString(R.string.start));
                        for (Marker marker : mapboxMap.getMarkers()) {
                            marker.remove();
                        }
                        mapboxMap.getStyle(style -> {
                            for (Layer layer : style.getLayers())
                                if (layersList.contains(layer.getId())) style.removeLayer(layer);
                            for (Source source : style.getSources()) style.removeSource(source);
                        });
                        layersList.clear();
                        totalLineDistance = 0;
                        activity.setCompletedDistance(totalLineDistance);
                        activity.getTrajectories().clear();
                        totalDistance.setText(getResources().getText(R.string.no_distance_travelled));
                        DataManager.save();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    public void backToMyActivities(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        // set displayed distance travelled value
        if (activity.getCompletedDistance() == 0) // prevent the "distance travelled : 0m"
            totalDistance.setText(getResources().getText(R.string.no_distance_travelled));
        else {
            String totalDistanceText = (activity.getCompletedDistance() > 1) ?
                    new DecimalFormat("#.##").format(activity.getCompletedDistance())
                            + " " + getResources().getString(R.string.distance_unit_km) :
                    (int) (activity.getCompletedDistance() * 1000)
                            + " " + getResources().getString(R.string.distance_unit_m);
            totalDistance.setText(totalDistanceText);
        }

        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder()
                .fromUri(STYLE_URI),
                this::enableLocationComponent);

        // init data
        if (!activity.getTrajectories().isEmpty()) {

            for (ArrayList<Point> points : activity.getTrajectories()){
                mapboxMap.getStyle(style -> {
                    GeoJsonSource geoJsonSource = new GeoJsonSource(UUID.randomUUID().toString());
                    geoJsonSource.setGeoJson(Feature.fromGeometry(LineString.fromLngLats(points)));
                    style.addSource(geoJsonSource);
                    layersList.add(geoJsonSource.getId());
                    style.addLayer(new LineLayer(geoJsonSource.getId(), geoJsonSource.getId()).withProperties(
                            lineColor(LINE_COLOR),
                            lineWidth(LINE_WIDTH),
                            lineJoin(LINE_JOIN_ROUND)));
                });

                if(points.size() != 0) {
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(points.get(0).latitude(), points.get(0).longitude()))
                            .title(getResources().getString(R.string.start)));

                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(
                                    points.get(points.size() - 1).latitude(),
                                    points.get(points.size() - 1).longitude()))
                            .title(getResources().getString(R.string.finish)));
                }
            }
        }
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

        long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
        long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
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
        if (activity.getSport().getAuthorizedGoals() != 1) mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (activity.getSport().getAuthorizedGoals() != 1) mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (activity.getSport().getAuthorizedGoals() != 1) mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (activity.getSport().getAuthorizedGoals() != 1) mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (activity.getSport().getAuthorizedGoals() != 1) mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (activity.getSport().getAuthorizedGoals() != 1){
            // Prevent leaks
            if (locationEngine != null) {
                locationEngine.removeLocationUpdates(callback);
            }
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (activity.getSport().getAuthorizedGoals() != 1) mapView.onLowMemory();
    }

    /**
     * Handle the the line data drawing.
     */
    private void addPointToLine() {

        mapboxMap.getStyle(style -> {

            // Get the source from the map's style
            GeoJsonSource geoJsonSource = style.getSourceAs(currentGeoJsonSource);
            if (geoJsonSource != null) {

                Point point = Point.fromLngLat(lastLocation.getLongitude(), lastLocation.getLatitude());
                activity.getTrajectories().get(currentLinePosition).add(point);
                int pointListSize = activity.getTrajectories().get(currentLinePosition).size();
                double distanceBetweenLastAndSecondToLastClickPoint = 0;

                // Make the Turf calculation between the last tap point and the second-to-last tap point.
                if (activity.getTrajectories().get(currentLinePosition).size() >= 2) {
                    distanceBetweenLastAndSecondToLastClickPoint = TurfMeasurement.distance(
                            activity.getTrajectories().get(currentLinePosition).get(pointListSize - 2),
                            activity.getTrajectories().get(currentLinePosition).get(pointListSize - 1));
                }

                // Re-draw the new GeoJSON data
                if (pointListSize >= 2 && distanceBetweenLastAndSecondToLastClickPoint > 0) {

                    // Add the last TurfMeasurement#distance calculated distance to the total line distance.
                    totalLineDistance += distanceBetweenLastAndSecondToLastClickPoint;

                    // Set the specific source's GeoJSON data
                    geoJsonSource.setGeoJson(
                            Feature.fromGeometry(
                                    LineString.fromLngLats(
                                            activity.getTrajectories().get(currentLinePosition))));
                }

            }
        });

    }

    private static class DetailedActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<DetailedActivityActivity> activityWeakReference;

        DetailedActivityLocationCallback(DetailedActivityActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            DetailedActivityActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();
                if (location == null) return;

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                    activity.lastLocation = new LatLng(location.getLatitude(), location.getLongitude(), location.getAltitude());
                    if (activity.distanceRunning) {
                        activity.addPointToLine();
                    }
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
            Log.d("LocationChangeActivity", Objects.requireNonNull(exception.getLocalizedMessage()));
            DetailedActivityActivity activity = activityWeakReference.get();
            if (activity != null)
                Toast.makeText(activity, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}