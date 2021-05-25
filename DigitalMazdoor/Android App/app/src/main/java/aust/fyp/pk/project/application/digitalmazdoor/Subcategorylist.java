package aust.fyp.pk.project.application.digitalmazdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class Subcategorylist extends AppCompatActivity {

    String[] graphic_subcategory = {"Logo Making","Flyer Designing","Banner Designing","UI & UX Designing","Business Card Designing",""};
    String[] development_subcategory = {"Mobile App Development","Web App Development","Wordpress Websites","Flutter App Development","React Native App Development"};
    String[] DataEntry_subcategory = {"Plain Data Entry","Word Processor or Typist","Cleaning of Data","Online Form Filling","Online Survey Job","Captcha Entry Jobs","Copy & Paste Jobs"};
    String[] ContentWriting_subcategory = {"Web content ","Blogging","Cleaning of Data","Online Form Filling","Online Survey Job","Captcha Entry Jobs","Copy & Paste Jobs"};
    RecyclerView recyclerView;
    subcategoryadaphter subcategoryadaphter;
    private ArrayList<subcategorydatamodel> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arrayList = new ArrayList<>();
        Intent intent = getIntent();
        String catname = intent.getStringExtra("catname");
        recyclerView = findViewById(R.id.subcategorylist);
        // laborlist.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(Subcategorylist.this));
         checkcatname(catname);
    }

    private void checkcatname(String catname) {
        switch (catname){
            case "Graphic Designing":
                for(int i=0;i<graphic_subcategory.length;i++){
                    String subcatname = graphic_subcategory[i];
                    subcategorydatamodel subcategorydatamodel = new subcategorydatamodel(subcatname);
                    arrayList.add(subcategorydatamodel);
                }
                subcategoryadaphter = new subcategoryadaphter(arrayList);
                recyclerView.setAdapter(subcategoryadaphter);

                break;
            case "App Development":
                for(int i=0;i<development_subcategory.length;i++){
                    String subcatname = development_subcategory[i];
                    subcategorydatamodel subcategorydatamodel = new subcategorydatamodel(subcatname);
                    arrayList.add(subcategorydatamodel);
                }
                subcategoryadaphter = new subcategoryadaphter(arrayList);
                recyclerView.setAdapter(subcategoryadaphter);

                break;
            case "Data Entry":
                for(int i=0;i<DataEntry_subcategory.length;i++){
                    String subcatname = DataEntry_subcategory[i];
                    subcategorydatamodel subcategorydatamodel = new subcategorydatamodel(subcatname);
                    arrayList.add(subcategorydatamodel);
                }
                subcategoryadaphter = new subcategoryadaphter(arrayList);
                break;
            case "Content Writing":
                for(int i=0;i<ContentWriting_subcategory.length;i++){
                    String subcatname = ContentWriting_subcategory[i];
                    subcategorydatamodel subcategorydatamodel = new subcategorydatamodel(subcatname);
                    arrayList.add(subcategorydatamodel);
                }
                subcategoryadaphter = new subcategoryadaphter(arrayList);
                break;
            default:
                Toast.makeText(this, "Category Not Added Yet..", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboardmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                break;

            case R.id.nav_search:
                Toast.makeText(Subcategorylist.this, "Search Labor", Toast.LENGTH_SHORT).show();
                final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        if(subcategoryadaphter == null){
                            Toast.makeText(Subcategorylist.this, "Could not find any data", Toast.LENGTH_SHORT).show();
                        }else {
                            subcategoryadaphter.getFilter().filter(newText);
                        }
                        return true;


                    }
                });
                return true;

        }


        return true;
    }
}