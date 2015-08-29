package app.porter.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.porter.com.datastore.parcel;
import app.porter.com.network.AppController;
import app.porter.com.viewmanager.MyRecyclerAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private  MyRecyclerAdapter adapter;
    private String api="null";
    private  int number_parcel = 0;
    private  ArrayList<parcel> parcels;
    public  final static String SER_KEY = "com.myporter.obj.ser";
    private Boolean isDataLoadedFromNetwork=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        networkApicall();
    }

    public void networkApicall()
    {

            JsonObjectRequest jreq = new JsonObjectRequest("https://porter.0x10.info/api/parcel?type=json&query=list_parcel",
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("parcels");
                                parcels  = new ArrayList<parcel>(jsonArray.length());
                                number_parcel = jsonArray.length();
                                Log.d("resp", String.valueOf(response.length()));
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    Log.d("json", jo.toString());
                                    Double latitude = jo.getJSONObject("live_location").getDouble("latitude");
                                    Double longitude = jo.getJSONObject("live_location").getDouble("longitude");
                                    parcel cr = new parcel(
                                            jo.getString("name"),
                                            jo.getString("image"),
                                            jo.getString("date"),
                                            jo.getString("type"),
                                            jo.getString("weight"),
                                            jo.getString("phone"),
                                            jo.getString("price"),
                                            jo.getInt("quantity"),
                                            jo.getString("color"),
                                            jo.getString("link"),
                                            latitude, longitude
                                    );

                                    parcels.add(cr);


                                adapter = new MyRecyclerAdapter(MainActivity.this,parcels);
                                mRecyclerView.setAdapter(adapter);
                                    isDataLoadedFromNetwork=true;
                                    final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

                                        @Override public boolean onSingleTapUp(MotionEvent e) {
                                            return true;
                                        }

                                    });


                                    mRecyclerView.addOnItemTouchListener(new OnItemTouchListener() {
                                        @Override
                                        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                                            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                                            if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                                             Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildAdapterPosition(child), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(),Details.class);
                                                Bundle mBundle = new Bundle();
                                                mBundle.putSerializable(SER_KEY,parcels.get(recyclerView.getChildAdapterPosition(child)));
                                                intent.putExtras(mBundle);
                                                startActivity(intent);
                                                return true;

                                            }

                                            return false;
                                        }

                                        @Override
                                        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                                        }

                                        @Override
                                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                                        }
                                    });



                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("err",error.toString());
                }
            });
        JsonObjectRequest req = new JsonObjectRequest("https://porter.0x10.info/api/parcel?type=json&query=api_hits",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            api = response.getString("api_hits");
                            TextView api_hits = (TextView)findViewById(R.id.api);
                            api_hits.setText("API HITS: " + api);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jreq, "jreq");
        AppController.getInstance().addToRequestQueue(req, "api_hits");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
    if(isDataLoadedFromNetwork) {
        if (id == R.id.sortweight) {
            Collections.sort(parcels, new WeightComparator());
            adapter.updateList(parcels);
            return true;
        }
        if (id == R.id.sortname) {
            Collections.sort(parcels, new NameComparator());
            adapter.updateList(parcels);
            return true;
        }
        if (id == R.id.sortcost) {
            Collections.sort(parcels, new CostComparator());
            adapter.updateList(parcels);
            return true;
        }

    }else
    {
        Toast.makeText(getApplicationContext(),"Sorry! No DataSet.",Toast.LENGTH_LONG);
    }
        return super.onOptionsItemSelected(item);
    }
    public class WeightComparator implements Comparator<parcel> {
        @Override
        public int compare(parcel o1, parcel o2) {
            return o1.getWeight().compareTo(o2.getWeight());
        }
    }

    public class NameComparator implements Comparator<parcel> {
        @Override
        public int compare(parcel o1, parcel o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public class CostComparator implements Comparator<parcel> {
        @Override
        public int compare(parcel o1, parcel o2) {
            Double p1=Double.parseDouble(o1.getPrice().replaceAll(",",""));
            Double p2=Double.parseDouble(o2.getPrice().replaceAll(",",""));

            return p1.compareTo(p2);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
