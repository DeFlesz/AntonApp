package antoni.nawrocki.adapters;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import antoni.nawrocki.MainActivity;
import antoni.nawrocki.R;
import antoni.nawrocki.db.DBReaderContract;
import antoni.nawrocki.fragments.CourseList;
import antoni.nawrocki.fragments.CourseView;
import antoni.nawrocki.models.CourseModel;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> courses;
    private Context context;

    public CoursesAdapter(ArrayList<HashMap<String, String>> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(courses.get(position).get(Courses.COLUMN_NAME_TITLE));
        holder.description.setText(courses.get(position).get(Courses.COLUMN_NAME_DESCRIPTION));
        holder.price.setText("Cena: " + courses.get(position).get(Courses.COLUMN_NAME_PRICE));

        holder.itemView.setOnClickListener(v -> {
//            Toast.makeText(v.getContext(), string, Toast.LENGTH_SHORT).show();
            String string = courses.get(position).get(Courses._ID);

            Bundle bundle = new Bundle();
            bundle.putString(Courses._ID, string);

            CourseView courseView = new CourseView();
            courseView.setArguments(bundle);

            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, courseView)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {

        return courses.size();
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
