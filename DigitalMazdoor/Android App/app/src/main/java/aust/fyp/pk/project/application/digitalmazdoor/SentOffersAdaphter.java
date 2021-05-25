package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SentOffersAdaphter extends RecyclerView.Adapter<SentOffersAdaphter.viewholder> {
    ArrayList<NewRequestDataModel> requestDataModels;
    Context context;
    int count = 0;

    public SentOffersAdaphter(ArrayList<NewRequestDataModel> requestDataModels, Context context, int count) {
        this.requestDataModels = requestDataModels;
        this.context = context;
        this.count = count;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sentoffersitemlist,parent,false);

        return new SentOffersAdaphter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
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
        holder.date.setText("Uploaded On: "+requestDataModels.get(position).getDate());
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String postid = sharedPreferences.getString("postid","");
        holder.status.setText("Status: "+requestDataModels.get(position).getStatus());
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
        TextView buyername,category,budget,title,description,date,status;
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
           date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
        }
    }
}
