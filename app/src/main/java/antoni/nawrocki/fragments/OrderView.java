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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import antoni.nawrocki.R;
import antoni.nawrocki.adapters.OrderOptionsAdapter;
import antoni.nawrocki.db.DBHelper;
import antoni.nawrocki.db.DBReaderContract;


public class OrderView extends Fragment {
    TextView titleTextView;
    TextView descriptionTextView;
    TextView orderPriceTextView;

    ImageButton backButton;
    ImageView thumbnail;

    RecyclerView recyclerView;

    private static final String ARG_PARAM1 = Orders.COLUMN_NAME_COURSE_ID;
    private static final String ARG_PARAM2 = Orders._ID;
    private static final String ARG_PARAM3 = Orders.COLUMN_NAME_PRICE;

    private String courseID;
    private String orderID;
    private String price;

    public OrderView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseID = getArguments().getString(ARG_PARAM1);
            orderID = getArguments().getString(ARG_PARAM2);
            price = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleTextView = view.findViewById(R.id.order_view_title);
        descriptionTextView = view.findViewById(R.id.order_view_description);
        orderPriceTextView = view.findViewById(R.id.order_price);

        DBHelper dbHelper = new DBHelper(getContext());

        HashMap<String, String> courseData = dbHelper.getCourse(Long.parseLong(courseID));
        titleTextView.setText(courseData.get(CoursesOptions.COLUMN_NAME_TITLE));
        descriptionTextView.setText(courseData.get(CoursesOptions.COLUMN_NAME_DESCRIPTION));
        orderPriceTextView.setText(price);

        recyclerView = view.findViewById(R.id.order_options_recycler_view);

        ArrayList<HashMap<String, String>> options = new ArrayList<>();

        ArrayList<Long> optionIDs = dbHelper.getOptionIDs(Long.parseLong(orderID));

        for (long optionID :
                optionIDs) {
            options.add(dbHelper.getOptionData(optionID));
        }

        OrderOptionsAdapter orderOptionsAdapter = new OrderOptionsAdapter(options);

        recyclerView.setAdapter(orderOptionsAdapter);


        backButton = view.findViewById(R.id.order_view_back_button);

        backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_view, container, false);
    }
}