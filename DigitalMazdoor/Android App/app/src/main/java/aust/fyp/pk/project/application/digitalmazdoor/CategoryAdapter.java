package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Categoryviewholder> implements Filterable {
    private HomeFragment context;
    public ArrayList<categoryitemdatamodel> categoryitemdatamodelArrayList;
    public ArrayList<categoryitemdatamodel> categoryitemdatamodelArrayListFull;
    private HomeFragment homeFragment;

    public CategoryAdapter(HomeFragment context,ArrayList<categoryitemdatamodel> categoryitemdatamodelArrayList) {
        this.context = context;
        this.categoryitemdatamodelArrayList = categoryitemdatamodelArrayList;
        this.homeFragment =  context;
        this.categoryitemdatamodelArrayListFull = new ArrayList<>(categoryitemdatamodelArrayList);
    }


    @NonNull
    @Override
    public Categoryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.categoryitemlayout,parent,false);

        return new CategoryAdapter.Categoryviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Categoryviewholder holder, final int position) {
        Glide.with(context).load(categoryitemdatamodelArrayList.get(position).getImagename()).
                into(holder.catimage);

        holder.catname.setText(categoryitemdatamodelArrayList.get(position).getCategoryname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeFragment.getActivity(), Subcategorylist.class);
                intent.putExtra("catname",categoryitemdatamodelArrayList.get(position).getCategoryname());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryitemdatamodelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return myfilter;
    }
    private Filter myfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<categoryitemdatamodel> filterlist = new ArrayList<>();

            if(constraint == null || constraint.length() == 0||constraint.toString().isEmpty()){
                filterlist.addAll(categoryitemdatamodelArrayListFull);
            }else{
                String FilterPattern = constraint.toString().toLowerCase().trim();
                for(categoryitemdatamodel item:categoryitemdatamodelArrayListFull){
                    if(item.getCategoryname().toLowerCase().contains(FilterPattern)){
                        filterlist.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categoryitemdatamodelArrayList.clear();
            categoryitemdatamodelArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
    public class Categoryviewholder extends RecyclerView.ViewHolder{

        private ImageView catimage;
        private TextView catname;
        public Categoryviewholder(View itemView) {
            super(itemView);
            catimage = itemView.findViewById(R.id.imageview);
            catname = itemView.findViewById(R.id.cattext);

        }
    }
}
