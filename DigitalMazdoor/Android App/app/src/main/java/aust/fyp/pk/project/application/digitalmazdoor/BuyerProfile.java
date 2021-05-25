package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class BuyerProfile extends Fragment {

    View view;
    TextInputEditText name,email,profiledescription,phone,password;
    TextView main_cat,sub_cat;
    String NAME="",EMAIL="",PHONE="",PASSWORD="",FILEPATH="",PROFILEPICTURE="";
    String NAME1="",EMAIL1="",PHONE1="",PASSWORD1="",MAINCAT1="",SUBCAT1="",PROFILEPICTURE1="",ID="";
    ImageView profilepic;
    Button changeprofilepic,savechanges;
    int REQUEST_CHECK_SETTINGS = 100;
    private ArrayList<MediaFile> mediaFiles;
    private int count=0;
    LinearLayout loader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buyer_profile, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        profiledescription =view.findViewById(R.id.profilesummary);
        phone = view.findViewById(R.id.phone);
        mediaFiles = new ArrayList<>();
        profilepic = view.findViewById(R.id.profilepic);
        changeprofilepic = view.findViewById(R.id.changeprofile);
        main_cat = view.findViewById(R.id.select_main_cat);
        sub_cat = view.findViewById(R.id.select_sub_cat);
        password= view.findViewById(R.id.password);
        savechanges = view.findViewById(R.id.savechanges);
        loader = view.findViewById(R.id.loader);
        loader.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        NAME = sharedPreferences.getString("username","");
        PHONE = sharedPreferences.getString("phone","");
        PASSWORD = sharedPreferences.getString("password","");
        EMAIL = sharedPreferences.getString("email","");
        PROFILEPICTURE = sharedPreferences.getString("profilepicture","");
        ID = sharedPreferences.getString("id","");
        name.setText(NAME);
        email.setText(EMAIL);

        String url1;
        url1  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+PROFILEPICTURE;
        url1 = url1.replace(" ","%20");

        Glide.with(getActivity()).load(url1).circleCrop().
                into(profilepic);
        phone.setText(PHONE);

        password.setText(PASSWORD);

        changeprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPics();
            }
        });

        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkcontents();
            }
        });
        return view;
    }
    private void checkcontents() {


        NAME1 = name.getText().toString().trim();
        EMAIL1 = email.getText().toString().trim();
        PHONE1= phone.getText().toString().trim();
        PASSWORD1 = password.getText().toString().trim();
        PROFILEPICTURE1 = PROFILEPICTURE;

        boolean isError = false;
        boolean isSame = false;
        if(NAME1.isEmpty() || NAME1.equals("")){
            name.setError("Field Cannot Be Empty");
            isError = true;
        }else{
            isError=false;
        }
        if(EMAIL1.isEmpty() || EMAIL1.equals("")){
            email.setError("Field Cannot Be Empty");
            isError = true;

        }else{
            isError=false;
        }

        if (PHONE1.isEmpty() ||PHONE1.equals("")) {
            phone.setError("Field Cannot Be Empty");
            isError = true;

        }else if(PHONE1.length()<12){
            phone.setError("Minimum 12 characters are required");
            isError = true;
        }else{
            isError=false;
        }

        if(PASSWORD1.isEmpty() ||PASSWORD1.equals("")){
            password.setError("Field Cannot Be Empty");
            isError= true;
        }else if(PASSWORD1.length() <8){
            password.setError("Minimum 8 characters are required");
            isError=true;
        }else{
            isError=false;
        }




        if(!isError){
            if(NAME1.equals(NAME) && EMAIL1.equals(EMAIL) &&
                    PASSWORD1.equals(PASSWORD) && PROFILEPICTURE1.equals(PROFILEPICTURE) &&
                    PHONE1.equals(PHONE)  && FILEPATH.isEmpty()){
                Toast.makeText(getActivity(), "No Changes To save", Toast.LENGTH_SHORT).show();
            }else {
                loader.setVisibility(View.VISIBLE);
                if(FILEPATH.equals("") ||FILEPATH.isEmpty()){
                    UpdatewithoutProfilePicture();
                }else{
                    loader.setVisibility(View.VISIBLE);
                    UpdatewithProfilePicture();
                }
            }
        }
    }
    private void UpdatewithoutProfilePicture() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.UPDATEWITHOUTPROFILEPICTURE1;
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

                                buildDialog(getActivity(),"502","Internal Server Error").show();
                            }
                            else if(status.equals("404")){
                                loader.setVisibility(View.GONE);

                                buildDialog(getActivity(),"Access Deined","Email/password is wrong").show();
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isuserloggedin",true);
                                editor.putString("category","buyer");

                                loader.setVisibility(View.GONE);
                                String username = reader.getString("username");
                                editor.putString("username",username);

                                String password = reader.getString("password");
                                editor.putString("password",password);

                                String phonenum = reader.getString("phone");
                                editor.putString("phonenumber",phonenum);

                                String email1 = reader.getString("email");
                                editor.putString("email",email1);
                                editor.apply();
                                buildDialog(getActivity(),"Details Updated Successfully","").show();
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
                buildDialog(getActivity(),"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID",ID);
                params.put("fullname", NAME1);
                params.put("email", EMAIL1);
                params.put("password", PASSWORD1);
                params.put("phone", PHONE1);

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



    private void UpdatewithProfilePicture() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating Your Account Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final File file = new File(this.FILEPATH);
        Ion.with(getActivity())
                .load("POST", Urls.UPDATEWITHPROFILEPICTURE1)
                .setLogging("1312wWOD", Log.INFO)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        double progress = (100.0 * uploaded) / total;
                        progressDialog.setMessage("Creating account.. " + ((int) progress) + " %");
                    }
                })
                .setMultipartFile("file", "application/jpeg", file)
                .setMultipartParameter("fullname", NAME1)
                .setMultipartParameter("email", EMAIL1)
                .setMultipartParameter("phone", PHONE1)
                .setMultipartParameter("password", PASSWORD1)
                .setMultipartParameter("ID", ID)

                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        progressDialog.dismiss();
                        Log.i("312333", result + "  " + e + "");
                        if (result != null) {
                            try {
                                JSONObject reader = new JSONObject(result);
                                String status = reader.getString("status");
                                if (status.equals("500")) {
                                    loader.setVisibility(View.GONE);
                                    buildDialog2(getActivity(), "Please Try Again", "Record Not Saved").show();

                                }

                                else if (status.equals("200")) {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isuserloggedin",true);
                                    editor.putString("category","freelancer");

                                    loader.setVisibility(View.GONE);
                                    String username = reader.getString("username");
                                    editor.putString("username",username);

                                    String password = reader.getString("password");
                                    editor.putString("password",password);

                                    String phonenum = reader.getString("phone");
                                    editor.putString("phonenumber",phonenum);
                                    String profilepic = reader.getString("profilepicture");
                                    editor.putString("profilepicture",profilepic);
                                    String email1 = reader.getString("email");
                                    editor.putString("email",email1);
                                    editor.apply();
                                    buildDialog(getActivity(),"Details Updated Successfully","").show();

                                }
                                else if(status.equals("409")){
                                    loader.setVisibility(View.GONE);
                                    buildDialog2(getActivity(), "Account not created", "User with same Email already exists").show();

                                }
                            } catch (Exception ex) {
                                Log.i("312333", ex + "");
                            }
                        } else {
                            loader.setVisibility(View.GONE);
                            buildDialog2(getActivity(), "Please Try Again", "Record Not Saved").show();
                        }
                    }
                });

    }



    public void UploadPics(){
        Intent intent = new Intent(getActivity(), FilePickerActivity.class);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1
                && resultCode == RESULT_OK
                && data != null){
            mediaFiles.clear();
            mediaFiles.addAll(data.<MediaFile>getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES));
            for(int i = 0;i<mediaFiles.size();i++){
                MediaFile file = mediaFiles.get(i);
                FILEPATH = file.getPath();

                String url1;
                url1  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+file.getName();
                url1 = url1.replace(" ","%20");

                profilepic.setImageURI(file.getUri());

            }

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
                Intent intent = new Intent(getActivity(),BuyerDashboard.class);
                startActivity(intent);
                getActivity().finish();
            }
        }, 1500);
    }
}