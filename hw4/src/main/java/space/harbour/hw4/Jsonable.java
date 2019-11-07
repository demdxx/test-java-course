package space.harbour.hw4;

import javax.json.*;
import java.io.FileOutputStream;
import java.io.FileReader;

public interface Jsonable {
    JsonObject toJsonObject();
    void fromJsonObject(JsonObject obj);

    default boolean writeJsonToFile(String filename) {
        JsonObject movie = toJsonObject();
        try{
            FileOutputStream fo = new FileOutputStream(filename);
            fo.write(movie.toString().getBytes());
            fo.close();
            System.out.println("Success");
        } catch (Exception e){
            return false;
        }
        return true;
    }

    default Jsonable readFromJsonFile(String filename) {
        try {
            FileReader fr = new FileReader(filename);
            JsonReader jsonReader = Json.createReader(fr);
            JsonObject jsonObject = jsonReader.readObject();
            fromJsonObject(jsonObject);
        }
        catch (Exception e){
            return null;
        }
        return this;
    }
}
