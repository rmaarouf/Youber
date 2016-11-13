package com.youber.cmput301f16t15.youber.gui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.youber.cmput301f16t15.youber.misc.GeoLocation;
import com.youber.cmput301f16t15.youber.R;
import com.youber.cmput301f16t15.youber.requests.Request;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverMainActivity extends AppCompatActivity {

    //TODO use controller not global
    private Request request;
    private CoordinatorLayout layout;

    Activity ourActivity = this;
    MapView map;
    Road[] mRoads;

    long start;
    long stop;
    int x, y;
    GeoPoint touchedPoint;
    ArrayList<OverlayItem> overlayItemArray = new ArrayList<>();
    GeoPoint searchPoint;
    LocationManager locationManager;
    String towers;
    int lat = 0;
    int lon = 0;

    static double radius = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);



        IMapController mapController = map.getController();
        mapController.setZoom(12);
        GeoPoint EdmontonGPS = new GeoPoint(53.521609, -113.530633);
        mapController.setCenter(EdmontonGPS);

        Touch t = new Touch();
        List<Overlay> overlayList = map.getOverlays();
        overlayList.add(t);

        Button searchByKeyword = (Button) findViewById(R.id.search_keyword_button);
        searchByKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String keyword = ((EditText)findViewById(R.id.keyword_search)).getText().toString();


                Intent intent = new Intent(DriverMainActivity.this, DriverSearchListActivity.class);
                intent.putExtra("Keyword",keyword);
                startActivity(intent);

            }
        });

        Button searchByGeolocation = (Button) findViewById(R.id.search_geolocation_button);
        searchByGeolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeoLocation geoLocation = new GeoLocation(searchPoint.getLatitude(), searchPoint.getLongitude());

                Intent intent = new Intent(DriverMainActivity.this,DriverSearchListActivity.class);
                intent.putExtra("GeoLocation", (Parcelable) geoLocation);
                intent.putExtra("Radius",radius);
                startActivity(intent);


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

            return true;
        }
        else if (id == R.id.action_view_requests) {
            Intent intent = new Intent(this, RequestViewActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class Touch extends Overlay {

        @Override
        protected void draw(Canvas canvas, MapView mapView, boolean b) {

        }

        public boolean onTouchEvent(MotionEvent e, MapView m) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                start = e.getEventTime();
                x = (int) e.getX();
                y = (int) e.getY();
                touchedPoint = (GeoPoint) map.getProjection().fromPixels(x, y);
            }
            if (e.getAction() == MotionEvent.ACTION_UP) {
                stop = e.getEventTime();
            }
            if (stop - start > 1000) {
                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    List<Address> address = geocoder.getFromLocation(touchedPoint.getLatitude(), touchedPoint.getLongitude(), 1);
                    if (address.size() > 0) {
                        String display = "Latitude: " + touchedPoint.getLatitude() + "\n" + "Longitude: " + touchedPoint.getLongitude() + "\n";
                        for (int i = 0; i < address.get(0).getMaxAddressLineIndex(); i++) {
                            display += address.get(0).getAddressLine(i) + "\n";
                        }
                        Toast t = Toast.makeText(getBaseContext(), display, Toast.LENGTH_LONG);
                        //t.show();

                        if (searchPoint == null) {
                            searchPoint = new GeoPoint(touchedPoint.getLatitude(), touchedPoint.getLongitude());
                            String searchtLat = String.valueOf(searchPoint.getLatitude());
                            String searchLon = String.valueOf(searchPoint.getLongitude());
                            Marker searchMarker = new Marker(map);
                            searchMarker.setPosition(searchPoint);
                            searchMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                            searchMarker.setTitle("search point");
                            map.getOverlays().add(searchMarker);
                            map.invalidate();

                            //http://stackoverflow.com/questions/6424032/android-seekbar-in-dialog
                            AlertDialog.Builder searchRadiusDialog = new AlertDialog.Builder(ourActivity);
                            LayoutInflater inflater = (LayoutInflater)ourActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                            final  View layout = inflater.inflate(R.layout.search_geolocation_dialog, (ViewGroup)findViewById(R.id.search_radius_dialog));
                            final TextView radiusText = (TextView)layout.findViewById(R.id.result);
                            radiusText.setText("1 meters");

                            searchRadiusDialog.setTitle("please set radius to search");
                            searchRadiusDialog.setView(layout);



//                            Button searchRadiusButton = (Button)layout.findViewById(R.id.search_radius_button);
                            SeekBar searchRadiusSeekbar = (SeekBar)layout.findViewById(R.id.search_radius_seekbar);
                            searchRadiusSeekbar.setMax(2000);
                            searchRadiusSeekbar.setProgress(1);
                            searchRadiusSeekbar.setKeyProgressIncrement(50);


                            SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int result, boolean b) {
                                    radius = result;
                                    radiusText.setText(result + " meters");

                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                }
                            };
                            searchRadiusSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);

                            searchRadiusDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Polygon circle = new Polygon();
                                    circle.setPoints(Polygon.pointsAsCircle(searchPoint, radius));
                                    circle.setFillColor(0x6984e1e1);
                                    circle.setStrokeColor(Color.CYAN);
                                    circle.setStrokeWidth(2);
                                    map.getOverlays().add(circle);
                                    map.invalidate();

                                    dialogInterface.dismiss();
                                }
                            });

                            searchRadiusDialog.show();

                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {

                }
            }

            return false;
        }
    }

    public void getRoadAsync(GeoPoint startPoint, GeoPoint destinationPoint) {
        mRoads = null;

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(destinationPoint);
        new UpdateRoadTask().execute(waypoints);
    }


    private class UpdateRoadTask extends AsyncTask<Object, Void, Road[]> {

        protected Road[] doInBackground(Object... params) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> waypoints = (ArrayList<GeoPoint>) params[0];
            RoadManager roadManager = new MapQuestRoadManager("9EEnjA3zxWdtQSkkUxB7QKAo0jLpGaCb");
            return roadManager.getRoads(waypoints);
        }

        @Override
        protected void onPostExecute(Road[] roads) {
            mRoads = roads;
            if (roads == null)
                return;
            if (roads[0].mStatus == Road.STATUS_TECHNICAL_ISSUE)
                Toast.makeText(map.getContext(), "Technical issue when getting the route", Toast.LENGTH_SHORT).show();
            else if (roads[0].mStatus > Road.STATUS_TECHNICAL_ISSUE) //functional issues
                Toast.makeText(map.getContext(), "No possible route here", Toast.LENGTH_SHORT).show();
            Polyline[] mRoadOverlays = new Polyline[roads.length];
            List<Overlay> mapOverlays = map.getOverlays();
            for (int i = 0; i < roads.length; i++) {
                Polyline roadPolyline = RoadManager.buildRoadOverlay(roads[i]);
                mRoadOverlays[i] = roadPolyline;
                String routeDesc = roads[i].getLengthDurationText(ourActivity, -1);
                roadPolyline.setTitle(getString(R.string.app_name) + " - " + routeDesc);
                roadPolyline.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
                roadPolyline.setRelatedObject(i);
                mapOverlays.add(roadPolyline);
                //selectRoad(0);
                map.invalidate();
                //we insert the road overlays at the "bottom", just above the MapEventsOverlay,
                //to avoid covering the other overlays.
            }
        }
    }
}
