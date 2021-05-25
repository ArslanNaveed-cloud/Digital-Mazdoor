package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class GiveRating extends Fragment {

    View view;
    RatingBar ratingBar;
    Button ratenow;
    float myrating;
    private String id;
    int count = 0;
    LinearLayout loader;
    boolean buyerrating = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

               view=  inflater.inflate(R.layout.fragment_give_rating, container, false);
               ratingBar = view.findViewById(R.id.ratingbar);
               ratenow = view.findViewById(R.id.placeorder);
               if(getArguments().getString("identifier")!=null){
                   if(getArguments().getString("identifier").equals("buyerrating")){
                       id = getArguments().getString("bid");
                       buyerrating = true;
                   }

               }else{
                  id = getArguments().getString("fid");

               }
        loader = view.findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
               ratenow.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(count == 0){
                           count++;

                           if(myrating == 0){
                               Toast.makeText(getActivity(), "Please Give Rating", Toast.LENGTH_SHORT).show();
                           }else{
                               loader.setVisibility(View.VISIBLE);
                               VolleyRequest();
                           }
                       }else{
                           Toast.makeText(getActivity(),"Please Wait...",Toast.LENGTH_LONG).show();
                       }


                   }
               });
               ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                   @Override
                   public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                       myrating = rating;
                   }
               });
        return view;

    }
    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ;

        if(buyerrating){
            url= Urls.GIVEBUYERRATING;
        }else{
            url= Urls.GIVEFREELANCERRATING;
        }
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

                                buildDialog(getActivity(),"Please Try Again Later","Internal Server Error").show();
                            }
                            else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                if(getArguments().getString("identifier")!=null){
                                    if(getArguments().getString("identifier").equals("buyerrating")){
                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("freelancingratinginfo", Context.MODE_PRIVATE);
                                        String id = reader.getString("freelancerid");
                                        String freelancerid = sharedPreferences.getString("freelancerid","");
                                        freelancerid= freelancerid+id+",";
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("freelancerid",freelancerid);
                                        editor.commit();
                                    }

                                }else{
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("buyerratinginfo", Context.MODE_PRIVATE);
                                    String id = reader.getString("freelancerid");
                                    String freelancerid = sharedPreferences.getString("freelancerid","");
                                    freelancerid= freelancerid+id+",";
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("freelancerid",freelancerid);
                                    editor.commit();

                                }
                                buildDialog(getActivity(),"Rated Successfully","").show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loader.setVisibility(View.GONE);

                buildDialog(getActivity(),"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("freelancerid",id);
                params.put("rating",String.valueOf(myrating));
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

                if(getArguments().getString("identifier")!=null){
                    Intent intent = new Intent(getActivity(),FreelancerDashboard.class);
                    startActivity(intent);

                }else{
                    Intent intent = new Intent(getActivity(),BuyerDashboard.class);
                    startActivity(intent);

                }

            }
        }, 2000);
    }
}