package com.kcs.androiduseddagger.fragment;

import com.kcs.androiduseddagger.scope.FragmentScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@FragmentScope
public interface MovieFragmentComponent extends dagger.android.AndroidInjector<MoviesFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MoviesFragment> {

    }
}
