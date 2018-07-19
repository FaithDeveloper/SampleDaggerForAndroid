package com.kcs.androiduseddagger.main;

import android.support.v4.app.Fragment;

import com.kcs.androiduseddagger.fragment.MainFragmentComponent;
import com.kcs.androiduseddagger.fragment.MainFragment;
import com.kcs.androiduseddagger.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = MainFragmentComponent.class)
abstract class MainModule {
    @ActivityScope
    @Binds
    abstract MainActivityContract.View bindView(MainActivity activity);


    @ActivityScope
    @Binds
    public abstract MainActivityContract.Presenter bindPresenter(MainPresenterImpl mainPresenter);

    @Binds
    @IntoMap
    @FragmentKey(MainFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindMoviesFragment(MainFragmentComponent.Builder builder);
}
