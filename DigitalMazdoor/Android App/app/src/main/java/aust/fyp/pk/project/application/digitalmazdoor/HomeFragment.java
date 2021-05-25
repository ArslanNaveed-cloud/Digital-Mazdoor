package aust.fyp.pk.project.application.digitalmazdoor;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    String[] cat_names = {"Graphic Designing","App Development","Data Entry","Content Writing","Software Development","Digital Marketing","Music And Video","Programming And Tech"," SEO ","More"};
    int[] cat_icons = {R.drawable.design,R.drawable.app_development,R.drawable.data_entry,R.drawable.content_writing,R.drawable.software_development,R.drawable.digital_marketing,R.drawable.music_video,R.drawable.programming,R.drawable.android,R.drawable.more};
    private SliderAdaphter adapter;
    SliderItem sliderItem ;
    ArrayList<SliderItem> sliderItems;
    SliderView sliderView;
    GridLayoutManager gridLayoutManager;
    private ArrayList<categoryitemdatamodel> arraylist;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdaphter;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        arraylist = new ArrayList<>();
        sliderItems = new ArrayList<>();
        sliderView = view.findViewById(R.id.imageSlider);
        recyclerView = view.findViewById(R.id.categorylist);

        for(int i = 0;i<cat_names.length;i++){
            String categoryname = cat_names[i];
            int categoryimages = cat_icons[i];
            categoryitemdatamodel datamodel = new categoryitemdatamodel(categoryname,categoryimages);
            arraylist.add(datamodel);
        }


        HomeFragment homeFragment = this;
        categoryAdaphter = new CategoryAdapter(homeFragment,arraylist);
        gridLayoutManager = new GridLayoutManager(getActivity(),3, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        recyclerView.setAdapter(categoryAdaphter);



        for(int i = 0;i<3;i++){
            sliderItem = new SliderItem("Image Description","https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
            sliderItems.add(sliderItem);
        }
        adapter = new SliderAdaphter(getActivity(),sliderItems);

        sliderView.setSliderAdapter(adapter);

        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorEnabled(false);
//           sliderView.setIndicatorSelectedColor(Color.WHITE);
//           sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds
        sliderView.startAutoCycle();
        setHasOptionsMenu(true);

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboardmenu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_search:
                Toast.makeText(getActivity(), "Search Category", Toast.LENGTH_SHORT).show();
                final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(adapter == null){
                            Toast.makeText(getActivity(), "Could not find any data", Toast.LENGTH_SHORT).show();
                        }else {
                            categoryAdaphter.getFilter().filter(query);
                            searchView.clearFocus();
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
                return true;

        }

        return super.onOptionsItemSelected(item); // important line
    }
}