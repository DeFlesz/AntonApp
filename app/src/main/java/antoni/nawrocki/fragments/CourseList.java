package antoni.nawrocki.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import antoni.nawrocki.MainActivity;
import antoni.nawrocki.R;
import antoni.nawrocki.adapters.CoursesAdapter;
import antoni.nawrocki.db.DBHelper;


public class CourseList extends Fragment {
    RecyclerView recyclerView;

    public CourseList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.course_recycler);

        DBHelper dbHelper = new DBHelper(getContext());
        String login = ((MainActivity) requireActivity()).login;

        CoursesAdapter coursesAdapter = new CoursesAdapter(dbHelper.getCourses(), getActivity());

        if (!login.equals("")){
            ArrayList<HashMap<String, String>> data = dbHelper.getCourses(dbHelper.getUserID());

            if (data != null) {
                coursesAdapter = new CoursesAdapter(data, getActivity());
            }
            else {
                coursesAdapter = null;
            }
        }

        recyclerView.setAdapter(coursesAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        String login = ((MainActivity) requireActivity()).login;
        if (login.equals("")){
            CoursesAdapter coursesAdapter = new CoursesAdapter(new DBHelper(getContext()).getCourses(), getActivity());
            recyclerView.setAdapter(coursesAdapter);
        }

        ((AppCompatActivity)requireActivity()).getSupportActionBar().show();
    }
}