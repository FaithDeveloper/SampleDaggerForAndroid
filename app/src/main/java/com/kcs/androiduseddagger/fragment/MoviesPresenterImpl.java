package com.kcs.androiduseddagger.fragment;

import com.kcs.androiduseddagger.data.DataSource;

import javax.inject.Inject;

public class MoviesPresenterImpl implements MoviesFragmentContract.Presenter {
    MoviesFragmentContract.View view;
    DataSource dataSource;

    @Inject
    public MoviesPresenterImpl(MoviesFragmentContract.View view, DataSource dataSource){
        this.view = view;
        this.dataSource = dataSource;
    }

}
