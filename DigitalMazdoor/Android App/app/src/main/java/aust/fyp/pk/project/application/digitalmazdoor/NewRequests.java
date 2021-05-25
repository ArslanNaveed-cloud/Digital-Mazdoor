package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewRequests extends Fragment {
    View view;
    RecyclerView recyclerView;
    LinearLayout loader,mylayout;
    TextView message;
    NewRequestAdaphter adaphter;
    ProgressBar progress_circular;
    private ArrayList<NewRequestDataModel> newRequestDataModels;
    private String city;
    private String category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_requests, container, false);
        recyclerView = view.findViewById(R.id.newrequestlist);
        message = view.findViewById(R.id.message);
        mylayout = view.findViewById(R.id.mylayout);
        newRequestDataModels = new ArrayList<>();
        progress_circular = view.findViewById(R.id.progress_circular);
        loader = view.findViewById(R.id.loader);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        city = sharedPreferences.getString("city","");
        category = sharedPreferences.getString("maincat","");
        VolleyRequest();
        return view;
    }
    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.GET_REQUESTFORBUYER;
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
                                mylayout.setVisibility(View.GONE);

                                JSONArray postid = reader.getJSONArray("postid");
                                JSONArray id = reader.getJSONArray("id");
                                JSONArray name = reader.getJSONArray("name");
                                JSONArray description = reader.getJSONArray("description");
                                JSONArray budget = reader.getJSONArray("budget");
                                JSONArray category = reader.getJSONArray("category");
                                JSONArray buyerpic = reader.getJSONArray("buyerpic");

                                JSONArray title = reader.getJSONArray("title");
                                JSONArray days = reader.getJSONArray("days");
                                JSONArray bids = reader.getJSONArray("bids");
                                JSONArray date = reader.getJSONArray("date");
                                JSONArray orderstatus = reader.getJSONArray("orderstatus");

                                for (int i = 0; i < postid.length(); i++) {

                                    String pid = postid.getString(i);
                                    String ptitle = title.getString(i);
                                    String pprice = budget.getString(i);
                                    String pdescription = description.getString(i);
                                    String pcategory = category.getString(i);
                                    String bpic = buyerpic.getString(i);
                                    int pbids = bids.getInt(i);

                                    String pdate = date.getString(i);
                                    String pdays = days.getString(i);
                                    String buyerid = id.getString(i);
                                    String bname = name.getString(i);
                                    String ostatus = orderstatus.getString(i);
                                    NewRequestDataModel newRequestDataModel = new NewRequestDataModel(ptitle,pdescription,pcategory,pprice,pdays,bpic,pid,buyerid,bname,pdate,pbids,ostatus);
                                    newRequestDataModels.add(newRequestDataModel);
                                }

                                adaphter = new NewRequestAdaphter(newRequestDataModels,getActivity());

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
                params.put("city", city);
                params.put("category",category);
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

    @Override
    public void onResume() {
        super.onResume();
        if(adaphter!=null){
            adaphter.notifyDataSetChanged();

        }
        }
}