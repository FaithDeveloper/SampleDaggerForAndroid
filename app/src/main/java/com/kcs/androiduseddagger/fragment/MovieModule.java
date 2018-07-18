package com.kcs.androiduseddagger.fragment;

import com.kcs.androiduseddagger.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;

@Module
abstract class MovieModule {
    @FragmentScope
    @Binds
    abstract MoviesFragmentContract.Presenter bindPresenter(MoviesPresenterImpl presenter);

    @FragmentScope
    @Binds
    abstract MoviesFragmentContract.View bindView(MoviesFragment fragment);
}
