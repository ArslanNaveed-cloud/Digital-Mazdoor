package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

public class FreelancerProfileDetails extends AppCompatActivity {

    ImageView buyerimage;
    TextView buyername,rating,datejoined,email,reviews,city,maincategory,subbcategory,profilesummary,ordercompleted;
    LinearLayout loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_profile_details);

        buyerimage = findViewById(R.id.userimage);
        buyername = findViewById(R.id.username);
        datejoined = findViewById(R.id.datejoined);
        city = findViewById(R.id.city);
        reviews = findViewById(R.id.reviews);
        email = findViewById(R.id.email);
        rating  = findViewById(R.id.rating);
        maincategory = findViewById(R.id.maincat);
        subbcategory = findViewById(R.id.subcat);
        profilesummary = findViewById(R.id.profilesummary);
        ordercompleted = findViewById(R.id.orders);
        loader = findViewById(R.id.loader);
        Intent intent = getIntent();
        String freelancerid = intent.getStringExtra("freelancerid");
        VolleyRequest(freelancerid);
    }

    public void VolleyRequest(String freelancerid){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(FreelancerProfileDetails.this);

        String url = Urls.GET_FREELANCERDETAILS;
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

                                buildDialog(FreelancerProfileDetails.this,"502","Internal Server Error").show();
                            }
                            else if(status.equals("404")){
                                loader.setVisibility(View.GONE);

                                buildDialog(FreelancerProfileDetails.this,"Access Deined","Email/password is wrong").show();
                            }else if(status.equals("200")){

                                loader.setVisibility(View.GONE);
                                String buyername1 = reader.getString("freelancername");
                                String buyeremail1 = reader.getString("freelanceremail");
                                String buyerimage1 = reader.getString("freelancerimage");
                                String datejoined1 = reader.getString("datejoined");
                                String city1= reader.getString("freelancercity");
                                String rating1 = reader.getString("freelancerrating");
                                String reviews1 = reader.getString("freelancerreviews");
                                String profilesummary1 = reader.getString("profilesummary");
                                String maincategory1 = reader.getString("maincategory");
                                String ordercompleted1 = reader.getString("ordercompleted");
                                String subcategory = reader.getString("subcategory");

                                buyername.setText(buyername1);
                                email.setText(buyeremail1);
                                datejoined.setText(datejoined1);
                                city.setText(city1);
                                reviews.setText(reviews1);
                                rating.setText(rating1);
                                ordercompleted.setText(ordercompleted1);
                                maincategory.setText(maincategory1);
                                profilesummary.setText(profilesummary1);
                                subbcategory.setText(subcategory);
                                String url1;
                                String url2;
                                url1  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+buyerimage1;
                                url1 = url1.replace(" ","%20");


                                Glide.with(FreelancerProfileDetails.this).load(url1).circleCrop().
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
                buildDialog(FreelancerProfileDetails.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("freelancerid", freelancerid);

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