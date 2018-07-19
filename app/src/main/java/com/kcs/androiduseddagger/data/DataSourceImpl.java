package com.kcs.androiduseddagger.data;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by chihacker on 2017. 8. 15..
 */

public class DataSourceImpl implements DataSource {

    private static final String MAIN_FRAGMENT_MESSAGE = "Main Fragment Message";

    @Inject
    public DataSourceImpl() {
    }

    @Override
    public String getMainFragmentMessage() {
        return MAIN_FRAGMENT_MESSAGE;
    }
}