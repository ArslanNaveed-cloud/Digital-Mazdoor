package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Context;
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

import java.util.ArrayList;

public class subcategoryadaphter extends RecyclerView.Adapter<subcategoryadaphter.viewholder> implements Filterable {

    private ArrayList<subcategorydatamodel> subcatnames;
    private ArrayList<subcategorydatamodel> subcatnamesFull;

    public subcategoryadaphter(ArrayList<subcategorydatamodel> subcatnames) {
        this.subcatnames = subcatnames;
        this.subcatnamesFull = new ArrayList<>(subcatnames);
    }

    private Context context;

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.subcategorylist,parent,false);

        return new subcategoryadaphter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
        holder.subcatname.setText(subcatnames.get(position).getSubcatname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GigsList.class);
                intent.putExtra("catname",subcatnames.get(position).getSubcatname());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subcatnames.size();
    }

    @Override
    public Filter getFilter() {
        return myfilter;
    }
    private Filter myfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<subcategorydatamodel> filterlist = new ArrayList<>();

            if(constraint == null || constraint.length() == 0||constraint.toString().isEmpty()){
                filterlist.addAll(subcatnamesFull);
            }else{
                String FilterPattern = constraint.toString().toLowerCase().trim();
                for(subcategorydatamodel item:subcatnamesFull){
                    if(item.getSubcatname().toLowerCase().contains(FilterPattern)){
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
            subcatnames.clear();
            subcatnames.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public class viewholder extends RecyclerView.ViewHolder{

        private TextView subcatname;
        public viewholder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            subcatname = itemView.findViewById(R.id.catname);

        }
    }
}
