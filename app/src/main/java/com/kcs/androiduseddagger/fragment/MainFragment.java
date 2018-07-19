package com.kcs.androiduseddagger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kcs.androiduseddagger.R;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class MainFragment extends Fragment implements MainFragmentContract.View{

    @Inject MainFragmentContract.Presenter presenter;
    View layoutView;

    public static MainFragment newInstance(String category){

        MainFragment fragment = new MainFragment();

        Bundle bundle = new Bundle();
        bundle.putString("name",category);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutView =  inflater.inflate(R.layout.fragment_main,container,false);
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String name = getArguments().getString("name");
        ((TextView)layoutView.findViewById(R.id.txt_fragment_name)).setText(name);
    }
}
