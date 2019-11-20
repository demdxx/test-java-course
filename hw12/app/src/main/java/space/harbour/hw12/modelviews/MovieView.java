package space.harbour.hw12.modelviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import space.harbour.hw12.models.Movie;

public class MovieView {
    public final LiveData<List<Movie>> usersList;

    public MovieView() {
        usersList = new MutableLiveData<List<Movie>>();
    }
}
