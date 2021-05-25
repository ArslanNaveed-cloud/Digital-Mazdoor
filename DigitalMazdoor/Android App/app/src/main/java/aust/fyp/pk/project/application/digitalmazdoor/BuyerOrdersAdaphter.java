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

public class BuyerOrdersAdaphter extends RecyclerView.Adapter<BuyerOrdersAdaphter.viewholder> {
    ArrayList<MyOrderDataModel> orderDataModels;
    Context context;
    int count = 0;
    BuyerOrder buyerOrder;
    public BuyerOrdersAdaphter(ArrayList<MyOrderDataModel> orderDataModels, Context context,BuyerOrder buyerOrder) {
        this.orderDataModels = orderDataModels;
        this.context = context;
        this.buyerOrder = buyerOrder;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.buyerorderitemlistlayout,parent,false);

        return new BuyerOrdersAdaphter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        String url1;
        String url2;
        url1  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+orderDataModels.get(position).getBuyerimage();
        url1 = url1.replace(" ","%20");


        Glide.with(context).load(url1).circleCrop().
                into(holder.buyerimage);

        holder.buyername.setText(orderDataModels.get(position).getBuyername());
        holder.category.setText("Provided You "+orderDataModels.get(position).getCategory());

        holder.budget.setText("Budget: "+orderDataModels.get(position).getBudget() +" In "+orderDataModels.get(position).getDays1()+" days");
        holder.date.setText("Uploaded On: "+orderDataModels.get(position).getDate());
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        holder.loader.setVisibility(View.GONE);
        holder.ratingholder.setVisibility(View.GONE);
        String orderid = sharedPreferences.getString("myorderid","");
        if(orderid.contains(orderDataModels.get(position).getOfferid())){
            holder.buttonholder.setVisibility(View.GONE);
        }else {
            holder.buttonholder.setVisibility(View.VISIBLE);
        }

        if(!orderDataModels.get(position).getStatus().equals("Pending")){
            holder.ratingholder.setVisibility(View.VISIBLE);
        }

        holder.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    buildDialog(context, "Are You Sure?", "Do You Want To Mark This Order As Delivered?", orderDataModels.get(position).getOfferid(),holder).show();
                    count++;
                } else {
                    Toast.makeText(context, "Please Wait..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    buildDialog2(context, "Are You Sure?", "Do You Want To Mark This Order As Cancelled?", orderDataModels.get(position).getOfferid(),holder).show();
                    count++;
                } else {
                    Toast.makeText(context, "Please Wait..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.status.setText("Status: "+orderDataModels.get(position).getStatus());

        holder.ratenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyerOrder.setrating(orderDataModels.get(position).getFreelancerid());
            }
        });

        SharedPreferences sharedPreferences1 = context.getSharedPreferences("buyerratinginfo",Context.MODE_PRIVATE);
        String freelancerid = sharedPreferences1.getString("freelancerid","");
        if(freelancerid.contains(orderDataModels.get(position).getFreelancerid())){
            holder.ratingholder.setVisibility(View.GONE);
        }
        holder.buyerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FreelancerProfileDetails.class);
                intent.putExtra("freelancerid",orderDataModels.get(position).getBuyerid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDataModels.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        private ImageView buyerimage;
        TextView buyername,category,budget,title,description,date,status;
        Button delivered,cancelled,ratenow;
        private LinearLayout buttonholder,loader,ratingholder;
        public viewholder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            delivered = itemView.findViewById(R.id.delivered);
            cancelled = itemView.findViewById(R.id.cancel);
            buyerimage = itemView.findViewById(R.id.buyerpic);
            buyername = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            budget = itemView.findViewById(R.id.budget);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            buttonholder = itemView.findViewById(R.id.buttonholder);
            loader = itemView.findViewById(R.id.loader);
            ratingholder = itemView.findViewById(R.id.ratingholder);
            ratenow = itemView.findViewById(R.id.ratenow);
        }
    }
    public AlertDialog.Builder buildDialog(Context c, String header, String message, final String orderid, final BuyerOrdersAdaphter.viewholder viewholder) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewholder.loader.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Please Wait While Order Status is Being Updated", Toast.LENGTH_SHORT).show();
                UpdateOrderStatus(orderid,"Delivered");
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
    public AlertDialog.Builder buildDialog2(Context c, String header, String message, final String orderid, final BuyerOrdersAdaphter.viewholder viewholder) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewholder.loader.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Please Wait While Order Status is Being Updated", Toast.LENGTH_SHORT).show();
                UpdateOrderStatus(orderid,"Cancelled");
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

    private void UpdateOrderStatus(final String orderid,final String orderstatus) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Urls.UPDATEORDERSTATUS;
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
                                buildDialog3(context, "Order Status Changed Successfully", "Check My Orders To See Details").show();
                                SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String orderid = reader.getString("orderid");
                                orderid = sharedPreferences.getString("myorderid","")+" "+orderid;
                                editor.putString("myorderid",orderid);
                                editor.commit();
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
                params.put("orderstatus", orderstatus);

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
                buyerOrder.endactivity();


            }
        }, 1500);
    }
}
