package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PostedRequests extends Fragment {


    View view;
    private TextView message;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<RequestDataModel> requestDataModels;
    private RequestAdaphter adaphter;
    private LinearLayout loader,mainlayout;
    private ProgressBar progress_circular;
    private String city;
    private String buyername;
    private String buyerid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_posted_requests, container, false);
        floatingActionButton = view.findViewById(R.id.fb_1);
        progress_circular = view.findViewById(R.id.progress_circular);
        loader = view.findViewById(R.id.loader);
        mainlayout = view.findViewById(R.id.mylayout);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        city = sharedPreferences.getString("city","");
        buyername = sharedPreferences.getString("username","");
        buyerid = sharedPreferences.getString("id","");
        requestDataModels = new ArrayList<>();
        message = view.findViewById(R.id.message);
        recyclerView = view.findViewById(R.id.requestlist);

        VolleyRequest();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getActivity(),AddRequest.class);
            getActivity().startActivity(intent);
            }
        });
        return view;
    }
    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.GET_REQUEST;
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
                                progress_circular.setVisibility(View.GONE);
                                message.setVisibility(View.VISIBLE);

                                message.setText("You Donot Have Any Request");
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                mainlayout.setVisibility(View.GONE);

                                JSONArray postid = reader.getJSONArray("postid");
                                JSONArray id = reader.getJSONArray("id");
                                JSONArray name = reader.getJSONArray("name");
                                JSONArray city = reader.getJSONArray("city");
                                JSONArray description = reader.getJSONArray("description");
                                JSONArray budget = reader.getJSONArray("budget");
                                JSONArray category = reader.getJSONArray("category");
                                JSONArray buyerpic = reader.getJSONArray("buyerpic");

                                JSONArray title = reader.getJSONArray("title");
                                JSONArray days = reader.getJSONArray("days");
                                JSONArray bids = reader.getJSONArray("bids");
                                JSONArray poststatus = reader.getJSONArray("poststatus");
                                JSONArray date = reader.getJSONArray("date");

                                for (int i = 0; i < postid.length(); i++) {

                                    String pid = postid.getString(i);
                                    String ptitle = title.getString(i);
                                    String pprice = budget.getString(i);
                                    String pdescription = description.getString(i);
                                    String pcategory = category.getString(i);
                                    String bpic = buyerpic.getString(i);
                                    int pbids = bids.getInt(i);
                                    String pstatus = poststatus.getString(i);

                                    String pdate = date.getString(i);
                                    String bcity = city.getString(i);
                                    String pdays = days.getString(i);
                                    String bid = bids.getString(i);
                                    String bname = name.getString(i);

                                    RequestDataModel itemDataModel = new RequestDataModel(pid,ptitle,pprice,pdescription,pcategory,bpic,pbids,pstatus,pdate,bcity,pdays,bid,bname);
                                    requestDataModels.add(itemDataModel);
                                }

                                adaphter = new RequestAdaphter(requestDataModels,getActivity());

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
                params.put("buyername", buyername);
                params.put("buyerid",buyerid);
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