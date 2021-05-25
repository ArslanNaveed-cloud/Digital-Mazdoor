package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuyerProfileDetails extends AppCompatActivity {

    ImageView buyerimage;
    TextView buyername,rating,datejoined,email,reviews,city;
    LinearLayout loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_profile_details);
        buyerimage = findViewById(R.id.userimage);
        buyername = findViewById(R.id.username);
        datejoined = findViewById(R.id.datejoined);
        city = findViewById(R.id.city);
        reviews = findViewById(R.id.reviews);
        email = findViewById(R.id.email);
        rating  = findViewById(R.id.rating);
        loader = findViewById(R.id.loader);
        Intent intent = getIntent();
        String buyerid = intent.getStringExtra("buyerid");
        VolleyRequest(buyerid);

    }
    public void VolleyRequest(String buyerid){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(BuyerProfileDetails.this);

        String url = Urls.GET_BUYERDETAILS;
        Log.i("112233",url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject reader = new JSONObject(response);
                            String status = reader.getString("status");

                            if(status.equals("500")){
                                loader.setVisibility(View.GONE);

                                buildDialog(BuyerProfileDetails.this,"502","Internal Server Error").show();
                            }
                            else if(status.equals("404")){
                                loader.setVisibility(View.GONE);

                                buildDialog(BuyerProfileDetails.this,"Access Deined","Email/password is wrong").show();
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                String buyername1 = reader.getString("buyername");
                                String buyeremail1 = reader.getString("buyeremail");
                                String buyerimage1 = reader.getString("buyerimage");
                                String datejoined1 = reader.getString("datejoined");
                                String city1= reader.getString("city");
                                String rating1 = reader.getString("rating");
                                String reviews1 = reader.getString("reviews");

                                buyername.setText(buyername1);
                                email.setText(buyeremail1);
                                datejoined.setText(datejoined1);
                                city.setText(city1);
                                reviews.setText(reviews1);
                                rating.setText(rating1);
                                buyername.setText(buyername1);
                                String url1;
                                String url2;
                                url1  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+buyerimage1;
                                url1 = url1.replace(" ","%20");


                                Glide.with(BuyerProfileDetails.this).load(url1).circleCrop().
                                        into(buyerimage);


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
                buildDialog(BuyerProfileDetails.this,"Error","Please Try Again Later").show();
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
               finish();
            }
        }, 2000);
    }
}