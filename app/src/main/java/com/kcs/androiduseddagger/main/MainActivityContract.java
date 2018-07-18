package com.kcs.androiduseddagger.main;

import java.util.List;

public interface MainActivityContract {
    interface View{
        void setViewPager(List<String> categories);
    }

    interface Presenter{
        void loadCategory();
    }
}
