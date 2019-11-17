package space.harbour.models;

import java.io.Serializable;

import com.mongodb.MongoClient;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Movie implements Serializable {
  private static final long serialVersionUID = 3343357308340235353L;

  private ObjectId _id;

  /**
   * Title of the movie
   */
  private String title;

  /**
   * Director of the movie
   */
  private String director;

  /**
   * Actor of the movie
   */
  private String actor;

  /**
   * Genre of the movie
   */
  private String genre;

  /**
   * Year of the movie
   */
  private Integer year;

  /**
   * Length of the movie
   */
  private Integer length;

  public Movie() {}

  public Movie(String title, String director, String actor, String genre, int year, int length) {
    this.title = title;
    this.director = director;
    this.actor = actor;
    this.genre = genre;
    this.year = year;
    this.length = length;
  }

  public static CodecRegistry registry() {
    PojoCodecProvider codecProvider = PojoCodecProvider.builder().automatic(true).build();
    return fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(codecProvider));
  }

  @Override
  public String toString() {
      return (_id != null ? _id.toString() + ") " : "") +
        title + " (" + year + ") [" + director + "]" + (genre == null ? "" : " - " + genre);
  }

  public ObjectId getId() {
    return _id;
  }

  public void setId(ObjectId id) {
    _id = id;
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
}
