package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BuyerOrderAdaphter extends RecyclerView.Adapter<BuyerOrderAdaphter.viewholder> {
    ArrayList<OrderDataModel> orderDataModels;
    Context context;
    String orderid;
    int count = 0;
    BuyerOrderRequest ordersRequest;

    public BuyerOrderAdaphter(ArrayList<OrderDataModel> orderDataModels, Context context, BuyerOrderRequest ordersRequest) {
        this.orderDataModels = orderDataModels;
        this.context = context;

        this.ordersRequest = ordersRequest;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.buyerorderitemlayout, parent, false);

        return new BuyerOrderAdaphter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        String url1;
        String url2;
        url1 = Urls.DOMAIN + "/assets/freelancersprofilepictures/" + orderDataModels.get(position).getBuyerimage();
        url1 = url1.replace(" ", "%20");

        Glide.with(context).load(url1).circleCrop().
                into(holder.buyerimage);

        holder.buyername.setText(orderDataModels.get(position).getBuyername());
        holder.description.setText(orderDataModels.get(position).getTitle());
        holder.budget.setText(orderDataModels.get(position).getBudget());
        holder.days.setText(orderDataModels.get(position).getDays());
        holder.date.setText(orderDataModels.get(position).getDate());
        holder.status.setText(orderDataModels.get(position).getStatus());

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

    public class viewholder extends RecyclerView.ViewHolder {

        private ImageView buyerimage;
        TextView buyername, budget, title, description, date, days, status;

        public viewholder(View itemView) {
            super(itemView);
            context = itemView.getContext();
             buyerimage = itemView.findViewById(R.id.buyerpic);
            buyername = itemView.findViewById(R.id.buyername);
            budget = itemView.findViewById(R.id.budget);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            days = itemView.findViewById(R.id.days);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);


        }
    }
}
