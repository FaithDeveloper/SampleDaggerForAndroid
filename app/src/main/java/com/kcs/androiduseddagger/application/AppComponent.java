package com.kcs.androiduseddagger.application;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class
})
public interface AppComponent {
    void inject(DaggerSampleApp daggerSampleApp);
}
