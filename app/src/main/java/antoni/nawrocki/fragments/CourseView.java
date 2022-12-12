package antoni.nawrocki.fragments;

import static antoni.nawrocki.db.DBReaderContract.*;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import antoni.nawrocki.R;
import antoni.nawrocki.adapters.CourseOptionsAdapter;
import antoni.nawrocki.db.DBHelper;
import antoni.nawrocki.models.OrderModel;

public class CourseView extends Fragment {

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

        HashMap<String, String> courseData = new DBHelper(getContext()).getCourse(Long.parseLong(courseID));
        ArrayList<HashMap<String, String>> options = new DBHelper(getContext()).getOptions(Long.parseLong(courseID));

        title.setText(courseData.get(CoursesOptions.COLUMN_NAME_TITLE));
        description.setText(courseData.get(CoursesOptions.COLUMN_NAME_DESCRIPTION));
        price.setText(courseData.get(CoursesOptions.COLUMN_NAME_PRICE));
        double basePrice = Long.parseLong(courseData.get(CoursesOptions.COLUMN_NAME_PRICE));

        CourseOptionsAdapter courseOptionsAdapter = new CourseOptionsAdapter(options, price, basePrice);
        recyclerView.setAdapter(courseOptionsAdapter);

        backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        orderButton.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(getContext());
            long userID = dbHelper.getUserID();
            
            if (userID < 0) {
                Toast.makeText(getContext(), "Zaloguj się przed zamawianiem!", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(getContext(), "Pomyślnie dokonałeś zamówienia!", Toast.LENGTH_SHORT).show();
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
        ((AppCompatActivity)requireActivity()).getSupportActionBar().hide();
    }
}