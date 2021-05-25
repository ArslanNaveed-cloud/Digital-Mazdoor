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
import androidx.recyclerview.widget.LinearLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderRequestAdaphter extends RecyclerView.Adapter<OrderRequestAdaphter.viewholder> {

    ArrayList<OrderDataModel> orderDataModels;
    Context context;
    String orderid;
    int count = 0;
    OrdersRequest ordersRequest;

    public OrderRequestAdaphter(ArrayList<OrderDataModel> orderDataModels, Context context, OrdersRequest ordersRequest) {
        this.orderDataModels = orderDataModels;
        this.context = context;
        this.ordersRequest = ordersRequest;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.orderrequestitemlayout, parent, false);

        return new OrderRequestAdaphter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {

        String url1;
        String url2;
        url1 = Urls.DOMAIN + "/assets/freelancersprofilepictures/" + orderDataModels.get(position).getBuyerimage();
        url1 = url1.replace(" ", "%20");

        holder.loader.setVisibility(View.GONE);
        Glide.with(context).load(url1).circleCrop().
                into(holder.buyerimage);

        holder.buyername.setText(orderDataModels.get(position).getBuyername());
        holder.description.setText(orderDataModels.get(position).getTitle());
        holder.budget.setText(orderDataModels.get(position).getBudget());
        holder.days.setText(orderDataModels.get(position).getDays());
        holder.date.setText(orderDataModels.get(position).getDate());
        holder.status.setText(orderDataModels.get(position).getStatus());


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    buildDialog(context, "Are You Sure?", "Do You Want To Accept This Order", orderDataModels.get(position).getOfferid(),holder).show();
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
                    buildDialog2(context, "Are You Sure?", "Do You Want To Reject This Order", orderDataModels.get(position).getOfferid(),holder).show();
                    count++;
                } else {
                    Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.buyerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BuyerProfileDetails.class);
                intent.putExtra("buyerid",orderDataModels.get(position).getBuyerid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDataModels.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        private ImageView buyerimage;
        TextView buyername, budget, title, description, date, days, status;
        Button accept, reject;
        LinearLayout loader;
        public viewholder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            accept = itemView.findViewById(R.id.accept);
            buyerimage = itemView.findViewById(R.id.buyerpic);
            buyername = itemView.findViewById(R.id.buyername);
            loader = itemView.findViewById(R.id.loader);
            budget = itemView.findViewById(R.id.budget);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            days = itemView.findViewById(R.id.days);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            reject = itemView.findViewById(R.id.reject);


        }
    }

    public AlertDialog.Builder buildDialog(Context c, String header, String message, final String orderid, final viewholder viewholder) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewholder.loader.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Please Wait While Order Is Being Accepted", Toast.LENGTH_SHORT).show();
                AcceptOrder(orderid);
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

    public AlertDialog.Builder buildDialog2(Context c, String header, String message, final String orderid, final viewholder viewholder) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Please Wait While Order Is Being Rejected", Toast.LENGTH_SHORT).show();
                viewholder.loader.setVisibility(View.VISIBLE);

                RejectOrder(orderid);
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

    public void waitforsometime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ordersRequest.endactivity();


            }
        }, 1500);
    }

    public void AcceptOrder(final String orderid) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Urls.ACCEPT_ORDER;
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
                params.put("orderid", orderid);
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

    public void RejectOrder(final String orderid) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Urls.REJECT_ORDER;
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
                                buildDialog3(context, "Order Rejected Successfully", "Check Order Request Daily").show();


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
                params.put("orderid", orderid);
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
