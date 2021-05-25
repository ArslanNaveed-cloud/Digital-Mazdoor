package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.sayantan.advancedspinner.SingleSpinner;
import com.sayantan.advancedspinner.SpinnerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditGig extends AppCompatActivity {
    String[] maincategory = {"Graphic Designing","App Development","Data Entry","Content Writing","Software Development","Digital Marketing","Music And Video","Programming And Tech"};
    String[] graphic_subcategory = {"Logo Making","Flyer Designing","Banner Designing","UI & UX Designing","Business Card Designing"};
    String[] development_subcategory = {"Mobile App Development","Web App Development","Wordpress Websites","Flutter App Development","React Native App Development"};
    String[] DataEntry_subcategory = {"Mobile App Development","Web App Development","Flutter App Development","React Native App Development"};
    String[] deliverydays = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
    List<String> deliverydataarraylist;
    private ArrayList<MediaFile> mediaFiles;
    private TextInputEditText title,price,image,desciption;
    private SingleSpinner days;
    private SingleSpinner singleSpinner;
    private boolean isuserloggedin;
    private TextView select_main_cat;
    private List<String> categorylist;
    String gcoverimage;
    private Button savegig;
    private String maincat="",gigtitle="",gigprice="",filepath="",gigdescription="",gigdays="";
    private String username;
    private LinearLayout loader;
    private String userid;
    private TextView numberofdays;
    String ggigid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gig);
        singleSpinner = findViewById(R.id.maincategory);
        title = findViewById(R.id.title);
        select_main_cat = findViewById(R.id.category);
        savegig = findViewById(R.id.addgig);
        mediaFiles = new ArrayList<>();
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
        numberofdays = findViewById(R.id.numberofdays);
        deliverydataarraylist = Arrays.asList(deliverydays);
        price = findViewById(R.id.price);
        image = findViewById(R.id.coverimage);
        desciption = findViewById(R.id.description);
        days = findViewById(R.id.days);
        days.setSpinnerList(deliverydataarraylist);

        Intent intent = getIntent();

        String gtitle = intent.getStringExtra("title");
        String gprice = intent.getStringExtra("price");
        String gdeliverydays = intent.getStringExtra("deliverydays");
        String gdescription = intent.getStringExtra("description");
        String gcategory = intent.getStringExtra("category");
        ggigid= intent.getStringExtra("id");

        gcoverimage = intent.getStringExtra("coverimage");

        title.setText(gtitle);
        price.setText(gprice);
        desciption.setText(gdescription);
        numberofdays.setText(gdeliverydays);
        gigdays = numberofdays.getText().toString().trim();
        select_main_cat.setText(gcategory);
        maincat = gcategory;
        title.setText(gtitle);
        title.setText(gtitle);
        filepath = gcoverimage;
        image.setText(filepath);

        days.addOnItemChoosenListener(new SpinnerListener() {
            @Override
            public void onItemChoosen(String s, int i) {
                if(s!=null){
                    numberofdays.setText(s);
                    gigdays = s;
                }else{
                    Toast.makeText(EditGig.this, "Selection Cleared", Toast.LENGTH_SHORT).show();

                    numberofdays.setText("Select Your Working Category");
                }
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username","");
        isuserloggedin =  sharedPreferences.getBoolean("isuserloggedin",false);
        if(isuserloggedin){
            String category = sharedPreferences.getString("maincat","");
            switch (category){
                case "Graphic Designing":
                    categorylist = Arrays.asList(graphic_subcategory);
                    singleSpinner.setSpinnerList(categorylist);

                    break;
                case "App Development":
                    categorylist = Arrays.asList(development_subcategory);
                    singleSpinner.setSpinnerList(categorylist);

                    break;
                case "Data Entry":
                    categorylist = Arrays.asList(DataEntry_subcategory);
                    singleSpinner.setSpinnerList(categorylist);

                    break;
            }
        }
        singleSpinner.addOnItemChoosenListener(new SpinnerListener() {
            @Override
            public void onItemChoosen(String s, int i) {
                if(s!=null){

                    select_main_cat.setText(s);
                    maincat = s;

                }else{
                    Toast.makeText(EditGig.this, "Selection Cleared", Toast.LENGTH_SHORT).show();

                    select_main_cat.setText("Select Your Working Category");
                }
            }
        });

        savegig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(EditGig.this)){
                    buildDialog2(EditGig.this,"We are sorry","Please Check Your Internet Connection.").show();
                }else {
                    checkcontents();
                }

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPics();
            }
        });
    }
    public void UploadPics(){
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowVideos(false)
                .setShowImages(true)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .setIgnoreNoMedia(true)
                .build());
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1
                && resultCode == RESULT_OK
                && data != null){
            mediaFiles.clear();
            mediaFiles.addAll(data.<MediaFile>getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES));
            for(int i = 0;i<mediaFiles.size();i++){
                MediaFile file = mediaFiles.get(i);
                filepath = file.getPath();
                image.setText(filepath);
            }
        }

    }

    private void checkcontents() {
        gigtitle = title.getText().toString().trim();

        gigdescription = desciption.getText().toString().trim();
        gigprice = price.getText().toString().trim();
        filepath = image.getText().toString().trim();
        boolean isError = false;
        if(gigtitle.isEmpty() || gigtitle.equals("")){
            title.setError("Field Cannot Be Empty");
            isError = true;
        }else if(gigdays.isEmpty() || gigdays.equals("")){
            Toast.makeText(this, "Please Select Number Of Days", Toast.LENGTH_SHORT).show();
            isError = true;

        }else if(gigdescription.isEmpty() || gigdescription.equals("")){
            desciption.setError("Field Cannot Be Empty");
            isError = true;
        }else if(gigprice.isEmpty() || gigprice.equals("")){
            price.setError("Field Cannot Be Empty");
            isError = true;
        }else if(filepath.isEmpty() || filepath.equals("")){
            image.setError("Field Cannot Be Empty");
            isError = true;
        }else{
            isError = false;
        }

        if(isError == false){
            if(filepath.equals(gcoverimage)){
                updatewithoutpicture();
            }else{
                uploadNow();

            }
            }

    }
    private void uploadNow() {
        final ProgressDialog progressDialog = new ProgressDialog(EditGig.this);
        progressDialog.setMessage("Creating Your Account Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final File file = new File(filepath);
        Ion.with(EditGig.this)
                .load("POST", Urls.EDITGIGSWITHIMAGE)
                .setLogging("1312wWOD", Log.INFO)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        double progress = (100.0 * uploaded) / total;
                        progressDialog.setMessage("Creating account.. " + ((int) progress) + " %");
                    }
                })
                .setMultipartFile("file", "application/jpeg", file)
                .setMultipartParameter("gigid", ggigid)
                 .setMultipartParameter("title", gigtitle)
                .setMultipartParameter("description", gigdescription)
                .setMultipartParameter("price", gigprice)
                .setMultipartParameter("category", maincat)
                .setMultipartParameter("username", username)
                .setMultipartParameter("deliverydays", gigdays)


                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        progressDialog.dismiss();
                        Log.i("312333", result + "  " + e + "");
                        if (result != null) {
                            try {
                                JSONObject mainObject = new JSONObject(result);
                                String status = mainObject.getString("status");
                                if (status.equals("500")) {
                                    loader.setVisibility(View.GONE);
                                    buildDialog2(EditGig.this, "Please Try Again", "Gigs Not Added").show();

                                }

                                else if (status.equals("200")) {
                                    loader.setVisibility(View.GONE);
                                    buildDialog(EditGig.this, "Congrats.!", "Gig Added Successfully").show();
                                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    int totalgigs = sharedPreferences.getInt("totalgigs",0);
                                    totalgigs = totalgigs+1;
                                    editor.putInt("totalgigs",totalgigs);
                                    editor.apply();



                                }
                                else if(status.equals("409")){
                                    loader.setVisibility(View.GONE);
                                    buildDialog2(EditGig.this, "Gig not Added", "Gig already exists").show();

                                }
                            } catch (Exception ex) {
                                Log.i("312333", ex + "");
                            }
                        } else {
                            loader.setVisibility(View.GONE);
                            buildDialog2(EditGig.this, "Please Try Again", "Gig Not Saved").show();
                        }
                    }
                });

    }
    public void updatewithoutpicture(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(EditGig.this);

        String url = Urls.EDITGIGWITHOUTIMAGE;
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

                                buildDialog(EditGig.this,"Error","Please Try Again").show();
                            }
                            else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                buildDialog(EditGig.this,"Congrats.!","Gig Updated Successfully").show();

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
                buildDialog(EditGig.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gigid", ggigid);

                params.put("title", gigtitle);
                params.put("description", gigdescription);
                params.put("price", gigprice);
                params.put("category", maincat);
                params.put("username", username);
                params.put("deliverydays", gigdays);


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
                Intent intent = new Intent(EditGig.this,FreelancerDashboard.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public AlertDialog.Builder buildDialog2(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }
}