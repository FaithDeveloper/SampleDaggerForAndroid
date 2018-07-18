package com.kcs.androiduseddagger.application;

import android.app.Activity;

import com.kcs.androiduseddagger.data.DataSource;
import com.kcs.androiduseddagger.data.DataSourceImpl;
import com.kcs.androiduseddagger.main.MainComponent;
import com.kcs.androiduseddagger.main.MainActivity;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = MainComponent.class)
abstract class AppModule {
    @Singleton
    @Binds
    abstract DataSource bindDataSource(DataSourceImpl dataSource);

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindMainActivity(MainComponent.Builder builder);
}
