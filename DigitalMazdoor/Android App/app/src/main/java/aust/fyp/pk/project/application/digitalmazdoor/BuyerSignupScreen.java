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
import android.view.MenuItem;
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

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuyerSignupScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback, LocationListener {

    private TextInputEditText fullname,phone,password,repassword,profilepic,email;
    private CountryCodePicker ccp;
    private String name,phonenum,countrycode,pass,repass,profile,email1,filePath="";
    private Button signup;
    private LinearLayout profilepiclayout;
    private ArrayList<MediaFile> mediaFiles;
    private LinearLayout loader;
    private TextView signin;
    LocationManager locationManager;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    private String district ="";
    private int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_signup_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
        mediaFiles = new ArrayList<>();
        fullname = findViewById(R.id.fullname);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        profilepic = findViewById(R.id.profilepicturetxt);
        profilepiclayout = findViewById(R.id.profilepiclayout);
        signin = findViewById(R.id.signin);
        email = findViewById(R.id.email);
        ccp = findViewById(R.id.ccp);
        signup = findViewById(R.id.signup_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkcontent();
            }
        });
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
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPics();
            }
        });
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BuyerSignupScreen.this,BuyerLogin.class);
                    startActivity(intent);
                    finish();
                }
            });


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
        }else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                //Toast.makeText(LaborListCategory.this, "GPS enabled", Toast.LENGTH_LONG).show();


            } else {
                count = 0;
                Toast.makeText(BuyerSignupScreen.this, "Please Enable Your Gps To Use Services", Toast.LENGTH_SHORT).show();
                waitforsometime();
            }
        }

    }
    private void checkcontent() {
        name = fullname.getText().toString().trim();
        phonenum = phone.getText().toString().trim();
        email1 = email.getText().toString().trim();
        pass = password.getText().toString().trim();
        repass = repassword.getText().toString().trim();

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
        }else{

            isError = false;
        }

        if(repass.isEmpty() || repass.equals("")){
            repassword.setError("Field Cannot be Empty");
            isError = true;
        }else if(repass.length()<8){
            repassword.setError("Minimum 8 characters are required");
            isError = true;
        }else{
            isError=false;
        }
        if(filePath.isEmpty() || filePath.equals("")){
            profilepic.setError("Please Upload Profile Picture");
            isError= true;
        }else{
            isError = false;
        }

       if(!isError){
           loader.setVisibility(View.VISIBLE);
           getLocation();

       }
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
                Intent intent = new Intent(BuyerSignupScreen.this,BuyerLogin.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
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


            Geocoder geocoder = new Geocoder(BuyerSignupScreen.this, Locale.getDefault());
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

                    status.startResolutionForResult(BuyerSignupScreen.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    private void uploadNow() {
        final ProgressDialog progressDialog = new ProgressDialog(BuyerSignupScreen.this);
        progressDialog.setMessage("Creating Your Account Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final File file = new File(filePath);
        Ion.with(BuyerSignupScreen.this)
                .load("POST", Urls.SIGNUP_BUYER)
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
                .setMultipartParameter("status", "Active")
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
                                    buildDialog2(BuyerSignupScreen.this, "Please Try Again", "Record Not Saved").show();

                                }

                                else if (status.equals("200")) {
                                    loader.setVisibility(View.GONE);
                                    buildDialog(BuyerSignupScreen.this, "Congratulations", "Account Created Successfully").show();

                                }
                                else if(status.equals("409")){
                                    loader.setVisibility(View.GONE);
                                    buildDialog2(BuyerSignupScreen.this, "Account not created", "User with same Email already exists").show();

                                }
                            } catch (Exception ex) {
                                Log.i("312333", ex + "");
                            }
                        } else {
                            loader.setVisibility(View.GONE);
                            buildDialog2(BuyerSignupScreen.this, "Please Try Again", "Record Not Saved").show();
                        }
                    }
                });

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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;

        }

        return true;
    }
}