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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewRequestAdaphter extends RecyclerView.Adapter<NewRequestAdaphter.viewholder> {

    ArrayList<NewRequestDataModel> requestDataModels;
    Context context;
    int count = 0;

    public NewRequestAdaphter(ArrayList<NewRequestDataModel> requestDataModels, Context context) {
        this.requestDataModels = requestDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.newrequestitemlayout,parent,false);

        return new NewRequestAdaphter.viewholder(view);
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
        holder.category.setText("Requires "+requestDataModels.get(position).getCategory());
        holder.description.setText(requestDataModels.get(position).getDescription());
        holder.title.setText(requestDataModels.get(position).getTitle());
        holder.budget.setText("Budget: "+requestDataModels.get(position).getBudget() +" In "+requestDataModels.get(position).getDays1()+" days");
        holder.bids.setText("Total Bids: "+requestDataModels.get(position).getBid());
        holder.date.setText("Uploaded On "+requestDataModels.get(position).getDate());
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String postid = sharedPreferences.getString("postid","");

        if(postid.contains(requestDataModels.get(position).getPostid())){
            holder.sendoffer.setVisibility(View.GONE);
        }else{
            holder.sendoffer.setVisibility(View.VISIBLE);
        }
        holder.sendoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context,SendOffer.class);
                    intent.putExtra("postid",requestDataModels.get(position).getPostid());
                    intent.putExtra("title",requestDataModels.get(position).getTitle());
                    intent.putExtra("buyername",requestDataModels.get(position).getBuyername());
                    intent.putExtra("category",requestDataModels.get(position).getCategory());
                    intent.putExtra("buyerid",requestDataModels.get(position).getBuyerid());
                    intent.putExtra("postid",requestDataModels.get(position).getPostid());
                    intent.putExtra("buyerpicture",requestDataModels.get(position).getBuyerimage());
                    context.startActivity(intent);



            }
        });
        holder.buyerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BuyerProfileDetails.class);
                intent.putExtra("buyerid",requestDataModels.get(position).getBuyerid());
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
      TextView buyername,category,budget,title,description,bids,date;
      Button sendoffer;
        public viewholder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            sendoffer = itemView.findViewById(R.id.sendoffer);
            buyerimage = itemView.findViewById(R.id.buyerpic);
            buyername = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            budget = itemView.findViewById(R.id.budget);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            bids = itemView.findViewById(R.id.bids);
            date = itemView.findViewById(R.id.date);
        }
    }
    public AlertDialog.Builder buildDialog(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }


}
