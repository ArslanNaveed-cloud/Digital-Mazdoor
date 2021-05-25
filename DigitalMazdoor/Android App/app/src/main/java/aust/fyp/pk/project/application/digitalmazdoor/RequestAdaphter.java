package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RequestAdaphter extends RecyclerView.Adapter<RequestAdaphter.viewholder> {

    ArrayList<RequestDataModel> requestDataModels;
    Context context;

    public RequestAdaphter(ArrayList<RequestDataModel> requestDataModels, Context context) {
        this.requestDataModels = requestDataModels;
        this.context = context;
        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.requestitemlayout,parent,false);

        return new RequestAdaphter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
       holder.title.setText(requestDataModels.get(position).getPtitle());
        holder.description.setText(requestDataModels.get(position).getPdescription());


        holder.bids.setText(requestDataModels.get(position).getBid());

        holder.date.setText(requestDataModels.get(position).getPdate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,RequestDescriptiom.class);
                intent.putExtra("pid",requestDataModels.get(position).getPid());
                intent.putExtra("ptitle",requestDataModels.get(position).getPtitle());
                intent.putExtra("pprice",requestDataModels.get(position).getPprice());
                intent.putExtra("pdescription",requestDataModels.get(position).getPdescription());
                intent.putExtra("pcategory",requestDataModels.get(position).getPcategory());
                 intent.putExtra("pbids",requestDataModels.get(position).getBid());
                intent.putExtra("pstatus",requestDataModels.get(position).getPstatus());
                intent.putExtra("pdate",requestDataModels.get(position).getPdate());
                intent.putExtra("bcity",requestDataModels.get(position).getBcity());
                intent.putExtra("pdays",requestDataModels.get(position).getPdays());
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestDataModels.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        private TextView title,description,bids,date;
        public viewholder(View itemView) {
            super(itemView);
            context = itemView.getContext();
           title = itemView.findViewById(R.id.title);
           description = itemView.findViewById(R.id.description);
            bids = itemView.findViewById(R.id.bids);
           date = itemView.findViewById(R.id.date);

        }
    }
}
