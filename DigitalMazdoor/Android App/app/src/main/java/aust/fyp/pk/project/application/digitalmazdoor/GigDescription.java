package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GigDescription extends AppCompatActivity {

    private String title;
    private String category;
    private String description;
    private String price;
    private String deliverytime;
    private String profileimage;
    private String name;
    private String gigimage;
    private String gigid;
    private String buyername,buyerid;
    private boolean isfragmentshowed=false;
    private LinearLayout loader;
    FrameLayout frameLayout;
    TextView gtitle,gdescription,gdays,gprice,fcategory,freelancername;
    ImageView gimage,profilepic;
    private Button ordernow,chatnow;
    private String FID;
    private String buyerpicture;
    private String senderid,sendername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_description);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        frameLayout = findViewById(R.id.container);
        loader = findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
        gtitle = findViewById(R.id.title);
        gimage = findViewById(R.id.image);
        gdescription = findViewById(R.id.description);
        gdays = findViewById(R.id.days);
        freelancername = findViewById(R.id.freename);
        gprice = findViewById(R.id.price);
        profilepic = findViewById(R.id.profile);
        fcategory = findViewById(R.id.category);
        ordernow = findViewById(R.id.ordernow);
        chatnow = findViewById(R.id.chatnow);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            buyername = sharedPreferences.getString("username","");
            buyerid = sharedPreferences.getString("id","");
            buyerpicture = sharedPreferences.getString("profilepicture","");

        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog(GigDescription.this,"Are You Sure","Do You Want to Place Order?").show();
            }
        });

        Intent intent = getIntent();

        FID = intent.getStringExtra("fid");
        title = intent.getStringExtra("title");
        category = intent.getStringExtra("category");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");
        deliverytime = intent.getStringExtra("deliverytime");
        profileimage = intent.getStringExtra("profilepic");
        name = intent.getStringExtra("name");
        gigimage = intent.getStringExtra("coverimage");
        gigid = intent.getStringExtra("id");

        String url1,url2;
        url1  = Urls.DOMAIN+"/assets/freelancergigpictures/"+gigimage;
        url1 = url1.replace(" ","%20");

        url2  = Urls.DOMAIN+"/assets/freelancersprofilepictures/"+profileimage;
        url2 = url2.replace(" ","%20");

        Glide.with(GigDescription.this).load(url1).
                into(gimage);

        Glide.with(GigDescription.this).load(url2).centerCrop().
                into(profilepic);

        gtitle.setText(title);
        gdescription.setText(description);
        gdays.setText(":  "+deliverytime);
        freelancername.setText(name);
        gprice.setText(price);


        chatnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GigDescription.this,ChatActivity.class);
                intent.putExtra("recievername",name);
                intent.putExtra("recieverid",FID);
                intent.putExtra("recieverimage",profileimage);
                startActivity(intent);
            }
        });

    }
    public AlertDialog.Builder buildDialog(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        builder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isfragmentshowed=true;
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);

                Bundle bundle = new Bundle();
                bundle.putString("fid", FID );
                bundle.putString("freelancername",name);
                bundle.putString("profileimage", profileimage );
                bundle.putString("buyerid",buyerid);
                bundle.putString("gigid",gigid);
                bundle.putString("gigtitle",title);
                bundle.putString("buyerpicture",buyerpicture);
                bundle.putString("category",category);
                bundle.putString("buyername",buyername);

                PlaceOrder guideFragment = new PlaceOrder();
                guideFragment.setArguments(bundle);
                transaction.replace(R.id.container,guideFragment);
                transaction.commit();
                manager.popBackStack();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder;
    }
    public void waitforsometime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                if(isfragmentshowed){
                    frameLayout.setVisibility(View.GONE);
                    isfragmentshowed = false;
                }else{
                    super.onBackPressed();
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(isfragmentshowed){
            frameLayout.setVisibility(View.GONE);
            isfragmentshowed = false;
        }else{
            super.onBackPressed();
        }
    }
}