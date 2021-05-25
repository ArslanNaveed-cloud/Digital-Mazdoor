package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.textfield.TextInputEditText;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.sayantan.advancedspinner.MultiSpinner;
import com.sayantan.advancedspinner.MultiSpinnerListener;
import com.sayantan.advancedspinner.SingleSpinner;
import com.sayantan.advancedspinner.SpinnerListener;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FreelancerSignupScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback, LocationListener{

    String[] maincategory = {"Graphic Designing","App Development","Data Entry","Content Writing","Software Development","Digital Marketing","Music And Video","Programming And Tech"};
    String[] graphic_subcategory = {"Logo Making","Flyer Designing","Banner Designing","UI & UX Designing","Business Card Designing"};
    String[] development_subcategory = {"Mobile App Development","Web App Development","Wordpress Websites","Flutter App Development","React Native App Development"};
    String[] DataEntry_subcategory = {"Mobile App Development","Web App Development","Flutter App Development","React Native App Development"};
    private SingleSpinner singleSpinner;
    private MultiSpinner multiSpinner;
    private TextInputEditText fullname,phone,password,repassword,profilepic,email,profilesummary;
    private CountryCodePicker ccp;
    private String name="",phonenum="",countrycode="",pass="",repass="",profile="",maincat="",subcat="",email1="",filePath="",summary="";
    private Button signup;
    private LinearLayout profilepiclayout;
    private  List<String> list;
    private LinearLayout subwrapper;
    private String subcategory="";
    private ArrayList<MediaFile> mediaFiles;
    private List<String> categorylist;
    private TextView select_main_cat,select_sub_cat;
    private LinearLayout loader;
    LocationManager locationManager;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    private String district ="";
    private int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_signup_screen);
        getSupportActionBar().setHomeButtonEnabled(true);
        mediaFiles = new ArrayList<>();
        profilesummary = findViewById(R.id.profilesummary);
        singleSpinner = findViewById(R.id.maincategory);
        multiSpinner = findViewById(R.id.subcategory);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
        subwrapper = findViewById(R.id.subwrapper);
        subwrapper.setVisibility(View.GONE);
        fullname = findViewById(R.id.fullname);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        profilepic = findViewById(R.id.profilepicturetxt);
        profilepiclayout = findViewById(R.id.profilepiclayout);
        categorylist=Arrays.asList(maincategory);
        email = findViewById(R.id.email);
        ccp = findViewById(R.id.ccp);
        if(!isConnected(this)){
            buildDialog(this,"We are sorry","Please Check Your Internet Connection.").show();
        }else {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 10000);
            locationRequest.setFastestInterval(5 * 10000);

        }
        categorylist = Arrays.asList(maincategory);
        singleSpinner.setSpinnerList(categorylist);

        select_main_cat = findViewById(R.id.select_main_cat);
        select_sub_cat = findViewById(R.id.select_sub_cat);
        singleSpinner.addOnItemChoosenListener(new SpinnerListener() {
            @Override
            public void onItemChoosen(String s, int i) {
                if(s!=null){
                    maincat = s;
                    select_main_cat.setText(s);
                    checkmaincat(s);

                }else{
                    Toast.makeText(FreelancerSignupScreen.this, "Selection Cleared", Toast.LENGTH_SHORT).show();
                    subwrapper.setVisibility(View.GONE);
                    select_main_cat.setText("Select Your Working Category");
                }
                }
        });

        multiSpinner.addOnItemsSelectedListener(new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<String> list, boolean[] booleans) {
                select_sub_cat.setText(list.toString().substring(0,30)+"....");
                for(int i= 0;i<list.size();i++){
                    subcategory = subcategory+list.get(i)+",";

                }
            }
        });
        signup = findViewById(R.id.signup_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0){

                    checkcontent();
                }else{
                    count++;
                    Toast.makeText(FreelancerSignupScreen.this, "Please Wait..", Toast.LENGTH_SHORT).show();
                }

            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
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
                filePath = file.getPath();
                profilepic.setText(filePath);
            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                //Toast.makeText(LaborListCategory.this, "GPS enabled", Toast.LENGTH_LONG).show();


            } else {
                count = 0;
                Toast.makeText(FreelancerSignupScreen.this, "Please Enable Your Gps To Use Services", Toast.LENGTH_SHORT).show();
                waitforsometime();
            }
        }

    }
    private void checkmaincat(String choice){
        switch (choice){
            case "Graphic Designing":
                maincat = choice;
                list = Arrays.asList(graphic_subcategory);
                multiSpinner.setSpinnerList(list);
                subwrapper.setVisibility(View.VISIBLE);
                break;
            case "App Development":
                maincat = choice;
                list = Arrays.asList(development_subcategory);
                multiSpinner.setSpinnerList(list);
                subwrapper.setVisibility(View.VISIBLE);
                break;
            case "Data Entry":
                maincat = choice;
                list = Arrays.asList(DataEntry_subcategory);
                multiSpinner.setSpinnerList(list);
                subwrapper.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(this, "No Sub ", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void checkcontent() {
        name = fullname.getText().toString().trim();
        phonenum = phone.getText().toString().trim();
        email1 = email.getText().toString().trim();
        pass = password.getText().toString().trim();
        repass = repassword.getText().toString().trim();
        summary = profilesummary.getText().toString().trim();
        filePath = profilepic.getText().toString().trim();
        if(maincat ==null){
            maincat = "";
        }
        boolean isError = false;
        if(name.isEmpty() || name ==null ||name.equals("")){
            fullname.setError("Field Cannot Be Empty");
            isError = true;
        }else{
            isError = false;
        }

        if(phonenum.isEmpty() || phonenum == null || phonenum.equals("")){
            phone.setError("Field Cannot Be Empty");
            isError = true;
        }else if(phonenum.length()<11){
            phone.setError("Incorrect Phone Number");
            isError = true;
        }else{
            isError = false;
        }

        if(email1.isEmpty() || email1.equals("") || email1 == null){
            email.setError("Field Cannot Be Empty");
            isError = true;
        } else if(!(Patterns.EMAIL_ADDRESS.matcher(email1).matches())){
            email.setError("Incorrect Email Addressr");
            isError = true;

        }else{

            isError = false;
        }

        if(pass.isEmpty() || pass.equals("")){
            password.setError("Field Cannot Be Empty");
            isError = true;
        }else if(pass.length()<8){
            password.setError("Minimum 8 characters are required");
            isError = true;
        }

        if(repass.isEmpty() || repass.equals("")){
            repassword.setError("Field Cannot be Empty");
            isError = true;
        }else if(repass.length()<8){
            repassword.setError("Minimum 8 characters are required");
            isError = true;
        }

        if(maincat.isEmpty() || maincat.equals("")){
            Toast.makeText(this, "Select Your Working Category", Toast.LENGTH_SHORT).show();
            isError = true;

        }else{
            if(subcategory.isEmpty()){
                Toast.makeText(this, "Select Your Working Category", Toast.LENGTH_SHORT).show();
                isError = true;
            }
        }
        if(filePath.isEmpty() || filePath.equals("")){
            profilepic.setError("Please Upload Profile Picture");
            isError= true;
        }
        if(summary.isEmpty() || summary.equals("")){
            profilesummary.setError("Field Cannot be empty");
            isError = true;
        }else if(summary.length()>500){
            profilesummary.setError("Cannot Enter Greater Than 500 Character");
            isError = true;
        }


        if(!isError){
            loader.setVisibility(View.VISIBLE);
            getLocation();
           }
    }

    private void uploadNow() {
        final ProgressDialog progressDialog = new ProgressDialog(FreelancerSignupScreen.this);
        progressDialog.setMessage("Creating Your Account Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final File file = new File(filePath);
        Ion.with(FreelancerSignupScreen.this)
                .load("POST", Urls.SIGNUP_FREELANCER)
                .setLogging("1312wWOD", Log.INFO)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        double progress = (100.0 * uploaded) / total;
                        progressDialog.setMessage("Creating account.. " + ((int) progress) + " %");
                    }
                })
                .setMultipartFile("file", "application/jpeg", file)
                .setMultipartParameter("fullname", name)
                .setMultipartParameter("email", email1)
                .setMultipartParameter("phone", phonenum)
                .setMultipartParameter("password", pass)
                .setMultipartParameter("city", district)
                .setMultipartParameter("workingcategory", maincat)
                .setMultipartParameter("subcategories", subcategory)
                .setMultipartParameter("status", "Active")
                .setMultipartParameter("profilesummary", summary)
                .setMultipartParameter("rating", "0")

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
                                    buildDialog2(FreelancerSignupScreen.this, "500", "Internal Server Error").show();

                                }

                                else if (status.equals("200")) {
                                    loader.setVisibility(View.GONE);
                                    buildDialog(FreelancerSignupScreen.this, "Congratulations", "Account Created Successfully").show();

                                }
                                else if(status.equals("409")){
                                    loader.setVisibility(View.GONE);
                                    buildDialog2(FreelancerSignupScreen.this, "Account not created", "User with same Email already exists").show();

                                }
                            } catch (Exception ex) {
                                Log.i("312333", ex + "");
                            }
                        } else {
                            loader.setVisibility(View.GONE);
                            buildDialog2(FreelancerSignupScreen.this, "Please Try Again", "Record Not Saved").show();
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
    public void waitforsometime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(FreelancerSignupScreen.this,LoginnScreen.class);
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
    void getLocation() {
        try {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        try {


            Geocoder geocoder = new Geocoder(FreelancerSignupScreen.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            district = addresses.get(0).getLocality();
            //Toast.makeText(LaborListCategory.this, "Location = "+district, Toast.LENGTH_SHORT).show();
           uploadNow();



        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                //Toast.makeText(getActivity(), "Gps Already Enabled", Toast.LENGTH_SHORT).show();


                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //Toast.makeText(getActivity(), "Gps Not Enabled", Toast.LENGTH_SHORT).show();

                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(FreelancerSignupScreen.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }
}