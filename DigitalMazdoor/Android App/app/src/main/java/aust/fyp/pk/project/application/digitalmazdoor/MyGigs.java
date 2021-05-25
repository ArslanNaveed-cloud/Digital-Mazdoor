package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyGigs extends Fragment{

    View view;
    private FloatingActionButton fb_1;
    private boolean isuserloggedin;
    public boolean isaction_mode = false;
    Toolbar toolbar;
    private TextView toolbartext;
    private ArrayList<GigDataModel> mygigsselectionlist,gigDataModels;
    private String id;
    private String username;
    private LinearLayout loader;
    private MyGigsAdaphter adapter;
    RecyclerView recyclerView;
    private LinearLayout mylayout;
    private TextView message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_gigs, container, false);
        fb_1 = view.findViewById(R.id.fb_1);
        message = view.findViewById(R.id.message);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        isuserloggedin =  sharedPreferences.getBoolean("isuserloggedin",false);
        id = sharedPreferences.getString("id","");
        username = sharedPreferences.getString("username","");
        loader = view.findViewById(R.id.loader);
        mylayout = view.findViewById(R.id.mylayout);
        recyclerView = view.findViewById(R.id.gigslist);
        VolleyRequest();
        gigDataModels = new ArrayList<>();
        mygigsselectionlist = new ArrayList<>();

        fb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int totalgigs = sharedPreferences.getInt("totalgigs",0);
                if(totalgigs==5){
                    Toast.makeText(getActivity(), "You Can Only Upload 5 Gigs", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getActivity(),AddGigs.class);
                    startActivity(intent);

                }

            }
        });
        return view;
    }

    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.GET_GIGS;
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

                                message.setText("You Donot Have Any Gigs");
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                JSONArray gigid = reader.getJSONArray("gigid");
                                JSONArray gigtitle = reader.getJSONArray("gigtitle");
                                JSONArray gigprice = reader.getJSONArray("gigprice");
                                JSONArray gigdescription = reader.getJSONArray("gigdescription");
                                JSONArray gigcoverimage = reader.getJSONArray("gigcoverimage");
                                JSONArray gigcategory = reader.getJSONArray("gigcategory");
                                JSONArray gigdateofuploading = reader.getJSONArray("gigdateofuploading");
                                JSONArray gigdelliveytime = reader.getJSONArray("gigdeliverytime");

                                for (int i = 0; i < gigid.length(); i++) {
                                    String id = gigid.getString(i);
                                    String title = gigtitle.getString(i);
                                    String price = gigprice.getString(i);
                                    String description = gigdescription.getString(i);
                                    String coverimage = gigcoverimage.getString(i);
                                    String cateogry = gigcategory.getString(i);
                                    String date = gigdateofuploading.getString(i);
                                    String deliverytime = gigdelliveytime.getString(i);
                                    GigDataModel  dataModel = new GigDataModel(title,cateogry,price,deliverytime,coverimage,description,id,date);
                                    gigDataModels.add(dataModel);
                                }

                                    adapter = new MyGigsAdaphter(gigDataModels,getActivity());
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                                recyclerView.setAdapter(adapter);
                                mylayout.setVisibility(View.GONE);


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
                params.put("username", username);
                params.put("id", id);
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

            }
        }, 2000);
    }

}