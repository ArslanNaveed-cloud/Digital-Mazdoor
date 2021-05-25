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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BuyerOrderRequest extends Fragment {


    private View view;
    private RecyclerView recyclerView;
    LinearLayout loader,mainlayout;
    ProgressBar bar;
    TextView message;
    String buyerid;
    BuyerOrderAdaphter adaphter;
    ArrayList<OrderDataModel> orderDataModels,newRequestDataModel;
    BuyerOrderRequest ordersRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_orders_request, container, false);
        orderDataModels = new ArrayList<>();
        recyclerView = view.findViewById(R.id.orderrequestlist);
        message = view.findViewById(R.id.message);
        mainlayout = view.findViewById(R.id.mylayout);
        loader = view.findViewById(R.id.loader);
        bar = view.findViewById(R.id.progress_circular);
        newRequestDataModel = new ArrayList<>();
        ordersRequest = this;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        buyerid = sharedPreferences.getString("id","");
        VolleyRequest();


        return view;}
    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.GET_BUYERORDRREQUEST;
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
                                bar.setVisibility(View.GONE);
                                message.setVisibility(View.VISIBLE);

                                message.setText("You Donot Have Any Request");
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                mainlayout.setVisibility(View.GONE);

                                JSONArray ordderid = reader.getJSONArray("orderid");
                                JSONArray buyerid = reader.getJSONArray("buyerid");
                                JSONArray buyername = reader.getJSONArray("buyername");
                                JSONArray gigname = reader.getJSONArray("gigname");
                                JSONArray budget = reader.getJSONArray("budget");
                                JSONArray date = reader.getJSONArray("date");
                                JSONArray days = reader.getJSONArray("days");
                                JSONArray buyerpic = reader.getJSONArray("buyerpic");
                                JSONArray orderstatus = reader.getJSONArray("orderstatus");

                                JSONArray freelancerid = reader.getJSONArray("freelancerid");

                                for (int i = 0; i < ordderid.length(); i++) {

                                    String oid = ordderid.getString(i);
                                    String pprice = budget.getString(i);
                                    String pdate = date.getString(i);
                                    String pdays = days.getString(i);
                                    String bid = buyerid.getString(i);
                                    String bname = buyername.getString(i);
                                    String gname = gigname.getString(i);
                                    String ostatus = orderstatus.getString(i);
                                    String buyerimage = buyerpic.getString(i);
                                    String freeid = freelancerid.getString(i);
                                    String title = "You Sent Request On Gig Titled '"+gname+" on "+pdate;
                                    OrderDataModel orderDataModel = new OrderDataModel(oid,bname,pprice,title,pdate,pdays,buyerimage,ostatus,bid,freeid);
                                    newRequestDataModel.add(orderDataModel);
                                }

                                adaphter = new BuyerOrderAdaphter(newRequestDataModel,getActivity(),ordersRequest);

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
                params.put("buyerid", buyerid);
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
    public void endactivity(){
        Intent intent = new Intent(getActivity(),FreelancerDashboard.class);
        startActivity(intent);
        getActivity().finish();
    }
}