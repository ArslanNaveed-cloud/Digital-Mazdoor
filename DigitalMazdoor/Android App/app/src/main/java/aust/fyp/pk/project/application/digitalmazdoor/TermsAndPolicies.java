package aust.fyp.pk.project.application.digitalmazdoor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TermsAndPolicies extends Fragment {

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  =inflater.inflate(R.layout.fragment_terms_and_policies, container, false);

        return view;
    }
}