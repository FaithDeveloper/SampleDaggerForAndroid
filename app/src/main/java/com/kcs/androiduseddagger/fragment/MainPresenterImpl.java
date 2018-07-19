package com.kcs.androiduseddagger.fragment;

import com.kcs.androiduseddagger.data.DataSource;

import javax.inject.Inject;

public class MainPresenterImpl implements MainFragmentContract.Presenter {
    MainFragmentContract.View view;
    DataSource dataSource;

    @Inject
    public MainPresenterImpl(MainFragmentContract.View view, DataSource dataSource){
        this.view = view;
        this.dataSource = dataSource;
    }

}
