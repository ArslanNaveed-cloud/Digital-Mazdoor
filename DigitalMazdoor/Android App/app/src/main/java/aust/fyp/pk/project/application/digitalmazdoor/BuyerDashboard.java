package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

public class BuyerDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView header_username,header_email,header_rating;
    private ImageView header_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        header_image = headerview.findViewById(R.id.imageview);
        header_username = headerview.findViewById(R.id.username);
        header_email = headerview.findViewById(R.id.useremail);
        header_rating = headerview.findViewById(R.id.rating);
        String profilepic = sharedPreferences.getString("profilepicture","");

        Glide.with(BuyerDashboard.this).load(Urls.DOMAIN+"/assets/freelancersprofilepictures//"+profilepic).
                circleCrop().
                into(header_image);

        String username = sharedPreferences.getString("username","");
        String email = sharedPreferences.getString("email","");
        String rating = sharedPreferences.getString("rating","");
        header_rating.setText(rating);

        header_username.setText(username);
        header_email.setText(email);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_requests:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostedRequests()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BuyerProfile()).commit();
                break;
            case R.id.nav_ordersrequests:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BuyerOrderRequest()).commit();
                break;
            case R.id.nav_support:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Support()).commit();
                break;
            case R.id.nav_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BuyerOrder()).commit();
                break;
            case R.id.nav_messages:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BuyerMessages()).commit();
                break;
            case R.id.nav_terms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TermsAndPolicies()).commit();
                break;
            case R.id.nav_logout:
                buildDialog(BuyerDashboard.this,"Are Your Sure","Your Will be Logged Out Of System").show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public AlertDialog.Builder buildDialog(Context c, String header, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(header);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isuserloggedin",false);
                editor.putString("category","");
                editor.putString("userarrylist","");

                editor.apply();
                Intent intent = new Intent(BuyerDashboard.this,MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder;
    }
}