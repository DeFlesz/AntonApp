package antoni.nawrocki.fragments;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import antoni.nawrocki.MainActivity;
import antoni.nawrocki.R;
import antoni.nawrocki.adapters.OrderOptionsAdapter;
import antoni.nawrocki.db.DBHelper;
import antoni.nawrocki.db.DBReaderContract;


public class OrderView extends Fragment {
    TextView titleTextView;
    TextView descriptionTextView;
    TextView orderPriceTextView;

    Button shareSMSButton;
    Button shareEmailButton;

    ImageButton backButton;
    ImageView thumbnail;

    RecyclerView recyclerView;

    private static final String ARG_PARAM1 = Orders.COLUMN_NAME_COURSE_ID;
    private static final String ARG_PARAM2 = Orders._ID;
    private static final String ARG_PARAM3 = Orders.COLUMN_NAME_PRICE;

    private String courseID;
    private String orderID;
    private String price;

    ArrayList<HashMap<String, String>> options;

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

        options = new ArrayList<>();

        ArrayList<Long> optionIDs = dbHelper.getOptionIDs(Long.parseLong(orderID));

        for (long optionID :
                optionIDs) {
            options.add(dbHelper.getOptionData(optionID));
        }

        OrderOptionsAdapter orderOptionsAdapter = new OrderOptionsAdapter(options);

        recyclerView.setAdapter(orderOptionsAdapter);


        backButton = view.findViewById(R.id.order_view_back_button);
        shareSMSButton = view.findViewById(R.id.order_share_sms_button);
        shareEmailButton = view.findViewById(R.id.order_share_email_button);

        backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        shareSMSButton.setOnClickListener(v -> {
            if (
                    !checkPermissions(Manifest.permission.READ_SMS, 1)
            ) {
                return;
            }

            TelephonyManager tpm = (TelephonyManager) ((MainActivity) requireContext()).getSystemService(Context.TELEPHONY_SERVICE);
            String number = tpm.getLine1Number();

            String option = "";

            for (HashMap<String, String> map:
                 options) {
                option += "\n- " + map.get(CoursesOptions.COLUMN_NAME_TITLE);
            }

            String text = ""
                    + titleTextView.getText().toString()
                    + "\n\n"
                    + descriptionTextView.getText().toString()
                    + (option != "" ? "\n\n" + getString(R.string.choosen_options) : "")
                    + option
                    + "\n"
                    + "\n"+ getString(R.string.final_price) +" " + price;
            Uri uri = Uri.parse("smsto:" + number);
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", text);
            try {
                startActivity(intent);
//            addMessage(destinationAdress, text);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "SMS failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkPermissions(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{permission},
                    requestCode);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            return false;
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_view, container, false);
    }
}