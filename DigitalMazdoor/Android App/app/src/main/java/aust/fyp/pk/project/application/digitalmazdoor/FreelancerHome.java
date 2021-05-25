package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class FreelancerHome extends Fragment {
    String[] cat_names = {"Total Earnings","Total Gigs","Total Orders","Total Reviews","In Queue Orders","Completed Orders"};

    int[] cat_icons = {R.drawable.earnings,R.drawable.gigs,R.drawable.ic_005_shopping_bag,R.drawable.ic_001_rating,R.drawable.ic_002_queue,R.drawable.ic_003_checklist};
   private ArrayList<freelancercategorydatamodel> arraylist;
    private RecyclerView recyclerView,categorylist;
    private View view;
    private FCategoryAdaphter categoryAdaphter;
    GridLayoutManager gridLayoutManager;
    LinearLayout loader;
    private String id;
    private String rating;
    OrderAdaphter adaphter;

    private ArrayList<MyOrderDataModel> newRequestDataModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_freelancer_home, container, false);
        loader = view.findViewById(R.id.loader);
        newRequestDataModels=new ArrayList<>();
        arraylist = new ArrayList<>();
        recyclerView = view.findViewById(R.id.orders_list);
        categorylist = view.findViewById(R.id.categorylist);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            id = sharedPreferences.getString("id","");

            VolleyRequest();
        VolleyRequest2();
        return view;
    }

    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.GET_DETAILS;
        Log.i("112233",url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject reader = new JSONObject(response);
                            String status = reader.getString("status");
                            Log.d(TAG, "onResponse: "+reader);
                            if(status.equals("500")){
                                loader.setVisibility(View.GONE);

                                buildDialog(getActivity(),"502","Internal Server Error").show();
                            }
                            else if(status.equals("404")){
                                loader.setVisibility(View.GONE);

                                }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                JSONArray cat_value = reader.getJSONArray("catvalues");

                                for(int i = 0;i<cat_names.length;i++){
                                String categoryname = cat_names[i];
                                int categoryimages = cat_icons[i];
                                String value = cat_value.getString(i);

                                freelancercategorydatamodel datamodel = new freelancercategorydatamodel(categoryname,value,categoryimages);
                                arraylist.add(datamodel);

                            }
                                categoryAdaphter = new FCategoryAdaphter(FreelancerHome.this,arraylist);
                                gridLayoutManager = new GridLayoutManager(getActivity(),3, LinearLayoutManager.VERTICAL,false);
                                categorylist.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
                                //  call the constructor of CustomAdapter to send the reference and data to Adapter
                                categorylist.setAdapter(categoryAdaphter);
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
    public void VolleyRequest2(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.GET_PENDINGORDERS;
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

                                buildDialog(getActivity(),"Please Try Again Later","Internal Server Error").show();
                            }
                            else if(status.equals("404")){


                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);

                                JSONArray postid = reader.getJSONArray("postid");
                                JSONArray id = reader.getJSONArray("buyerid");
                                JSONArray name = reader.getJSONArray("buyername");
                                JSONArray budget = reader.getJSONArray("budget");
                                JSONArray category = reader.getJSONArray("category");
                                JSONArray buyerpic = reader.getJSONArray("buyerpic");


                                JSONArray days = reader.getJSONArray("days");

                                JSONArray date = reader.getJSONArray("date");
                                JSONArray orderstatus = reader.getJSONArray("orderstatus");
                                JSONArray freelancerid = reader.getJSONArray("freelancerid");
                                JSONArray freelancername = reader.getJSONArray("freelancername");
                                JSONArray freelancerimage = reader.getJSONArray("freelancerimage");


                                for (int i = 0; i < postid.length(); i++) {

                                    String pid = postid.getString(i);

                                    String pprice = budget.getString(i);

                                    String pcategory = category.getString(i);
                                    String bpic = buyerpic.getString(i);


                                    String pdate = date.getString(i);
                                    String pdays = days.getString(i);
                                    String buyerid = id.getString(i);
                                    String bname = name.getString(i);
                                    String ostatus = orderstatus.getString(i);
                                    String Freelancerid = freelancerid.getString(i);
                                    String Freelancername = freelancername.getString(i);
                                    String Freelancerprofilepicture = freelancerimage.getString(i);

                                    MyOrderDataModel orderDataModel = new MyOrderDataModel(pid,bname,buyerid,bpic,Freelancerid,Freelancername,Freelancerprofilepicture,pprice,"",pcategory,pdays,pdate,pdays,ostatus);
                                    newRequestDataModels.add(orderDataModel);
                                    Log.d(TAG, "Pending Orders: "+newRequestDataModels.get(0).getStatus());
                                }

                                adaphter = new OrderAdaphter(newRequestDataModels,getActivity(),FreelancerHome.this,"");

                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(adaphter);
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
                params.put("freelancerid", id);

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
    public void endactivity() {
        if(adaphter!=null){
            adaphter.notifyDataSetChanged();
            getActivity().finish();
        }
    }
}