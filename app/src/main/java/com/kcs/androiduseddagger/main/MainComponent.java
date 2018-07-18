package com.kcs.androiduseddagger.main;

import com.kcs.androiduseddagger.main.MainActivity;
import com.kcs.androiduseddagger.main.MainModule;
import com.kcs.androiduseddagger.scope.ActivityScope;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ActivityScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent extends AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity>{

    }
}
