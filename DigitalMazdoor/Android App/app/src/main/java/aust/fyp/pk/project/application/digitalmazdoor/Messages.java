package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Messages extends Fragment {

    View view;
    private String freelancerid;
    LinearLayout loader;
    RecyclerView recyclerView;
    ArrayList<UserListDataModel> userListDataModels;
    UserListAdaphter adapter;
    String identifierid="";
    LinearLayout mylayout;
    TextView message;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_messages, container, false);
        loader = view.findViewById(R.id.loader);
        userListDataModels = new ArrayList<>();
        message = view.findViewById(R.id.message);
        mylayout = view.findViewById(R.id.mylayout);
        recyclerView = view.findViewById(R.id.userslist);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            freelancerid = sharedPreferences.getString("id","");
        if(!isConnected(getActivity())){
            buildDialog(getActivity(),"We are sorry","Please Check Your Internet Connection.").show();
        }else {
            GetBuyers();
        }


        return view;
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
                Intent intent = new Intent(getActivity(),FreelancerDashboard.class);
                startActivity(intent);
                getActivity().finish();
            }
        }, 2000);
    }
    public void GetBuyers(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Urls.GETBUYERS;
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
                                mylayout.setVisibility(View.VISIBLE);
                                message.setText("You Donot Have Any Message");
                                     }else if(status.equals("200")){
                                mylayout.setVisibility(View.GONE);
                                loader.setVisibility(View.GONE);
                                JSONArray buyername = reader.getJSONArray("buyername");
                                JSONArray buyeremail = reader.getJSONArray("buyeremail");
                                JSONArray buyerid = reader.getJSONArray("buyerid");
                                JSONArray buyerimage = reader.getJSONArray("buyerimage");
                                JSONArray message = reader.getJSONArray("messages");
                                JSONArray type = reader.getJSONArray("type");

                                for(int i=0;i<buyerid.length();i++){
                                    String bid = buyerid.getString(i);
                                    if(identifierid.contains(bid)){

                                    }else{
                                        identifierid=identifierid+bid+" ";

                                        String bname = buyername.getString(i);
                                        String bimage = buyerimage.getString(i);
                                        String bemail = buyeremail.getString(i);
                                        String text = message.getString(i);
                                        String typeofmessage = type.getString(i);
                                        UserListDataModel userListDataModel = new UserListDataModel(bname,bimage,bemail,text,bid,typeofmessage);
                                        userListDataModels.add(userListDataModel);
                                    }

                                }
                                adapter = new UserListAdaphter(userListDataModels,getActivity());

                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
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
                buildDialog(getActivity(),"Error","Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("freelancerid", freelancerid);
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