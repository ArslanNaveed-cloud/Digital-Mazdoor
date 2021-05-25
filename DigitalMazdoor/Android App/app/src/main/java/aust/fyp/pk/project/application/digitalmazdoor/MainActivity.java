package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private LinearLayout welcomeprompt,buttons,messsage;
    Animation aniFadeIn,aniFadeOut;
    private Button freelancer,buyer;
    private Intent intent;
    private TextView signin;
    private static boolean ispermitted=false;
    private boolean isuserloggedin;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcomeprompt = findViewById(R.id.welcomeprompt);
        buttons = findViewById(R.id.buttons);
        messsage = findViewById(R.id.message);
        freelancer = findViewById(R.id.freelancer);
        buyer = findViewById(R.id.buyer);
        signin = findViewById(R.id.signin);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        isuserloggedin =  sharedPreferences.getBoolean("isuserloggedin",false);

        aniFadeIn= AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);
        aniFadeOut= AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
        welcomeprompt.setAnimation(aniFadeIn);
        buttons.setVisibility(View.GONE);
        messsage.setVisibility(View.GONE);
            if(isuserloggedin){
                category = sharedPreferences.getString("category","");
                if(category.equals("freelancer")){
                    Intent intent = new Intent(MainActivity.this,FreelancerDashboard.class);
                    startActivity(intent);
                    finish();

                }else  if(category.equals("buyer")){
                    Intent intent = new Intent(MainActivity.this,BuyerDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
            waitforsometime();
            checklocationpermissions();
            freelancer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ispermitted){
                        checklocationpermissions();
                    }else{
                        intent = new Intent(MainActivity.this,LoginnScreen.class);
                        startActivity(intent);
                    }

                }
            });

            buyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ispermitted){
                        checklocationpermissions();
                    }else{
                        intent = new Intent(MainActivity.this,BuyerLogin.class);
                        startActivity(intent);
                    }

                }
            });
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ispermitted){
                        checklocationpermissions();
                    }else{
                        intent = new Intent(MainActivity.this,LoginnScreen.class);
                        startActivity(intent);
                    }
                }
            });


    }

    public void waitforsometime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                welcomeprompt.setAnimation(aniFadeOut);
                waitforsometime2();

            }
        }, 2000);
    }
    public void waitforsometime2(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                welcomeprompt.setVisibility(View.GONE);
                buttons.setVisibility(View.VISIBLE);
                messsage.setVisibility(View.VISIBLE);
                buttons.setAnimation(aniFadeIn);
                messsage.setAnimation(aniFadeIn);


            }
        }, 2000);
    }
    private void checklocationpermissions(){
        if (ContextCompat.checkSelfPermission(MainActivity.this ,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
               || ActivityCompat.checkSelfPermission(MainActivity.this ,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(MainActivity.this ,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this ,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please Allow All Permissions To Begin", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(MainActivity.this , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }else{

            ispermitted=true;
//            if(!isConnected(MainActivity.this)){
//                buildDialog(MainActivity.this,"We are sorry","Please Check Your Internet Connection.").show();
//            }else{
//                checkcontents();
//            }

        }
    } @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (requestCode == 101 && (grantResults[i] == PackageManager.PERMISSION_GRANTED)) {

//                if (!isConnected(MainActivity.this)) {
//                    buildDialog(MainActivity.this, "We are sorry", "Please Check Your Internet Connection.").show();
//                } else {
//                    checkcontents();
//                }
                ispermitted = true;
            } else {
                ispermitted = false;
            }
        }

    }
    public AlertDialog.Builder buildDialog(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        return builder;
    }

}