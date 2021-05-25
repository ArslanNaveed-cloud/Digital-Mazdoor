package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestDescriptiom extends AppCompatActivity {

    private TextView title,description,category,budget,deliveryday,timeofposting,status,bids;
    private Button viewrequest,deleterequests;
    String pid ;
    String ptitle;
    String pprice ;
    String pdescription;
    String pcategory ;
    String bpic;
    String pbids ;
    String pstatus;
    private LinearLayout loader;
    String pdate;
    String bcity;
    String pdays ;
    int count= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_descriptiom);
        final Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
            pid = intent.getStringExtra("pid");
            ptitle = intent.getStringExtra("ptitle");
            pprice = intent.getStringExtra("pprice");
            pdescription = intent.getStringExtra("pdescription");
            pcategory = intent.getStringExtra("pcategory");
            bpic = intent.getStringExtra("bpic");
            pbids = intent.getStringExtra("pbids");
            pstatus = intent.getStringExtra("pstatus");
            pdate = intent.getStringExtra("pdate");
            pdate = intent.getStringExtra("pdate");
            pdays = intent.getStringExtra("pdays");

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        budget = findViewById(R.id.budget);
        deliveryday = findViewById(R.id.deliverydays);
        timeofposting = findViewById(R.id.date);
        status = findViewById(R.id.poststatus);
        bids = findViewById(R.id.bids);



        title.setText(ptitle);
        description.setText(pdescription);
        category.setText(pcategory);
        budget.setText(pprice);
        deliveryday.setText(pdays);
        timeofposting.setText(pdate);
        status.setText(pstatus);
        bids.setText(pbids);

        viewrequest = findViewById(R.id.viewrequest);
        viewrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RequestDescriptiom.this,ShowOffers.class);
                startActivity(intent1);

            }
        });


        deleterequests = findViewById(R.id.delete_btn);
        deleterequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0){
                    count++;
                    buildDialog(RequestDescriptiom.this,"Are You Sure","Following Request Would Be Deleted").show();

                }else{
                    Toast.makeText(RequestDescriptiom.this, "Please Wait..", Toast.LENGTH_SHORT).show();
                }

                }
        });



    }
    public AlertDialog.Builder buildDialog(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loader.setVisibility(View.VISIBLE);
                    DeleteRequest();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }
    public AlertDialog.Builder buildDialog2(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

       waitforsometime();

        return builder;
    }
    public void DeleteRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(RequestDescriptiom.this);

        String url = Urls.DELETE_REQUEST;
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

                                buildDialog(RequestDescriptiom.this,"Please Try Again Later","Internal Server Error").show();
                            }
                            else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                buildDialog2(RequestDescriptiom.this,"Congrats.!","Request Deleted Successfully").show();
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
                buildDialog(RequestDescriptiom.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("postid", pid);

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
    public void waitforsometime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(RequestDescriptiom.this,BuyerDashboard.class);
                startActivity(intent);
                finishAffinity();
            }
        }, 2000);
    }
}