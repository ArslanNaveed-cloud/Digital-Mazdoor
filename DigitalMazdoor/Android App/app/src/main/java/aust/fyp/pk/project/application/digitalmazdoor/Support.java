package aust.fyp.pk.project.application.digitalmazdoor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Support extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_support, container, false);
        floatingActionButton = view.findViewById(R.id.fb_1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                String number = "tel: 0992 532586";
                intent.setData(Uri.parse(number));
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}