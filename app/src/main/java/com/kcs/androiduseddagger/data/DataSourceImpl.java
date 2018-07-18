package com.kcs.androiduseddagger.data;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by chihacker on 2017. 8. 15..
 */

public class DataSourceImpl implements DataSource{

    private static final String[] CATEGORIES = {"SF","DRAMA"};
    private static final String[] DRAMA = {"Table 19","Fifty Shades Darker","Dunkirk","The Founder"};
    private static final String[] SF = {"Power Rangers","Wonder Woman","Spider-Man: Homecoming"
            ,"Transformers: The Last Knight","The Dark Tower"};

    @Inject
    public DataSourceImpl(){}

    @Override
    public List<String> getMovies(String category){
        if(category.equals("SF"))   return Arrays.asList(SF);
        else return Arrays.asList(DRAMA);
    }

    @Override
    public List<String> getCategory(){
        return Arrays.asList(CATEGORIES);
    }
}