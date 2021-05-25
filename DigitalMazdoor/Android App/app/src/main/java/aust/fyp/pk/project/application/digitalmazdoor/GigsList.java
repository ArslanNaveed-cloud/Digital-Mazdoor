package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GigsList extends AppCompatActivity {
    LocationManager locationManager;
//    protected GoogleApiClient mGoogleApiClient;
//    protected LocationRequest locationRequest;
//    int REQUEST_CHECK_SETTINGS = 100;
    private String district ="";
    private static String catname="";
    private LinearLayout loader;
    private CoordinatorLayout mainlayout;
    private static String apikey;
    private  String laborname;
    private String laborexperience;
    private String ratingtext;
    private String ordercompleted;
    private String laborpicture;
    private TextView headingtxt,browse;
    private Toolbar toolbar;
    private ArrayList<GigsItemDataModel> arrayList;
    private CollapsingToolbarLayout collapsibleToolbar;
    private AppBarLayout appBarLayout;
    private String ldescription,lphone,lcity,lcnic;
    private String lportfolioimages;
    private GigsAdapter adaphter;
    private RecyclerView recyclerView;
    private String title;
    private String category;
    private String description;
    private String price;
    private String deliverytime;
    private String profileimage;
    private String name;
    private String rating;
    private String orderscompleted;
    private String city;
    private FloatingActionButton fb_1;
    private boolean isuserloggedin;
    public boolean isaction_mode = false;
    private TextView toolbartext;
    private ProgressBar progress_circular;
    private ArrayList<GigsItemDataModel> mygigsselectionlist,gigDataModels;
    private String id;
    private String username;
    private LinearLayout mylayout;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gigs_list2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        message = findViewById(R.id.message);
        message.setVisibility(View.GONE);
        gigDataModels = new ArrayList<>();
        progress_circular = findViewById(R.id.progress_circular);

        catname = intent.getStringExtra("catname");
        getSupportActionBar().setTitle("  "+catname);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        city = sharedPreferences.getString("city","");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        collapsibleToolbar = findViewById(R.id.collapsetoolbar);
        appBarLayout = findViewById(R.id.appbarlayout);
        appBarLayout.setExpanded(true);
        headingtxt= findViewById(R.id.headingtxt);
        loader = findViewById(R.id.loader);
        VolleyRequest();
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    headingtxt.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    headingtxt.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });



    }

    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(GigsList.this);

        String url = Urls.GET_GIGSFRBUYER;
        Log.i("112233",url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject reader = new JSONObject(response);
                            Log.d("GiGLIST", "onResponse: "+reader);
                            String status = reader.getString("status");

                            if(status.equals("500")){
                                loader.setVisibility(View.GONE);

                                buildDialog(GigsList.this,"Please Try Again Later","Internal Server Error").show();
                            }
                            else if(status.equals("404")){
                                progress_circular.setVisibility(View.GONE);
                                message.setVisibility(View.VISIBLE);

                                message.setText("Cannot Find Any Gigs In Your City "+city);
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                JSONArray gigid = reader.getJSONArray("gigid");
                                JSONArray gigtitle = reader.getJSONArray("gigtitle");
                                JSONArray gigprice = reader.getJSONArray("gigprice");
                                JSONArray gigdescription = reader.getJSONArray("gigdescription");
                                JSONArray gigcoverimage = reader.getJSONArray("gigcoverimage");
                                JSONArray gigcategory = reader.getJSONArray("gigcategory");
                                JSONArray gigdateofuploading = reader.getJSONArray("gigdateofuploading");
                                JSONArray gigdelliveytime = reader.getJSONArray("gigdeliverytime");

                                JSONArray fid = reader.getJSONArray("freelancerid");
                                JSONArray fname = reader.getJSONArray("freelancernames");
                                JSONArray frating = reader.getJSONArray("freelancerrating");
                                JSONArray fimage = reader.getJSONArray("freelancerprofilepics");
                                JSONArray ftotalorders = reader.getJSONArray("freelancerorders");
                                JSONArray fcity = reader.getJSONArray("freelancercity");
                                JSONArray ftotalreviews = reader.getJSONArray("freelancertotalreviews");
                                JSONArray fprofilesummary = reader.getJSONArray("freelancerprofilesummary");

                                for (int i = 0; i < gigid.length(); i++) {
                                    String id = gigid.getString(i);
                                    String title = gigtitle.getString(i);
                                    String price = gigprice.getString(i);
                                    String description = gigdescription.getString(i);
                                    String coverimage = gigcoverimage.getString(i);
                                    String cateogry = gigcategory.getString(i);
                                     String deliverytime = gigdelliveytime.getString(i);
                                    String dateofupload = gigdateofuploading.getString(i);

                                    String fID= fid.getString(i);
                                    String flname = fname.getString(i);
                                    String flimage = fimage.getString(i);
                                    String flrating = frating.getString(i);
                                    String fltotalorders = ftotalorders.getString(i);
                                    String flcity = fcity.getString(i);
                                    String flprofilesummary = fprofilesummary.getString(i);
                                    String fltotalreviews = ftotalreviews.getString(i);

                                    GigsItemDataModel itemDataModel = new GigsItemDataModel(title,cateogry,description,price,deliverytime,flimage,flname,flrating,fltotalorders,flcity,coverimage,dateofupload,flprofilesummary,fltotalreviews,id,fID);
                                    gigDataModels.add(itemDataModel);
                                }

                                adaphter = new GigsAdapter(gigDataModels);
                                recyclerView = findViewById(R.id.gigslist);
                                recyclerView.setLayoutManager(new LinearLayoutManager(GigsList.this));
                                recyclerView.setAdapter(adaphter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loader.setVisibility(View.VISIBLE);
                buildDialog(GigsList.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category", catname);
                params.put("city",city);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
    public AlertDialog.Builder buildDialog(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        waitforsometime();

        return builder;
    }
    public void waitforsometime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                    super.onBackPressed();

                break;

            case R.id.nav_search:
                Toast.makeText(GigsList.this, "Search Labor", Toast.LENGTH_SHORT).show();
                final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        if(adaphter == null){
                            Toast.makeText(GigsList.this, "Could not find any data", Toast.LENGTH_SHORT).show();
                        }else {
                            adaphter.getFilter().filter(newText);
                        }
                        return true;


                    }
                });
                return true;


        }

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboardmenu, menu);
        return true;
    }
}