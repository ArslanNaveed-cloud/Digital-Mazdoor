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
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.sayantan.advancedspinner.SingleSpinner;
import com.sayantan.advancedspinner.SpinnerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendOffer extends AppCompatActivity {

    private TextInputEditText description,price;
    private TextView numbofdays;
    private  String numberofdays="",budget="",descrip="",buyername="",buyerid="",id="",freelancername="",category="",postid="";
    Button sendoffer;

    String[] deliverydays = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
    List<String> deliverydataarraylist;
    private SingleSpinner days;
    private String freeelancerid;
    LinearLayout loader;
    int count= 0;
    private String buyerpicture;
    private String freelancerpicture;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_offer);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        numbofdays = findViewById(R.id.numberofdays);
        sendoffer = findViewById(R.id.sendoffer);
        deliverydataarraylist = Arrays.asList(deliverydays);
        days = findViewById(R.id.days);
        days.setSpinnerList(deliverydataarraylist);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
        Intent intent = getIntent();
                buyername = intent.getStringExtra("buyername");
                buyerid = intent.getStringExtra("buyerid");
                category = intent.getStringExtra("category");
                postid = intent.getStringExtra("postid");
        buyerpicture = intent.getStringExtra("buyerpicture");
        title = intent.getStringExtra("title");
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        freelancername = sharedPreferences.getString("username","");
            freeelancerid = sharedPreferences.getString("id","");
        freelancerpicture = sharedPreferences.getString("profilepicture","");
        days.addOnItemChoosenListener(new SpinnerListener() {
            @Override
            public void onItemChoosen(String s, int i) {
                if(s!=null){
                    numbofdays.setText(s);
                    numberofdays = s;
                }else{
                    Toast.makeText(SendOffer.this, "Selection Cleared", Toast.LENGTH_SHORT).show();

                    numbofdays.setText("Select Your Working Category");
                }
            }
        });

        sendoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                if(count == 0){
                    checkcontents();
                }
                else{
                    Toast.makeText(SendOffer.this, "Please Wait..", Toast.LENGTH_SHORT).show();
                    count++;
                }

            }
        });
    }

    private void checkcontents() {
        budget = price.getText().toString().trim();
        descrip = description.getText().toString().trim();

        if(budget.isEmpty() || budget.equals("")){
            price.setError("Field Cannot Be Empty");
        }else if(descrip.isEmpty() || descrip.equals("")){
            description.setError("Field Cannot Be Empty");

        }else if(numberofdays.isEmpty() || numberofdays.equals("")){
            Toast.makeText(this, "Please Select Number Of Days", Toast.LENGTH_SHORT).show();
        }else{
            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(budget);
            boolean b = m.find();
            if(b){
                price.setError("Incorrect Budget");
            }else{
                VolleyRequest();
            }
        }
    }


    public void VolleyRequest(){
        Toast.makeText(SendOffer.this, "Please Wait While We Send Your Offer", Toast.LENGTH_LONG).show();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(SendOffer.this);

        String url = Urls.SEND_OFFER;
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
                                buildDialog2(SendOffer.this,"Please Try Again Later","Internal Server Error").show();
                            }
                            else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                String pid = sharedPreferences.getString("postid","");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String post = pid+" "+postid;
                                editor.putString("postid",post);
                                editor.commit();
                                buildDialog2(SendOffer.this,"Offer Sent Successfully","Please Check Your Orders Daily For Updates").show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loader.setVisibility(View.GONE);

                buildDialog2(SendOffer.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("buyername", buyername);
                params.put("buyerid", buyerid);
                params.put("freelancername", freelancername);
                params.put("freelancerid", freeelancerid);
                params.put("description", descrip);
                params.put("budget", budget);
                params.put("category",category);
                params.put("numberofdays",numberofdays);
                params.put("postid",postid);
                params.put("freelancerpicture",freelancerpicture);
                params.put("buyerpicture",buyerpicture);
                params.put("title",title);

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
    public AlertDialog.Builder buildDialog2(Context c, String header, String message) {

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