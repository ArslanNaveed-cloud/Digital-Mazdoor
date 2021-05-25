package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Context;
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

public class FreelancerMessagesAdapahter extends RecyclerView.Adapter<FreelancerMessagesAdapahter.viewholder> {
    ArrayList<MessageDataModel> messageDataModels;
    Context context;

    public FreelancerMessagesAdapahter(ArrayList<MessageDataModel> messageDataModels, Context context) {
        this.messageDataModels = messageDataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.messageslistitemlayout,parent,false);

        return new FreelancerMessagesAdapahter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String freelancerid = sharedPreferences.getString("id", "");
        if(freelancerid.equals(messageDataModels.get(position).getSenderid())){
            if(messageDataModels.get(position).getMessagetype().equals("text")){
                holder.recievrtxtmsglayout.setVisibility(View.GONE);
                holder.recieverimgmsglayout.setVisibility(View.GONE);
                holder.senderimgmsglayout.setVisibility(View.GONE);
                holder.sendertxtmsglayout.setVisibility(View.VISIBLE);
                holder.sendertextmessage.setText(messageDataModels.get(position).getMessage());
            }else{
                holder.recievrtxtmsglayout.setVisibility(View.GONE);
                holder.recieverimgmsglayout.setVisibility(View.GONE);
                holder.senderimgmsglayout.setVisibility(View.VISIBLE);
                holder.sendertxtmsglayout.setVisibility(View.GONE);
                String url;
                url  = Urls.DOMAIN+"/assets/messagesmedia/"+messageDataModels.get(position).getMessage();
                url = url.replace(" ","%20");
                Glide.with(context).load(url).
                        into(holder.senderimagemsg);
            }
        }else if(freelancerid.equals(messageDataModels.get(position).getRecieverid())){
            if(messageDataModels.get(position).getMessagetype().equals("text")){
                holder.recievrtxtmsglayout.setVisibility(View.VISIBLE);
                holder.recieverimgmsglayout.setVisibility(View.GONE);
                holder.senderimgmsglayout.setVisibility(View.GONE);
                holder.sendertxtmsglayout.setVisibility(View.GONE);
                holder.recievertextmessage.setText(messageDataModels.get(position).getMessage());
            }else{
                holder.recievrtxtmsglayout.setVisibility(View.GONE);
                holder.recieverimgmsglayout.setVisibility(View.VISIBLE);
                holder.senderimgmsglayout.setVisibility(View.GONE);
                holder.sendertxtmsglayout.setVisibility(View.GONE);
                String url;
                url  = Urls.DOMAIN+"/assets/messagesmedia/"+messageDataModels.get(position).getMessage();
                url = url.replace(" ","%20");
                Glide.with(context).load(url).
                        into(holder.senderimagemsg);
            }
        }else{
            holder.recievrtxtmsglayout.setVisibility(View.GONE);
            holder.recieverimgmsglayout.setVisibility(View.GONE);
            holder.senderimgmsglayout.setVisibility(View.GONE);
            holder.sendertxtmsglayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class viewholder extends RecyclerView.ViewHolder{

        private ImageView senderimagemsg,recieverimagemsg;
        private TextView sendertextmessage,recievertextmessage,recievername,recievername2;
        RelativeLayout sendertxtmsglayout,senderimgmsglayout,recievrtxtmsglayout,recieverimgmsglayout;
        public viewholder(View itemView) {
            super(itemView);
            sendertextmessage = itemView.findViewById(R.id.sendermessage);
            senderimagemsg = itemView.findViewById(R.id.senderimage);

            recievertextmessage = itemView.findViewById(R.id.receivedTxt);
            recieverimagemsg = itemView.findViewById(R.id.receiverimage);

            recievername = itemView.findViewById(R.id.recievername);
            recievername2 = itemView.findViewById(R.id.recievername2);

            sendertxtmsglayout = itemView.findViewById(R.id.sendertextmessage);
            senderimgmsglayout=itemView.findViewById(R.id.senderimagemessage);

            recievrtxtmsglayout = itemView.findViewById(R.id.recievertextmessage);
            recieverimgmsglayout = itemView.findViewById(R.id.recieverimagemessage);

        }
    }

}
