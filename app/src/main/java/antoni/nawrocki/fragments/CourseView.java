package antoni.nawrocki.fragments;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import antoni.nawrocki.R;
import antoni.nawrocki.adapters.OptionsAdapter;
import antoni.nawrocki.db.DBHelper;

public class CourseView extends Fragment {

    TextView title;
    TextView description;
    TextView price;
    Button orderButton;
    RecyclerView recyclerView;

    private static final String ARG_PARAM1 = Courses._ID;
    private String courseID;

    public CourseView() {
        // Required empty public constructor
    }

    public static CourseView newInstance(String param1) {
        CourseView fragment = new CourseView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.course_view_title);
        description = view.findViewById(R.id.course_view_description);
        price = view.findViewById(R.id.course_view_price);
        orderButton = view.findViewById(R.id.course_view_buy_button);
        recyclerView = view.findViewById(R.id.course_view_recycler);

        HashMap<String, Object> courseData = new DBHelper(getContext()).getCourse(Long.parseLong(courseID));

        title.setText((String) courseData.get(CoursesOptions.COLUMN_NAME_TITLE));
        description.setText((String) courseData.get(CoursesOptions.COLUMN_NAME_DESCRIPTION));
        price.setText("Cena: " + courseData.get(CoursesOptions.COLUMN_NAME_PRICE));

        OptionsAdapter optionsAdapter = new OptionsAdapter((ArrayList<HashMap<String, String>>) courseData.get("options"));
        recyclerView.setAdapter(optionsAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_view, container, false);
    }
}