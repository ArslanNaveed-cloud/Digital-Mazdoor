package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class AddRequest extends AppCompatActivity {
    String[] maincategory = {"Graphic Designing","App Development","Data Entry","Content Writing","Software Development","Digital Marketing","Music And Video","Programming And Tech"};
    List<String> categorydatalist;
    private TextInputEditText title,description,budget;
    TextView numofdays,category;
    private LinearLayout loader;
    private SingleSpinner days;
    String[] deliverydays = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
    List<String> deliverydataarraylist;
    private SingleSpinner singleSpinner;

    private Button postgig;
    String ptitle="",pdays="",pbudget="",pdescription="",pcategory="",pcity="",buyername="",buyerid="",buyerpic="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        title = findViewById(R.id.title);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        pcity = sharedPreferences.getString("city","");
        buyername = sharedPreferences.getString("username","");
        buyerid = sharedPreferences.getString("id","");
        buyerpic =sharedPreferences.getString("profilepicture","");

        description = findViewById(R.id.description);
        budget = findViewById(R.id.price);
        numofdays = findViewById(R.id.numberofdays);
        category = findViewById(R.id.category);
        days = findViewById(R.id.days);
        deliverydataarraylist = Arrays.asList(deliverydays);
        days.setSpinnerList(deliverydataarraylist);
        postgig = findViewById(R.id.addpost);
        singleSpinner = findViewById(R.id.maincategory);
        categorydatalist = Arrays.asList(maincategory);
        singleSpinner.setSpinnerList(categorydatalist);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
        singleSpinner.addOnItemChoosenListener(new SpinnerListener() {
            @Override
            public void onItemChoosen(String s, int i) {
                if(s!=null){

                    category.setText(s);
                    pcategory = s;

                }else{
                    Toast.makeText(AddRequest.this, "Selection Cleared", Toast.LENGTH_SHORT).show();

                    category.setText("Select Your Working Category");
                }
            }
        });
        days.addOnItemChoosenListener(new SpinnerListener() {
            @Override
            public void onItemChoosen(String s, int i) {
                if(s!=null){
                    numofdays.setText(s);
                    pdays = s;
                }else{
                    Toast.makeText(AddRequest.this, "Selection Cleared", Toast.LENGTH_SHORT).show();

                    numofdays.setText("Select Your Working Category");
                }
            }
        });

        postgig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              checkcontents();
            }
        });

    }

    private void checkcontents() {
            ptitle = title.getText().toString().trim();
            pbudget = budget.getText().toString().trim();
            pdescription = description.getText().toString().trim();
            boolean isError = false;
            if(ptitle.isEmpty() || ptitle.equals("")){
                title.setError("Field Cannot Be Empty");
                isError = true;
            }else if(pbudget.isEmpty() || pbudget.equals("")){
                budget.setError("Field Cannot Be Empty");
                isError=true;
            }

            else if(pdescription.equals("") || pdescription.isEmpty()){
                description.setError("Field Cannot Be Empty");
                isError = true;
            }else if(pdays.isEmpty() || pdays.equals("")){
                isError = true;
                Toast.makeText(this, "Select Time Period", Toast.LENGTH_SHORT).show();
            }

            else{
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(pbudget);
                boolean b = m.find();
                if(b){
                    budget.setError("Wrong Value For Budget.");
                    isError = true;
                }else{
                    int intpbudget = Integer.parseInt(pbudget);
                    if(intpbudget<=0){
                        budget.setError("Wrong Value For Budget.");
                        isError = true;
                    }else{
                        isError = false;
                    }

                }


            }
            if(!isError){
                loader.setVisibility(View.VISIBLE);
                PostRequest();
            }

    }

    public void PostRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(AddRequest.this);

        String url = Urls.PLACE_REQUESTS;
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

                                buildDialog(AddRequest.this,"Error","Internal Server Error").show();
                            }
                            else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                buildDialog2(AddRequest.this,"Your Post is Placed","Check Recieved Offers Daily").show();
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
                buildDialog(AddRequest.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("buyername", buyername);
                params.put("buyerid", buyerid);
                params.put("city", pcity);
                params.put("description", pdescription);
                params.put("title", ptitle);
                params.put("budget", pbudget);
                params.put("category", pcategory);
                params.put("buyerpic", buyerpic);
                params.put("days", pdays);

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
    public AlertDialog.Builder buildDialog2(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);
        waitforsometime2();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }
    public void waitforsometime2(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(AddRequest.this,BuyerDashboard.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
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