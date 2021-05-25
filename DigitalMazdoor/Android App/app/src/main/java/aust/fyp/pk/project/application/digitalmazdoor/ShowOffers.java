package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowOffers extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout loader,mylayout;
    TextView message;
    ShowOffersAdpahter adaphter;
    ProgressBar progress_circular;
    private ArrayList<NewRequestDataModel> newRequestDataModels;
    private String city;
    private String category;
    private String buyerid;
    private String freelancerpicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offers);

        recyclerView = findViewById(R.id.sentofferslist);
        message = findViewById(R.id.message);
        mylayout =findViewById(R.id.mylayout);
        newRequestDataModels = new ArrayList<>();
        progress_circular = findViewById(R.id.progress_circular);
        loader = findViewById(R.id.loader);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        city = sharedPreferences.getString("city","");
        buyerid = sharedPreferences.getString("id","");

        VolleyRequest();

    }
    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ShowOffers.this);

        String url = Urls.GET_OFFERSFORBUYERS;
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

                                buildDialog(ShowOffers.this,"Please Try Again Later","Internal Server Error").show();
                            }
                            else if(status.equals("404")){
                                progress_circular.setVisibility(View.GONE);
                                message.setVisibility(View.VISIBLE);

                                message.setText("You Didnot Recieved any Offers");
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                mylayout.setVisibility(View.GONE);

                                JSONArray postid = reader.getJSONArray("postid");
                                JSONArray id = reader.getJSONArray("id");
                                JSONArray name = reader.getJSONArray("name");
                                JSONArray description = reader.getJSONArray("description");
                                JSONArray budget = reader.getJSONArray("budget");
                                JSONArray category = reader.getJSONArray("category");
                                JSONArray buyerpic = reader.getJSONArray("buyerpicture");

                                JSONArray title = reader.getJSONArray("title");
                                JSONArray days = reader.getJSONArray("days");
                                JSONArray bids = reader.getJSONArray("bids");
                                JSONArray date = reader.getJSONArray("date");
                                JSONArray orderstatus = reader.getJSONArray("orderstatus");

                                for (int i = 0; i < postid.length(); i++) {

                                    String pid = postid.getString(i);
                                    String ptitle = title.getString(i);
                                    String pprice = budget.getString(i);
                                    String pdescription = description.getString(i);
                                    String pcategory = category.getString(i);
                                    String bpic = buyerpic.getString(i);
                                    int pbids = 0;

                                    String pdate = date.getString(i);
                                    String pdays = days.getString(i);
                                    String buyerid = id.getString(i);
                                    String bname = name.getString(i);
                                    String ostatus = orderstatus.getString(i);
                                    NewRequestDataModel newRequestDataModel = new NewRequestDataModel(ptitle,pdescription,pcategory,pprice,pdays,bpic,pid,buyerid,bname,pdate,pbids,ostatus);
                                    newRequestDataModels.add(newRequestDataModel);
                                }

                                adaphter = new ShowOffersAdpahter(newRequestDataModels,ShowOffers.this,ShowOffers.this);

                                recyclerView.setLayoutManager(new LinearLayoutManager(ShowOffers.this));
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
                buildDialog(ShowOffers.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("buyerid", buyerid);

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

    public void endactivity() {
        if(adaphter!=null){
            adaphter.notifyDataSetChanged();
            finish();
        }

    }
}