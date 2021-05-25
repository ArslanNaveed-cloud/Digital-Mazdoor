package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class GigsAdapter extends RecyclerView.Adapter<GigsAdapter.viewholder> implements Filterable {

    private ArrayList<GigsItemDataModel> dataModel;
    private ArrayList<GigsItemDataModel> datamodelFull;

    private Context context;
    public GigsAdapter(ArrayList<GigsItemDataModel> dataModel) {
        this.dataModel = dataModel;
        datamodelFull  = new ArrayList<>(dataModel);
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.gigitemlayout,parent,false);

        return new GigsAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
        String url1;
        String url2;
        url1  = Urls.DOMAIN+"/assets/freelancergigpictures/"+dataModel.get(position).getGigimage();
        url1 = url1.replace(" ","%20");


        Glide.with(context).load(url1).
                into(holder.gigimage);

        url2  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+dataModel.get(position).getProfileimage();
        url2 = url2.replace(" ","%20");

        Glide.with(context).load(url2).circleCrop().
                into(holder.profilepic);

        holder.gigtitle.setText(dataModel.get(position).getTitle());

        holder.category.setText(dataModel.get(position).getCategory());
        holder.price.setText("Rs: "+dataModel.get(position).getPrice());
        holder.name.setText(dataModel.get(position).getName());
        holder.delierytime.setText("Deliver In "+dataModel.get(position).getDeliverytime()+" Days");
        holder.city.setText(dataModel.get(position).getCity());
        holder.rating.setText(dataModel.get(position).getRating());
        holder.totalreviews.setText("( "+dataModel.get(position).getTotalreviews()+" )");

        holder.clickholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GigDescription.class);
                intent.putExtra("fid",dataModel.get(position).getFid());

                intent.putExtra("id",dataModel.get(position).getGigid());
                intent.putExtra("coverimage",dataModel.get(position).getGigimage());
                intent.putExtra("profilepic",dataModel.get(position).getProfileimage());
                    intent.putExtra("name",dataModel.get(position).getName());
                    intent.putExtra("category",dataModel.get(position).getCategory());
                    intent.putExtra("price",dataModel.get(position).getPrice());
                    intent.putExtra("title",dataModel.get(position).getTitle());
                intent.putExtra("description",dataModel.get(position).getDescription());
                intent.putExtra("deliverytime",dataModel.get(position).getDeliverytime());

                context.startActivity(intent);

            }
        });
        holder.profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FreelancerProfileDetails.class);
                intent.putExtra("freelancerid",dataModel.get(position).getFid());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }

    @Override
    public Filter getFilter() {
        return myfilter;
    }
    private Filter myfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<GigsItemDataModel> filterlist = new ArrayList<>();

            if(constraint == null || constraint.length() == 0||constraint.toString().isEmpty()){
                filterlist.addAll(datamodelFull);
            }else{
                String FilterPattern = constraint.toString().toLowerCase().trim();
                for(GigsItemDataModel item:datamodelFull){
                    if(item.getTitle().toLowerCase().contains(FilterPattern)){
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
            dataModel.clear();
            dataModel.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public class viewholder extends RecyclerView.ViewHolder{

        private ImageView profilepic,gigimage;
        private TextView gigtitle,category,price,name,delierytime,city,rating,totalreviews;
        LinearLayout chatbtn,clickholder;
        public viewholder(View itemView) {
            super(itemView);
            context = itemView.getContext();
           profilepic = itemView.findViewById(R.id.profilepic);
           gigimage = itemView.findViewById(R.id.gigimage);
           gigtitle = itemView.findViewById(R.id.gigtitle);
           category = itemView.findViewById(R.id.gigcategory);
           price = itemView.findViewById(R.id.gigprice);
           name = itemView.findViewById(R.id.name);
           delierytime = itemView.findViewById(R.id.gigdeliverytime);
           city = itemView.findViewById(R.id.city);
            rating = itemView.findViewById(R.id.rating);
            totalreviews = itemView.findViewById(R.id.totalreviews);
            clickholder = itemView.findViewById(R.id.clickholder);

        }
    }
}
