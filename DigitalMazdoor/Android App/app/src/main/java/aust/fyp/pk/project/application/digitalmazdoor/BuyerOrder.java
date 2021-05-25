package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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


public class BuyerOrder extends Fragment {

    private View view;

    RecyclerView recyclerView;
    LinearLayout loader,mylayout;
    TextView message;
    BuyerOrdersAdaphter adaphter;
    ProgressBar progress_circular;
    private ArrayList<MyOrderDataModel> newRequestDataModels;
    private String city;
    private String category;
    private String buyerid;
    private String freelancerpicture;
    private boolean isfragmentshowed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_my_orders, container, false);
        recyclerView = view.findViewById(R.id.orderslist);
        message = view.findViewById(R.id.message);
        mylayout = view.findViewById(R.id.mylayout);
        newRequestDataModels = new ArrayList<>();
        progress_circular = view.findViewById(R.id.progress_circular);
        loader = view.findViewById(R.id.loader);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        city = sharedPreferences.getString("city","");
        buyerid = sharedPreferences.getString("id","");

        VolleyRequest();
        return view;
    }

    public void VolleyRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.GET_ORDERSFORBUYERS;
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

                                message.setText("You Didnot send any Order Request");
                            }else if(status.equals("200")){
                                loader.setVisibility(View.GONE);
                                mylayout.setVisibility(View.GONE);

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
                                }

                                adaphter = new BuyerOrdersAdaphter(newRequestDataModels,getActivity(),BuyerOrder.this);

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

    public void endactivity() {
        if(adaphter!=null){
            adaphter.notifyDataSetChanged();
            getActivity().finish();
        }

    }

    public void setrating(String freelancerid) {
        isfragmentshowed=true;
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);

        Bundle bundle = new Bundle();
        bundle.putString("fid", freelancerid );

        GiveRating guideFragment = new GiveRating();
        guideFragment.setArguments(bundle);
        transaction.replace(R.id.container,guideFragment);
        transaction.commit();
        manager.popBackStack();
    }

}