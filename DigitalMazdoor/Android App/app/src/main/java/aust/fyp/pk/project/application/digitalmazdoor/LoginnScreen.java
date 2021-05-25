package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginnScreen extends AppCompatActivity {


    private TextInputEditText email,password;
    private String email1,password1="";
    private Button signin;
    private TextView signupuser,forgotpass;
    private LinearLayout loader;
    private ArrayList<String> userinfoarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginn_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        signupuser = findViewById(R.id.signupuser);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
        forgotpass= findViewById(R.id.forgotpass);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginnScreen.this,FreelancerForgotPassword.class);
                startActivity(intent);
            }
        });
        userinfoarray = new ArrayList<>();
        signupuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(LoginnScreen.this,FreelancerSignupScreen.class);
               startActivity(intent);
               finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkcontents();
            }
        });

    }

    private void checkcontents() {
        email1 = email.getText().toString().trim();
        password1 = password.getText().toString().trim();
        boolean isError = true;
        if(email1.isEmpty() || email1.equals("") || email1 == null){
            email.setError("Field Cannot Be Empty");
            isError = true;
        } else if(!(Patterns.EMAIL_ADDRESS.matcher(email1).matches())){
            email.setError("Incorrect Email Addressr");
            isError = true;

        }else{

            isError = false;
        }
        if(password1.isEmpty() || password1.equals("")){
            password.setError("Field Cannot Be Empty");
            isError = true;
        }else if(password1.length()<8){
            password.setError("Minimum 8 characters are required");
            isError = true;
        }else{

            isError = false;
        }
        if(!isError){
           loader.setVisibility(View.VISIBLE);
            VolleyRequest();
           }
    }

    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(LoginnScreen.this);

        String url = Urls.LOGIN_FREELANCER;
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

                                buildDialog(LoginnScreen.this,"502","Internal Server Error").show();
                            }
                            else if(status.equals("404")){
                                loader.setVisibility(View.GONE);

                                buildDialog(LoginnScreen.this,"Access Deined","Email/password is wrong").show();
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isuserloggedin",true);
                                editor.putString("category","freelancer");

                                loader.setVisibility(View.GONE);
                                String username = reader.getString("username");
                                editor.putString("username",username);
                                String id = reader.getString("id");
                                editor.putString("id",id);
                                String password = reader.getString("password");
                                editor.putString("password",password);

                                String phonenum = reader.getString("phone");
                                editor.putString("phonenumber",phonenum);
                                String profilepic = reader.getString("profilepicture");
                                editor.putString("profilepicture",profilepic);
                                String email1 = reader.getString("email");
                                editor.putString("email",email1);
                                String dateofjoining = reader.getString("dateofjoining");
                                editor.putString("dateofjoining",dateofjoining);
                                String city = reader.getString("city");
                                editor.putString("city",city);
                                String accountstatus = reader.getString("accountstatus");
                                editor.putString("accountstatus",accountstatus);
                                String rating = reader.getString("rating");
                                editor.putString("rating",rating);
                                String summary = reader.getString("summary");
                                editor.putString("summary",summary);

                                String maincat = reader.getString("workingcat");
                                editor.putString("maincat",maincat);
                                String subcat = reader.getString("subcat");
                                editor.putString("subcat",subcat);

                                int totalgigs = reader.getInt("totalgigs");
                                editor.putInt("totalgigs",totalgigs);

                                editor.apply();
                                Intent intent = new Intent(LoginnScreen.this,FreelancerDashboard.class);
                                startActivity(intent);
                               finishAffinity();
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
                buildDialog(LoginnScreen.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email1);
                params.put("password", password1);
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
                Intent intent = new Intent(LoginnScreen.this,LoginnScreen.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}