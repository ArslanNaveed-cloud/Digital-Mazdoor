package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    TextView username;


    String message = "";
    Button btn_send;
    EditText text_send;
    RecyclerView recyclerView;
    private String sendername,recievername,senderid,recieverid;;
    MessagesAdaphter adapter;
    Intent intent;
    private ImageView attachfile;
    ArrayList<MessageDataModel> arrayList;
    private int counter,loopsize=0;
    private LinearLayout loader;
    private static int loopcounter=0;
    private ArrayList<MediaFile> mediaFiles;
    private static String filePath;
    private boolean isfragmentshowed=false;
    FrameLayout container;
    SwipeRefreshLayout refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        ImageView image =getSupportActionBar().getCustomView().findViewById(R.id.image);
        container = findViewById(R.id.container);
        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                GetMessages();
                refresh.setRefreshing(false);
            }
        });
        loader = findViewById(R.id.loader);
        btn_send = findViewById(R.id.sendBtn);
        text_send = findViewById(R.id.send_text);
        intent = getIntent();
        String recieverimage = intent.getStringExtra("recieverimage");
        String recievern = intent.getStringExtra("recievername");

        String url;
        url  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+recieverimage;
        url = url.replace(" ","%20");
        Glide.with(ChatActivity.this).load(url).circleCrop().
                into(image);
        textView.setText(recievern);
        mediaFiles = new ArrayList<>();
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        attachfile = findViewById(R.id.attachfile);

        if(!isConnected(ChatActivity.this)){
            buildDialog3(ChatActivity.this,"We are sorry","Please Check Your Internet Connection.").show();
        }else {
            GetMessages();
        }
        attachfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   UploadPics();
                }

        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = text_send.getText().toString().trim();
                if(message.equals("") || message.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Cannot Send Empty Message", Toast.LENGTH_SHORT).show();
                }else{
                    arrayList.clear();
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    senderid = sharedPreferences.getString("id","");
                    sendername = sharedPreferences.getString("username","");
                    recieverid = intent.getStringExtra("recieverid");
                    recievername = intent.getStringExtra("recievername");
                    counter = sharedPreferences.getInt("counter",0);
                    counter = counter+1;
                    sendMessage(senderid,recieverid,sendername,recievername,message);
                    message="";
                    text_send.setText("");
                }
            }
        });
    }

    private void sendMessage(String senderid, String recieverid,String sendername,String recievername,String message) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ChatActivity.this);

        String url = Urls.SendFreelancerMessage;
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

                                buildDialog(ChatActivity.this,"502","Internal Server Error").show();
                            }
                            else if(status.equals("404")){

                               }else if(status.equals("200")){

                                Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                JSONArray sendername = reader.getJSONArray("sendername");
                                JSONArray recievername = reader.getJSONArray("recievername");
                                JSONArray senderid = reader.getJSONArray("senderid");
                                JSONArray recieverid = reader.getJSONArray("recieverid");
                                JSONArray message = reader.getJSONArray("message");
                                JSONArray type = reader.getJSONArray("type");
                                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                int counter = reader.getInt("counter");
                                editor.putInt("counter",counter);
                                editor.commit();
                                for (int i=0;i<senderid.length();i++){
                                    String sname = sendername.getString(i);
                                    String rname = recievername.getString(i);
                                    String sid = senderid.getString(i);
                                    String rid = recieverid.getString(i);
                                    String textmsg = message.getString(i);
                                    String texttype = type.getString(i);
                                    MessageDataModel dataModel = new MessageDataModel(sname,rname,textmsg,sid,rid,texttype);
                                    arrayList.add(dataModel);
                                }
                                adapter = new MessagesAdaphter(arrayList,ChatActivity.this,ChatActivity.this);

                                recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this,RecyclerView.VERTICAL,true));
                                recyclerView.setAdapter(adapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                buildDialog(ChatActivity.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("senderid", senderid);
                params.put("recieverid", recieverid);
                params.put("message", message);
                params.put("sendername",sendername);
                params.put("recievername",recievername);
                params.put("counter", String.valueOf(counter));

                params.put("type", "text");

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

    public void UploadPics(){
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowVideos(false)
                .setShowImages(true)
                .setMaxSelection(-1)
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
                loopsize = mediaFiles.size();
                MediaFile file = mediaFiles.get(i);
                filePath = file.getPath();
                arrayList.clear();
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                senderid = sharedPreferences.getString("id","");
                sendername = sharedPreferences.getString("username","");
                recieverid = intent.getStringExtra("recieverid");
                recievername = intent.getStringExtra("recievername");
                counter = sharedPreferences.getInt("counter",0);
                counter = counter+1;
                message="";
                text_send.setText("");
                uploadNow(senderid,recieverid,sendername,recievername,filePath);

            }
        }

    }
    private void uploadNow(String senderid,String recieverid,String sendername,String recievername,String filePath) {
        final ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setMessage("Uploading Your Pictures\n Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final File file = new File(filePath);
        Ion.with(ChatActivity.this)
                .load("POST", Urls.SendFreelancerImages)
                .setLogging("1312wWOD", Log.INFO)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        double progress = (100.0 * uploaded) / total;
                        progressDialog.setMessage("Uploading.. " + ((int) progress) + " %");
                    }
                })

                .setMultipartFile("file", "application/jpeg", file)
                .setMultipartParameter("senderid", senderid)
                .setMultipartParameter("recieverid", recieverid)
                .setMultipartParameter("sendername",sendername)
                .setMultipartParameter("recievername",recievername)
                .setMultipartParameter("counter", String.valueOf(counter))
                .setMultipartParameter("type", "media")

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

                                    buildDialog2(ChatActivity.this, "Please Try Again", "Record Not Saved").show();

                                }

                                else if (status.equals("200")) {
                                    loopcounter=loopcounter+1;
                                    if(loopcounter != loopsize){
                                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);


                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        int counter = mainObject.getInt("counter");
                                        editor.putInt("counter",counter);
                                    }else{
                                        Toast.makeText(ChatActivity.this, "Message Sent..", Toast.LENGTH_SHORT).show();
                                        JSONArray sendername = mainObject.getJSONArray("sendername");
                                        JSONArray recievername = mainObject.getJSONArray("recievername");
                                        JSONArray senderid = mainObject.getJSONArray("senderid");
                                        JSONArray recieverid = mainObject.getJSONArray("recieverid");
                                        JSONArray message = mainObject.getJSONArray("message");
                                        JSONArray type = mainObject.getJSONArray("type");
                                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        int counter = mainObject.getInt("counter");
                                        editor.putInt("counter",counter);
                                        editor.commit();
                                        for (int i=0;i<senderid.length();i++){
                                            String sname = sendername.getString(i);
                                            String rname = recievername.getString(i);
                                            String sid = senderid.getString(i);
                                            String rid = recieverid.getString(i);
                                            String textmsg = message.getString(i);
                                            String texttype = type.getString(i);
                                            MessageDataModel dataModel = new MessageDataModel(sname,rname,textmsg,sid,rid,texttype);
                                            arrayList.add(dataModel);
                                        }
                                        adapter = new MessagesAdaphter(arrayList,ChatActivity.this,ChatActivity.this);

                                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this,RecyclerView.VERTICAL,true));
                                        recyclerView.setAdapter(adapter);
                                    }

                                }

                            } catch (Exception ex) {
                                Log.i("312333", ex + "");
                            }
                        } else {

                            buildDialog2(ChatActivity.this, "Please Try Again", "Record Not Saved").show();
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
    public void GetMessages()
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ChatActivity.this);

        String url = Urls.GET_MESSAGES;
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

                                buildDialog(ChatActivity.this,"Please Try Again Later","Internal Server Error").show();
                            }
                            else if(status.equals("404")){
                               loader.setVisibility(View.GONE);
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                JSONArray sendername = reader.getJSONArray("sendername");
                                JSONArray recievername = reader.getJSONArray("recievername");
                                JSONArray senderid = reader.getJSONArray("senderid");
                                JSONArray recieverid = reader.getJSONArray("recieverid");
                                JSONArray message = reader.getJSONArray("message");
                                JSONArray type = reader.getJSONArray("type");

                                for (int i=0;i<senderid.length();i++){
                                    String sname = sendername.getString(i);
                                    String rname = recievername.getString(i);
                                    String sid = senderid.getString(i);
                                    String rid = recieverid.getString(i);
                                    String textmsg = message.getString(i);
                                    String texttype = type.getString(i);
                                    MessageDataModel dataModel = new MessageDataModel(sname,rname,textmsg,sid,rid,texttype);
                                    arrayList.add(dataModel);
                                }
                                adapter = new MessagesAdaphter(arrayList,ChatActivity.this,ChatActivity.this);

                                recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this,RecyclerView.VERTICAL,true));
                                recyclerView.setAdapter(adapter);
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
                buildDialog(ChatActivity.this,"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category", "");
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
    public AlertDialog.Builder buildDialog3(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        waitforsometime2();

        return builder;
    }
    public void waitforsometime2(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(ChatActivity.this,FreelancerDashboard.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
public void showimage(String imagename){
    isfragmentshowed=true;
    Log.d("12333", "showimage: "+imagename);
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);

    Bundle bundle = new Bundle();
    bundle.putString("imagename", imagename );


    ShowImage guideFragment = new ShowImage();
    guideFragment.setArguments(bundle);
    transaction.replace(R.id.container,guideFragment);
    transaction.commit();
    manager.popBackStack();
}

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
               case android.R.id.home:
                   if(isfragmentshowed){
                       container.setVisibility(View.GONE);
                       isfragmentshowed = false;
                   }else{
                       super.onBackPressed();
                   }
                   break;
            }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(isfragmentshowed){
            container.setVisibility(View.GONE);
            isfragmentshowed = false;
        }else{
            super.onBackPressed();
        }
    }
}