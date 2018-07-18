package com.kcs.androiduseddagger.main;

import com.kcs.androiduseddagger.data.DataSource;

import javax.inject.Inject;

public class MainPresenterImpl implements MainActivityContract.Presenter {
    MainActivityContract.View view;
    DataSource dataSource;

    @Inject
    public MainPresenterImpl(MainActivityContract.View view, DataSource dataSource){
        this.view =view;
        this.dataSource = dataSource;
    }

    @Override
    public void loadCategory(){
        view.setViewPager(dataSource.getCategory());
    }
}
