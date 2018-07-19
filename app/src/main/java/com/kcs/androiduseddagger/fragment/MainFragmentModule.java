package com.kcs.androiduseddagger.fragment;

import com.kcs.androiduseddagger.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;

@Module
abstract class MainFragmentModule {
    @FragmentScope
    @Binds
    abstract MainFragmentContract.Presenter bindPresenter(MainPresenterImpl presenter);

    @FragmentScope
    @Binds
    abstract MainFragmentContract.View bindView(MainFragment fragment);
}
