package space.harbour.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.MongoClient;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Movie implements Serializable {
  private static final long serialVersionUID = 3343357308340235353L;

  private String _id;
  private String title;
  private String director;
  private String actor;
  private String genre;
  private Integer year;
  private Integer length;
  private Integer runtime;
  private String released;
  private String awards;
  private String plot;
  private String poster;

  public Movie() {}

  public Movie(Map<String, Object> data) {
    _id      = (String)data.get("id");
    title    = (String)data.get("title");
    director = (String)data.get("director");
    actor    = (String)data.get("actor");
    genre    = (String)data.get("genre");
    year     = (Integer)data.get("year");
    length   = (Integer)data.get("length");
    runtime  = (Integer)data.get("runtime");
    released = (String)data.get("released");
    awards   = (String)data.get("awards");
    plot     = (String)data.get("plot");
    poster   = (String)data.get("poster");
  }

  public Movie(String title, String director, String actor, String genre, int year, int length, String plot, String poster) {
    this.title = title;
    this.director = director;
    this.actor = actor;
    this.genre = genre;
    this.year = year;
    this.length = length;
    this.plot = plot;
    this.poster = poster;
  }

  public static CodecRegistry registry() {
    PojoCodecProvider codecProvider = PojoCodecProvider.builder().automatic(true).build();
    return fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(codecProvider));
  }

  @Override
  public String toString() {
      return (_id != null ? _id + ") " : "") +
        title + " (" + year + ") [" + director + "]" + (genre == null ? "" : " - " + genre);
  }

  public Map<String, Object> toMap() {
    var m = new HashMap<String, Object>();
    if (_id != null) m.put("id", _id);
    m.put("title", title);
    m.put("director", director);
    m.put("actor", actor);
    m.put("genre", genre);
    m.put("year", year);
    m.put("length", length);
    m.put("runtime", runtime);
    m.put("released", released);
    m.put("awards", awards);
    m.put("plot", plot);
    m.put("poster", poster);
    return m;
  }

  public ObjectId getId() {
    if (_id == null) return null;
    return new ObjectId(_id);
  }

  public void setId(ObjectId id) {
    if (id != null)
      this._id = id.toString();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String s) {
    title = s;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String s) {
    director = s;
  }

  public String getActor() {
    return actor;
  }

  public void setGenre(String s) {
    genre = s;
  }

  public String getGenre() {
    return genre;
  }

  public void setActor(String s) {
    actor = s;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer i) {
    year = i;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer i) {
    length = i;
  }

  public Integer getRuntime() {
    return runtime;
  }

  public void setRuntime(Integer i) {
    runtime = i;
  }

  public String getReleased() {
      return released;
  }

  public void setReleased(String s) {
      this.released = s;
  }

  public String getAwards() {
      return awards;
  }

  public void setAwards(String s) {
      this.awards = s;
  }

  public String getPlot() {
      return plot;
  }

  public void setPlot(String plot) {
      this.plot = plot;
  }

  public String getPoster() {
      return poster;
  }

  public void setPoster(String poster) {
      this.poster = poster;
  }
}
