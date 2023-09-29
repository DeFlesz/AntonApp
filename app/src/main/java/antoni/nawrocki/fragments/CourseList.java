package antoni.nawrocki.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import antoni.nawrocki.MainActivity;
import antoni.nawrocki.R;
import antoni.nawrocki.adapters.CoursesAdapter;
import antoni.nawrocki.db.DBHelper;

/**
 * Fragment displaying list of courses
 */
public class CourseList extends Fragment {
    RecyclerView recyclerView;
    FragmentContainerView fragmentContainerView;

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
        fragmentContainerView = view.findViewById(R.id.course_fragment_container);

        DBHelper dbHelper = new DBHelper(getContext());
        String login = ((MainActivity) requireActivity()).login;

        CoursesAdapter coursesAdapter = new CoursesAdapter(dbHelper.getCourses(), getActivity());

        if (!login.equals("")){
            ArrayList<HashMap<String, String>> data = dbHelper.getCourses(dbHelper.getUserID());

            if(data.size() < 1) {
                Log.i("TAG", "aaaa");


                if (getActivity() != null && getContext() != null) {
                    FragmentManager fragmentManager = ((MainActivity) getContext()).getSupportFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.course_fragment_container, new NoCourses())
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }
            }

            coursesAdapter = new CoursesAdapter(data, getActivity());
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

        Context context = getActivity();
        if (context == null) { return; }

        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.current_course_key), "");
        editor.apply();

        ((AppCompatActivity)requireActivity()).getSupportActionBar().show();
    }
}