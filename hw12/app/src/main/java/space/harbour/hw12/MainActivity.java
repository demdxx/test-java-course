package space.harbour.hw12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import space.harbour.hw12.lib.JSONParser;
import space.harbour.hw12.models.Movie;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MovieAdapter(null, this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new DownloadFileFromURL().execute("https://api.myjson.com/bins/nfvfi");
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }

    class DownloadFileFromURL extends AsyncTask<String, String, Movie[]> {
        @Override
        protected Movie[] doInBackground(String... f_url) {
            JSONParser parser = new JSONParser();
            JSONArray arr = parser.getJSONFromUrl(f_url[0]);
            List<Movie> list = new LinkedList<>();
            for (int i=0; i < arr.length(); i++) {
                try {
                    JSONObject obj = (JSONObject) arr.get(i);
                    Movie mv = new Movie(
                            obj.getString("Title"),
                            Integer.valueOf(obj.getInt("Year")),
                            Integer.valueOf(obj.getInt("Runtime")),
                            obj.getString("Released"),
                            obj.getString("Plot"),
                            obj.getString("Awards"),
                            obj.getString("Poster")
                    );
                    list.add(mv);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list.toArray(new Movie[list.size()]);
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mAdapter.updateData(movies);
        }
    }
}
