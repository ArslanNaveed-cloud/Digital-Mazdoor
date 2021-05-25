package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyGigsAdaphter extends RecyclerView.Adapter<MyGigsAdaphter.viewholder> {

    private ArrayList<GigDataModel> dataModels;
    private Context context;

    public MyGigsAdaphter(ArrayList<GigDataModel> dataModels, Context context) {
        this.dataModels = dataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mygigslayout,parent,false);

        return new MyGigsAdaphter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        String url;
        url  = Urls.DOMAIN+"/assets/freelancergigpictures/"+dataModels.get(position).getGigimage();
        url = url.replace(" ","%20");
        Glide.with(context).load(url).
                into(holder.gigimage);

        holder.gigtitle.setText(dataModels.get(position).getGigtitle());
        holder.gigcategory.setText(dataModels.get(position).getGigcategory());
        holder.gigprice.setText("Rs: "+dataModels.get(position).getGigprice());
        holder.gigdeliverytime.setText("Delivery In: "+dataModels.get(position).getGigdeliverytime()+" days");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MyGigDescription.class);
                intent.putExtra("id",dataModels.get(position).getGigid());
                intent.putExtra("title",dataModels.get(position).getGigtitle());
                intent.putExtra("price",dataModels.get(position).getGigprice());
                intent.putExtra("category",dataModels.get(position).getGigcategory());
                intent.putExtra("days",dataModels.get(position).getGigdeliverytime());
                intent.putExtra("image",dataModels.get(position).getGigimage());
                intent.putExtra("description",dataModels.get(position).getGigdescription());


                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        private ImageView gigimage;
        private TextView gigtitle,gigcategory,gigprice,gigdeliverytime;
        public viewholder(View itemView) {
            super(itemView);
            gigimage = itemView.findViewById(R.id.gigimage);
            gigtitle = itemView.findViewById(R.id.gigtitle);
            gigcategory = itemView.findViewById(R.id.gigcategory);
            gigprice = itemView.findViewById(R.id.gigprice);
            gigdeliverytime = itemView.findViewById(R.id.gigdeliverytime);
        }
    }
}
