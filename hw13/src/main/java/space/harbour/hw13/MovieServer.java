package space.harbour.hw13;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.eclipse.jetty.http.HttpStatus;

import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import space.harbour.models.Movie;
import space.harbour.models.SuperclassExclusionStrategy;

public class MovieServer {
  private final Gson gson = new GsonBuilder()
    // .excludeFieldsWithoutExposeAnnotation()
    .addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy())
    .create();
  // private final Gson gson = new Gson();
  private final MongoCollection<Movie> collection;
  private final FreeMarkerEngine renderer;

  public static class MyResponse {
    private String error;
    @Expose
    private Object response;

    public MyResponse(Object response) {
      this(response, null);
    }

    public MyResponse(Object response, String error) {
      this.response = response;
      this.error = error;
    }
  }

  public MovieServer(MongoCollection<Movie> collection) {
    this.collection = collection;
    this.renderer = new FreeMarkerEngine();
    Spark.get("/", this::indexPage);
    Spark.get("/api/movies", this::getMovies, gson::toJson);
    Spark.get("/api/movies/:id", this::getMovie, gson::toJson);
    Spark.post("/api/movies", this::createMovie, gson::toJson);
    Spark.put("/api/movies/:id", this::updateMovie, gson::toJson);
    Spark.delete("/api/movies/:id", this::deleteMovie, gson::toJson);
  }

  protected Object indexPage(Request request, Response response) throws Exception {
    var movies = collection.find();
    List<Movie> list = new LinkedList<>();
    for (var movie: movies) {
      list.add(movie);
    }
    Map<String, Object> moviesMap = new HashMap<>();
    moviesMap.put("title", "Movies");
    moviesMap.put("movies", list);
    return render(moviesMap, "index.ftl");
  }

  protected Object getMovies(Request request, Response response) throws Exception {
    var movies = collection.find();
    List<Movie> list = new LinkedList<>();
    for (var movie: movies) {
      list.add(movie);
    }
    return new MyResponse(list);
  }

  protected Object getMovie(Request request, Response response) throws Exception {
    var movie = collection.find(new Document("_id", new ObjectId(request.params(":id")))).first();
    if (movie == null) {
      response.status(HttpStatus.NOT_FOUND_404);
      return new MyResponse(null, "not found");
    }
    return new MyResponse(movie.toMap());
  }

  protected Object createMovie(Request request, Response response) throws Exception {
    try {
      var movie = gson.fromJson(request.body(), Movie.class);
      collection.insertOne(movie);
      response.status(HttpStatus.CREATED_201);
      return new MyResponse("success");
    } catch (Exception e) {
      return new MyResponse(null, e.toString());
    }
  }

  protected Object updateMovie(Request request, Response response) throws Exception {
    try {
      var movie = gson.fromJson(request.body(), Movie.class);
      collection.replaceOne(
        new Document("_id", new ObjectId(request.params(":id"))),
        movie);
      return new MyResponse("success");
    } catch (Exception e) {
      return new MyResponse(null, e.toString());
    }
  }

  protected Object deleteMovie(Request request, Response response) throws Exception {
    collection.deleteOne(new Document("_id", new ObjectId(request.params(":id"))));
    return new MyResponse("success");
  }

  public String render(Object values, String template) {
      return renderer.render(new ModelAndView(values, template));
  }
}
