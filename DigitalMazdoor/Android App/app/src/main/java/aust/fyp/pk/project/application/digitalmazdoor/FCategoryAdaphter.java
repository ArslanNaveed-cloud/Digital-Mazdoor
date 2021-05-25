package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FCategoryAdaphter extends RecyclerView.Adapter<FCategoryAdaphter.Categoryviewholder> {
    private FreelancerHome context;
    public ArrayList<freelancercategorydatamodel> categoryitemdatamodelArrayList;
    public ArrayList<freelancercategorydatamodel> categoryitemdatamodelArrayListFull;
    private FreelancerHome homeFragment;

    public FCategoryAdaphter(FreelancerHome context,ArrayList<freelancercategorydatamodel>categoryitemdatamodelArrayList) {
        this.context = context;
        this.categoryitemdatamodelArrayList = categoryitemdatamodelArrayList;
        this.homeFragment =  context;
        this.categoryitemdatamodelArrayListFull = new ArrayList<>(categoryitemdatamodelArrayList);
    }

    @NonNull
    @Override
    public Categoryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.freelancercategoryitemlist,parent,false);

        return new FCategoryAdaphter.Categoryviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Categoryviewholder holder, final int position) {
        Glide.with(context).load(categoryitemdatamodelArrayList.get(position).getCat_icon()).
                into(holder.catimage);

        holder.catname.setText(categoryitemdatamodelArrayList.get(position).getCatname());
        holder.value.setText(categoryitemdatamodelArrayList.get(position).getValue());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryitemdatamodelArrayList.size();
    }

    public class Categoryviewholder extends RecyclerView.ViewHolder{

        private ImageView catimage;
        private TextView catname,value;
        public Categoryviewholder(View itemView) {
            super(itemView);
            catimage = itemView.findViewById(R.id.imageview);
            catname = itemView.findViewById(R.id.cattext);
            value = itemView.findViewById(R.id.value);
        }
    }
}
