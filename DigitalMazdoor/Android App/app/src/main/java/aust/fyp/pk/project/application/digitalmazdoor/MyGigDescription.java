package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MyGigDescription extends AppCompatActivity {
    private String gigid;
    private String  gigtitle;
    private String gigcategory;
    private String gigprice;
    private String gigdeliverytime;
    private String gigimage;
    private String gigdescription;

    private ImageView coverimage;
    TextView title,description,price,category,days;
    private String userid;
    private String username;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gig_description);

        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        price = findViewById(R.id.price);
        days = findViewById(R.id.days);
        description = findViewById(R.id.description);
        coverimage = findViewById(R.id.coverimage);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        userid = sharedPreferences.getString("id","");
        username = sharedPreferences.getString("username","");

        Intent intent = getIntent();

        gigid = intent.getStringExtra("id");
        gigtitle = intent.getStringExtra("title");
        gigcategory = intent.getStringExtra("category");
        gigdeliverytime = intent.getStringExtra("days");
        gigprice = intent.getStringExtra("price");
        gigdescription = intent.getStringExtra("description");
        gigimage = intent.getStringExtra("image");

        title.setText(gigtitle);
        category.setText(gigcategory);
        price.setText(gigprice);
        days.setText(gigdeliverytime);
        description.setText(gigdescription);
        String url;
        url  = Urls.DOMAIN+"/assets/freelancergigpictures/"+gigimage;
        url = url.replace(" ","%20");
        Glide.with(MyGigDescription.this).load(url).
                into(coverimage);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.descriptionmenu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_delete:
              buildDialog(MyGigDescription.this,"Are Your Sure?","Following Gig Will Be Deleted").show();
                break;
            case android.R.id.home:
                super.onBackPressed();
                break;


            case R.id.nav_edit:
             Intent intent = new Intent(MyGigDescription.this,EditGig.class);

                intent.putExtra("id",gigid);
                intent.putExtra("title",gigtitle);
                intent.putExtra("deliverydays",gigdeliverytime);
                intent.putExtra("description",gigdescription);
                intent.putExtra("category",gigcategory);
                intent.putExtra("price",gigprice);
                intent.putExtra("coverimage",gigimage);
                startActivity(intent);

                break;

        }


        return true;
    }
    public AlertDialog.Builder buildDialog(final Context c, String header, String message) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(count ==0){
                    count++;
                    Toast.makeText(MyGigDescription.this, "Please Wait", Toast.LENGTH_SHORT).show();
                    DeleteGig();

                }else{
                    Toast.makeText(MyGigDescription.this, "Please Wait", Toast.LENGTH_SHORT).show();

                }
                }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }

    public void DeleteGig(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MyGigDescription.this);

        String url = Urls.DELETEGIG;
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

                                buildDialog(MyGigDescription.this,"Error","Please Try Again").show();
                            }
                          else if(status.equals("200")){
                                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                String totalgigs = reader.getString("totalgigs");
                                editor.putString("username",totalgigs);

                                editor.apply();
                                Intent intent = new Intent(MyGigDescription.this,FreelancerDashboard.class);
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

                buildDialog(MyGigDescription.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("userid", userid);
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

}