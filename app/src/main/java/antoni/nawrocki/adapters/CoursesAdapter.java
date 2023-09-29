package antoni.nawrocki.adapters;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import antoni.nawrocki.MainActivity;
import antoni.nawrocki.R;
import antoni.nawrocki.db.Base64Converter;
import antoni.nawrocki.fragments.CourseView;

/**
 * Adapter for the Courses recycler view
 */
public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> courses;
    private Context context;

    /**
     * Constructor for the adapter
     * @param courses ArrayList of HashMaps containing courses references for db
     * @param context lil dependency injection for context
     */
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
        // TODO
        //  GET DATA FROM DB NOT FROM GIGA QUERY BEFORE!!!!!!! :)))))


        holder.title.setText(courses.get(position).get(Courses.COLUMN_NAME_TITLE));
        holder.description.setText(courses.get(position).get(Courses.COLUMN_NAME_DESCRIPTION));
        holder.price.setText(courses.get(position).get(Courses.COLUMN_NAME_PRICE) + " PLN");
        String thumbnail = courses.get(position).get(Courses.COLUMN_NAME_THUMBNAIL);
        if (thumbnail != null && !Objects.equals(thumbnail, "") && !Objects.equals(thumbnail, "STRING_TOO_LARGE")){
            Base64Converter.decodeBase64StringAndSetImage(thumbnail, holder.thumbnail);
        }

        holder.itemView.setOnClickListener(v -> {
//            Toast.makeText(v.getContext(), string, Toast.LENGTH_SHORT).show();
            String string = courses.get(position).get(Courses._ID);

            Bundle bundle = new Bundle();
            bundle.putString(Courses._ID, string);

            CourseView courseView = new CourseView();
            courseView.setArguments(bundle);

//            ((MainActivity) context).getSupportActionBar().hide();

            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right);
            fragmentTransaction
                    .replace(R.id.fragment_container, courseView)
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    /**
     * ViewHolder for Courses recycler view
     */
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
