package com.kcs.androiduseddagger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kcs.androiduseddagger.R;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class MoviesFragment extends Fragment implements MoviesFragmentContract.View{
    @Inject MoviesFragmentContract.Presenter presenter;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    public static MoviesFragment newInstance(String category){

        MoviesFragment fragment = new MoviesFragment();

        Bundle bundle = new Bundle();
        bundle.putString("category",category);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }
}
