package space.harbour.hw4;

import java.util.Arrays;
import java.util.List;
import javax.json.*;

import space.harbour.hw4.Jsonable;
import space.harbour.hw4.JsonBuilder;
import space.harbour.hw4.Writer;

public class Movie implements Jsonable {
    String title;
    Director director;
    List<String> geners;
    Integer year;
    String plot;
    String awards;
    String poster;
    List<String> countries;
    List<String> languages;
    List<Writer> writers;
    List<Actor> actors;
    List<Rating> ratings;

    Movie() {
    };

    Movie(String title, Director director, List<String> geners, Integer year) {
        this.title = title;
        this.director = director;
        this.geners = geners;
        this.year = year;
    }

    @Override
    public JsonObject toJsonObject() {
        return JsonBuilder.create().
            add("Title", title).
            add("Year", year).
            add("Director", director == null ? null : director.toJsonObject()).
            add("Plot", plot).
            add("Awards", awards).
            add("Poster", poster).
            add("Countries", countries).
            add("Languages", languages).
            addJObjectArray("Writers", writers).
            addJObjectArray("Actors", actors).
            addJObjectArray("Ratings", ratings).
            build();
    }

    @Override
    public void fromJsonObject(JsonObject obj) {
        this.title = obj.getString("Title");
        this.director = new Director(obj.getJsonObject("Director"));
        this.geners = JsonBuilder.toStringArray(obj.getJsonArray("Genres"));
        this.year = obj.getInt("Year");
        this.plot = obj.getString("Plot");
        this.awards = obj.getString("Awards");
        this.poster = obj.getString("Poster");
        this.countries = JsonBuilder.toStringArray(obj.getJsonArray("Countries"));
        this.languages = JsonBuilder.toStringArray(obj.getJsonArray("Languages"));
        this.writers = JsonBuilder.toListArray(obj.getJsonArray("Writers"), Writer.class);
        this.actors = JsonBuilder.toListArray(obj.getJsonArray("Actors"), Actor.class);
        this.ratings = JsonBuilder.toListArray(obj.getJsonArray("Ratings"), Rating.class);
    }

    @Override
    public String toString() {
        return title + " (" + year + ") [" + director + "] - " + (geners == null ? "" : geners.toString());
    }

    public static void main(String[] args) {
        Movie movie = new Movie(
            "joker",
            new Director("no idea"),
            Arrays.asList("thriller"),
            2019
        );
        movie.writeJsonToFile("joker.json");
        Movie movie2 = new Movie();
        movie2.readFromJsonFile("BladeRunner.json");
        System.out.println(movie2.readFromJsonFile("BladeRunner.json"));
    }
}
