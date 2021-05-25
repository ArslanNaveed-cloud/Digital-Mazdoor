package aust.fyp.pk.project.application.digitalmazdoor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowOffersAdpahter extends RecyclerView.Adapter<ShowOffersAdpahter.viewholder> {
    ArrayList<NewRequestDataModel> requestDataModels;
    Context context;
    int count = 0;
    ShowOffers showOffers;

    public ShowOffersAdpahter(ArrayList<NewRequestDataModel> requestDataModels, Context context, ShowOffers showOffers) {
        this.requestDataModels = requestDataModels;
        this.context = context;
        this.count = count;
        this.showOffers=showOffers;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.showofferitemlistlayout,parent,false);

        return new ShowOffersAdpahter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        String url1;
        String url2;
        url1  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+requestDataModels.get(position).getBuyerimage();
        url1 = url1.replace(" ","%20");


        Glide.with(context).load(url1).circleCrop().
                into(holder.buyerimage);

        holder.buyername.setText(requestDataModels.get(position).getBuyername());
        holder.category.setText("Provides "+requestDataModels.get(position).getCategory());
        holder.description.setText(requestDataModels.get(position).getDescription());
        holder.title.setText(requestDataModels.get(position).getTitle());
        holder.budget.setText("Offered Budget: "+requestDataModels.get(position).getBudget() +" In "+requestDataModels.get(position).getDays1()+" days");
        holder.date.setText("Uploaded On: "+requestDataModels.get(position).getDate());
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        holder.loader.setVisibility(View.GONE);
        String postid = sharedPreferences.getString("postid","");
        holder.status.setText("Status: "+requestDataModels.get(position).getStatus());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    buildDialog(context, "Are You Sure?", "Do You Want To Accept This Offer", requestDataModels.get(position).getPostid(),holder).show();
                    count++;
                } else {
                    Toast.makeText(context, "Please Wait..", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    buildDialog2(context, "Are You Sure?", "Do You Want To Reject This Offer", requestDataModels.get(position).getPostid(),holder).show();
                    count++;
                } else {
                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.buyerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FreelancerProfileDetails.class);
                intent.putExtra("freelancerid",requestDataModels.get(position).getBuyerid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestDataModels.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        private ImageView buyerimage;
        TextView buyername,category,budget,title,description,date,status;
        Button accept,reject;
        LinearLayout loader;
        public viewholder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
            loader = itemView.findViewById(R.id.loader);
            buyerimage = itemView.findViewById(R.id.buyerpic);
            buyername = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            budget = itemView.findViewById(R.id.budget);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
        }
    }

    public AlertDialog.Builder buildDialog(Context c, String header, String message, final String orderid, final ShowOffersAdpahter.viewholder viewholder) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewholder.loader.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Please Wait While Offer Is Being Accepted", Toast.LENGTH_SHORT).show();
                AppectOffer(orderid);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewholder.loader.setVisibility(View.GONE);

            }
        });
        return builder;
    }


    private void AppectOffer(final String orderid) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Urls.ACCEPT_OFFER;
        Log.i("112233", url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject reader = new JSONObject(response);
                            Log.d("GiGLIST", "onResponse: " + reader);
                            String status = reader.getString("status");

                            if (status.equals("500")) {
                                count = 0;
                                buildDialog3(context, "Please Try Again Later", "Internal Server Error").show();
                            } else if (status.equals("200")) {
                                count = 0;
                                buildDialog3(context, "Order Accepted Successfully", "Check My Orders To See Details").show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                buildDialog3(context, "Error", "Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("offerid", orderid);
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
    private void RejectOffer(final String orderid) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Urls.REJECT_OFFER;
        Log.i("112233", url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject reader = new JSONObject(response);
                            Log.d("GiGLIST", "onResponse: " + reader);
                            String status = reader.getString("status");

                            if (status.equals("500")) {
                                count = 0;
                                buildDialog3(context, "Please Try Again Later", "Internal Server Error").show();
                            } else if (status.equals("200")) {
                                count = 0;
                                buildDialog3(context, "Order Rejected Successfully", "Check My Orders To See Details").show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                buildDialog3(context, "Error", "Please Try Again Later").show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("offerid", orderid);
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

    public AlertDialog.Builder buildDialog2(Context c, String header, String message, final String orderid, final ShowOffersAdpahter.viewholder viewholder) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Please Wait While Offer Is Being Rejected", Toast.LENGTH_SHORT).show();
                viewholder.loader.setVisibility(View.VISIBLE);

                RejectOffer(orderid);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewholder.loader.setVisibility(View.GONE);

            }
        });
        return builder;
    }
    public AlertDialog.Builder buildDialog3(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        waitforsometime();
        return builder;
    }
    public void waitforsometime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                showOffers.endactivity();


            }
        }, 1500);
    }
}
