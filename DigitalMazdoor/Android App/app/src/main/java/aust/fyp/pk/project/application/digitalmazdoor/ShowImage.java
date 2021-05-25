package aust.fyp.pk.project.application.digitalmazdoor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;


public class ShowImage extends Fragment {

    View view;
    ImageView image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_image, container, false);
        String url;
        Log.d("12323", "onCreateView: hello");
        url  = Urls.DOMAIN+"/assets/messagesmedia/"+getArguments().getString("imagename");
        url = url.replace(" ","%20");
        image = view.findViewById(R.id.image);
        Glide.with(getActivity()).load(url).
                into(image);
        image.setOnTouchListener(new ImageMatrixTouchHandler(getActivity()));
        return view;
    }
}