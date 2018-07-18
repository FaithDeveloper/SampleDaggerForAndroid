package com.kcs.androiduseddagger.data;

import java.util.List;

public interface DataSource {
    List<String> getMovies(String category);

    List<String> getCategory();
}
