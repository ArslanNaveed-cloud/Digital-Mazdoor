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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.sayantan.advancedspinner.SingleSpinner;
import com.sayantan.advancedspinner.SpinnerListener;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddGigs extends AppCompatActivity {
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
    private Button savegig;
    private String maincat="",gigtitle="",gigprice="",filepath="",gigdescription="",gigdays="",city="";
    private String username;
    private LinearLayout loader;
    private String userid;
    private TextView numberofdays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gigs);
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
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        city = sharedPreferences.getString("city","");
        days.addOnItemChoosenListener(new SpinnerListener() {
            @Override
            public void onItemChoosen(String s, int i) {
                if(s!=null){
                    numberofdays.setText(s);
                    gigdays = s;
                }else{
                    Toast.makeText(AddGigs.this, "Selection Cleared", Toast.LENGTH_SHORT).show();

                    numberofdays.setText("Select Your Working Category");
                }
            }
        });

        username = sharedPreferences.getString("username","");
        userid = sharedPreferences.getString("id","");
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
                    Toast.makeText(AddGigs.this, "Selection Cleared", Toast.LENGTH_SHORT).show();

                    select_main_cat.setText("Select Your Working Category");
                }
            }
        });

        savegig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(AddGigs.this)){
                    buildDialog2(AddGigs.this,"We are sorry","Please Check Your Internet Connection.").show();
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
            uploadNow();
        }

    }
    private void uploadNow() {
        final ProgressDialog progressDialog = new ProgressDialog(AddGigs.this);
        progressDialog.setMessage("Creating Your Gig Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final File file = new File(filepath);
        Ion.with(AddGigs.this)
                .load("POST", Urls.SAVE_GIGS)
                .setLogging("1312wWOD", Log.INFO)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        double progress = (100.0 * uploaded) / total;
                        progressDialog.setMessage("Creating Gig.. " + ((int) progress) + " %");
                    }
                })
                .setMultipartFile("file", "application/jpeg", file)
                .setMultipartParameter("id", userid)
                .setMultipartParameter("title", gigtitle)
                .setMultipartParameter("description", gigdescription)
                .setMultipartParameter("price", gigprice)
                .setMultipartParameter("category", maincat)
                .setMultipartParameter("username", username)
                .setMultipartParameter("deliverydays", gigdays)
                .setMultipartParameter("city", city)


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
                                    buildDialog2(AddGigs.this, "Please Try Again", "Gigs Not Added").show();

                                }

                                else if (status.equals("200")) {
                                    loader.setVisibility(View.GONE);
                                    buildDialog(AddGigs.this, "Congrats.!", "Gig Added Successfully").show();
                                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    int totalgigs = sharedPreferences.getInt("totalgigs",0);
                                    totalgigs = totalgigs+1;
                                    editor.putInt("totalgigs",totalgigs);
                                    editor.apply();



                                }
                                else if(status.equals("409")){
                                    loader.setVisibility(View.GONE);
                                    buildDialog2(AddGigs.this, "Gig not Added", "Gig already exists").show();

                                }
                            } catch (Exception ex) {
                                Log.i("312333", ex + "");
                            }
                        } else {
                            loader.setVisibility(View.GONE);
                            buildDialog2(AddGigs.this, "Please Try Again", "Gig Not Saved").show();
                        }
                    }
                });

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
                Intent intent = new Intent(AddGigs.this,FreelancerDashboard.class);
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