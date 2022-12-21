package antoni.nawrocki.fragments;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import antoni.nawrocki.R;
import antoni.nawrocki.adapters.CourseOptionsAdapter;
import antoni.nawrocki.db.Base64Converter;
import antoni.nawrocki.db.DBHelper;
import antoni.nawrocki.models.CourseOption;
import antoni.nawrocki.models.OrderModel;

public class CourseView extends Fragment {

    ImageView thumbnail;
    TextView title;
    TextView description;
    TextView price;
    Button orderButton;
    ImageButton backButton;
    RecyclerView recyclerView;

    private static final String ARG_PARAM1 = Courses._ID;
    private String courseID;

    public CourseView() {
        // Required empty public constructor
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
        backButton = view.findViewById(R.id.course_view_back_button);
        recyclerView = view.findViewById(R.id.course_view_recycler);
        thumbnail = view.findViewById(R.id.course_view_thumbnail);

        HashMap<String, String> courseData = new DBHelper(getContext()).getCourse(Long.parseLong(courseID));
        ArrayList<CourseOption> options = new DBHelper(getContext()).getOptions(Long.parseLong(courseID));

        String imageData = courseData.get(Courses.COLUMN_NAME_THUMBNAIL);

        if (imageData != null && (!Objects.equals(thumbnail, "") || !Objects.equals(thumbnail, "STRING_TOO_LARGE"))) {
            Base64Converter.decodeBase64StringAndSetImage(imageData, thumbnail);
        }

        title.setText(courseData.get(Courses.COLUMN_NAME_TITLE));
        description.setText(courseData.get(Courses.COLUMN_NAME_DESCRIPTION));
        price.setText(courseData.get(Courses.COLUMN_NAME_PRICE));
        double basePrice = Long.parseLong(courseData.get(Courses.COLUMN_NAME_PRICE));

        CourseOptionsAdapter courseOptionsAdapter = new CourseOptionsAdapter(options, price, basePrice);
        recyclerView.setAdapter(courseOptionsAdapter);

        backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        orderButton.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(getContext());
            long userID = dbHelper.getUserID();
            
            if (userID < 0) {
                Toast.makeText(getContext(), getString(R.string.log_in_toast), Toast.LENGTH_SHORT).show();
                return;
            }
            
            double finalPrice = courseOptionsAdapter.getFinalPrice();
            ArrayList<String> selectedOptionIDs = courseOptionsAdapter.getSelectedOptionIDs();

            OrderModel newOrder = new OrderModel(
                    1,
                    new Date(),
                    finalPrice
            );


            long[] longArray = new long[selectedOptionIDs.size()];

            for (int i = 0; i < selectedOptionIDs.size(); i++) {
                longArray[i] = Long.parseLong(selectedOptionIDs.get(i));
            }

            dbHelper.createOrder(
                    newOrder,
                    userID,
                    Long.parseLong(courseID),
                    longArray
            );

//            Toast.makeText(getContext(), "Pomyślnie dokonałeś zamówienia!", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();

//            Toast.makeText(getContext(), "ID kursu:" + courseID + ", ID opcji: " + selectedOptionIDs.toString() + ", Cena:" + finalPrice, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_view, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getActivity();
        if (context == null) { return; }

        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("current_course", courseID);
        editor.apply();

//        ((AppCompatActivity)requireActivity()).getSupportActionBar().hide();
    }
}