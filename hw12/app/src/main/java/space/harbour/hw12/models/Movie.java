package space.harbour.hw12.models;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private Integer year;
    private Integer runtime;
    private String released;
    private String plot;
    private String awards;
    private String poster;

    public Movie() { }

    public Movie(String title, Integer year, Integer runtime, String released, String plot, String awards, String poster) {
        this.title = title;
        this.year = year;
        this.runtime = runtime;
        this.released = released;
        this.plot = plot;
        this.awards = awards;
        this.poster = poster;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
