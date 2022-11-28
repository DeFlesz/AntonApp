package antoni.nawrocki.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import antoni.nawrocki.R;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView price;
        ImageView thumbnail;

        public ViewHolder(@NonNull View view) {
            super(view);

            title = view.findViewById(R.id.course_title);
            description = view.findViewById(R.id.course_description);
            price = view.findViewById(R.id.course_price);
            thumbnail = view.findViewById(R.id.course_thumbnail);
        }
    }
}
