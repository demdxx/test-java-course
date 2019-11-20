package space.harbour.hw12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import space.harbour.hw12.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private Movie[] movieList;
    private View.OnClickListener clickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleView;
        public TextView releasedView;
        public ImageView imageView;

        public MyViewHolder(LinearLayout v) {
            super(v);
            titleView = v.findViewById(R.id.title);
            releasedView = v.findViewById(R.id.released);
            imageView = v.findViewById(R.id.image);
        }

        public void setOnCLickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieAdapter(Movie[] myDataset, View.OnClickListener clickListener) {
        this.movieList = myDataset;
        this.clickListener = clickListener;
    }

    public void updateData(Movie[] data) {
        this.movieList = data;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = movieList[position];
        holder.setOnCLickListener(clickListener);
        holder.titleView.setText(movie.getTitle());
        holder.releasedView.setText(movie.getReleased());
        Picasso.get().load(movie.getPoster()).into(holder.imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.length;
    }
}