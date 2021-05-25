package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PlaceOrder extends Fragment {

  View view;
    TextInputEditText budget,time;
    private Button placeorder;
    String price="",days="",buyername,buyerid,gigid,gigtitle,fid;
    LinearLayout loader;
    private String buyerpic;
    private String profileimage;
    private String category;
    private String freelancername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_place_order, container, false);
            budget = view.findViewById(R.id.budget);
            time = view.findViewById(R.id.time);
        placeorder = view.findViewById(R.id.placeorder);
        loader = view.findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gigid = getArguments().getString("gigid");
                fid = getArguments().getString("fid");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                buyerpic = sharedPreferences.getString("profilepicture","");
                buyerid = sharedPreferences.getString("id","");
                buyername = getArguments().getString("buyername");
                profileimage = getArguments().getString("profileimage");
                gigtitle = getArguments().getString("gigtitle");
                category = getArguments().getString("category");
                freelancername = getArguments().getString("freelancername");
                price = budget.getText().toString().trim();
                    days = time.getText().toString().trim();
                if(days.isEmpty() || days.equals(""))
                {
                    time.setError("Field Cannot be empty");

                }
                   else if(price.isEmpty() || price.equals("")) {
                        budget.setError("Field Cannot Be Empty");
                    }
                    else{
                        int intprice = Integer.parseInt(price);
                        int inttime = Integer.parseInt(days);
                        if(inttime<=0){
                            time.setError("Incorrect Days");
                        }else if(intprice<=0){
                            budget.setError("Incorrect Budget");

                        }else
                        {
                            loader.setVisibility(View.VISIBLE);
                            VolleyRequest();
                        }
                    }


            }
        });
        return view;
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
        waitforsometime2();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }
    public void waitforsometime2(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getActivity().finish();
            }
        }, 6000);
    }
    public void waitforsometime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getActivity().finish();
            }
        }, 2000);
    }
    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.PLACE_ORDERS;
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

                                buildDialog(getActivity(),"Error","Internal Server Error").show();
                            }
                           else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                buildDialog2(getActivity(),"Your Order is Placed","Order will be sarted once your Freelancer accepts your order").show();
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
                params.put("freelancerid",fid);
                params.put("freelancername",freelancername);
                params.put("freelancerimage",profileimage);
                params.put("buyername", buyername);
                params.put("buyerid", buyerid);
                params.put("profilepicture", buyerpic);
                params.put("gigid", gigid);
                params.put("gigtitle", gigtitle);
                params.put("budget", price);
                params.put("time", days);
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
}