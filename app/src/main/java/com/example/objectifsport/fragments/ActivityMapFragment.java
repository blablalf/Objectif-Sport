package com.example.objectifsport.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.objectifsport.R;
import com.example.objectifsport.Services.DataManager;
import com.example.objectifsport.model.activities.Activity;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
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

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static android.os.Looper.getMainLooper;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class ActivityMapFragment extends Fragment implements OnMapReadyCallback {

    private boolean distanceRunning = false;
    private boolean distanceStarted = false;
    private double totalLineDistance = 0;
    private int currentLinePosition;
    private Activity activity;
    private LatLng lastLocation;
    private String currentGeoJsonSource;
    private ArrayList<String> layersList;

    // views
    private TextView totalDistance;
    private Button startDistanceButton;
    private Button resetDistanceButton;
    private MapView mapView;
    private MapboxMap mapboxMap;
    View view;

    // Variables needed to add the location engine
    private LocationEngine locationEngine;

    // Variables needed to listen to location updates
    private final ActivityMapFragment.ActivityMapFragmentLocationCallback callback =
            new ActivityMapFragment.ActivityMapFragmentLocationCallback(this);

    // Adjust private static final variables below to change the example's UI
    private static final String STYLE_URI = "mapbox://styles/mapbox/cjv6rzz4j3m4b1fqcchuxclhb";
    private static final int LINE_COLOR = Color.parseColor("#FF6200EE");
    private static final float LINE_WIDTH = 4f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(inflater.getContext(), getString(R.string.mapbox_access_token));
        view = inflater.inflate(R.layout.activity_map_fragment, container, false);

        activity = DataManager.getActivities().get(Objects.requireNonNull(
                getActivity()).getIntent().getIntExtra("position", 0));

        layersList = new ArrayList<>();
        totalDistance = view.findViewById(R.id.distance_travelled);
        mapView = view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        startDistanceButton = view.findViewById(R.id.start_stop_tracking);
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

        resetDistanceButton = view.findViewById(R.id.reset_tracking);
        if (resetDistanceButton != null) {
            resetDistanceButton.setEnabled(!activity.isAchieved());
            startDistanceButton.setEnabled(!activity.isAchieved());
        }
        resetDistanceButton.setOnClickListener(v -> new AlertDialog.Builder(view.getContext())
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
                .show());

        return view;
    }

    public void enablingDisablingTrackingButtons() {
        if (resetDistanceButton != null) {
            resetDistanceButton.setEnabled(!activity.isAchieved());
            startDistanceButton.setEnabled(!activity.isAchieved());
        }
    }

    public MapView getMapView() {
        return mapView;
    }

    public void preventLeak() {
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
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
        if (PermissionsManager.areLocationPermissionsGranted(view.getContext())) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(view.getContext(), loadedMapStyle)
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

        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */

    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(view.getContext());

        long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
        long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    public void treatOnExplanationNeeded() {
        Toast.makeText(view.getContext(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    public void treatOnPermissionResult(boolean granted) {
        if (granted)
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
            else {
                Toast.makeText(view.getContext(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
                Objects.requireNonNull(getActivity()).finish();
            }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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

    private static class ActivityMapFragmentLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<ActivityMapFragment> activityWeakReference;

        ActivityMapFragmentLocationCallback(ActivityMapFragment activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */

        @Override
        public void onSuccess(LocationEngineResult result) {
            ActivityMapFragment fragment = activityWeakReference.get();

            if (fragment != null) {
                Location location = result.getLastLocation();
                if (location == null) return;

                // Pass the new location to the Maps SDK's LocationComponent
                if (fragment.mapboxMap != null && result.getLastLocation() != null) {
                    fragment.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                    fragment.lastLocation = new LatLng(location.getLatitude(), location.getLongitude(), location.getAltitude());
                    if (fragment.distanceRunning) {
                        fragment.addPointToLine();
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
            ActivityMapFragment fragment = activityWeakReference.get();
            if (fragment != null)
                Toast.makeText(fragment.getContext(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show(); // will the context be good ?
        }
    }

}

