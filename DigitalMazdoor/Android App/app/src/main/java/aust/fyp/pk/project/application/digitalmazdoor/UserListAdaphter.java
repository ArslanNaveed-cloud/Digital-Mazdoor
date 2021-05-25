package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserListAdaphter extends RecyclerView.Adapter<UserListAdaphter.viewholder> {
    ArrayList<UserListDataModel> userListDataModels;
    Context context;
    String buyerid="";
    int counter=0;
    public UserListAdaphter(ArrayList<UserListDataModel> userListDataModels, Context context) {
        this.userListDataModels = userListDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.showbuyerslistlayout,parent,false);

        return new UserListAdaphter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {


        if(buyerid.contains(userListDataModels.get(position).getBuyerid())){

        }else{
            SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String freelancerid = sharedPreferences.getString("id","");
            String name = sharedPreferences.getString("username","");
            counter=counter+1;
            buyerid =buyerid+ userListDataModels.get(position).getBuyerid()+" ";
            String url;
            url  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+userListDataModels.get(position).getBuyerimage();
            url = url.replace(" ","%20");
            Glide.with(context).load(url).circleCrop().
                    into(holder.senderimage);

            holder.buyeremail.setText(userListDataModels.get(position).getBuyeremail());
            holder.buyerlastmessage.setText(userListDataModels.get(position).getLastmessage());
            holder.buyername.setText(userListDataModels.get(position).getBuyername());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ChatActivity.class);
                    intent.putExtra("recievername",userListDataModels.get(position).getBuyername());
                    intent.putExtra("recieverid",userListDataModels.get(position).getBuyerid());
                    intent.putExtra("recieverimage",userListDataModels.get(position).getBuyerimage());

                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return userListDataModels.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        private ImageView senderimage;
        private TextView buyername,buyeremail,buyerlastmessage;
        public viewholder(View itemView) {
            super(itemView);
            senderimage = itemView.findViewById(R.id.buyerimage);
            buyername = itemView.findViewById(R.id.buyername);
            buyeremail = itemView.findViewById(R.id.buyeremail);
            buyerlastmessage = itemView.findViewById(R.id.lastmessage);


        }
    }

}
