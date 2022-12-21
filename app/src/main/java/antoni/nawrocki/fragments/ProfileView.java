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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import antoni.nawrocki.MainActivity;
import antoni.nawrocki.R;
import antoni.nawrocki.adapters.OrdersAdapter;
import antoni.nawrocki.db.Base64Converter;
import antoni.nawrocki.db.DBHelper;
import antoni.nawrocki.db.DBReaderContract;
import antoni.nawrocki.models.UserModel;


public class ProfileView extends Fragment {
    ImageButton backButton;
    ImageButton logOutButton;

    TextView nameTextView;

    RecyclerView recyclerView;

    ImageView profilePic;

    public ProfileView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton = view.findViewById(R.id.profile_back_button);
        logOutButton = view.findViewById(R.id.profile_log_out_button);

        nameTextView = view.findViewById(R.id.profile_name);

        recyclerView = view.findViewById(R.id.profile_recycler_view);

        profilePic = view.findViewById(R.id.profile_picture);


        DBHelper dbHelper = new DBHelper(getContext());
        UserModel userData = dbHelper.getUser(
                ((MainActivity) requireActivity()).login);

        if (userData != null) {
            String username = userData.getUsername();
            long userID = dbHelper.getUserID();
            nameTextView.setText(username);

            if (userID >= 0) {
                OrdersAdapter ordersAdapter = new OrdersAdapter(
                        getContext(),
                        dbHelper.getOrders(userID)
                );

                recyclerView.setAdapter(ordersAdapter);
            }
            String pic = userData.getProfilePicture();

            if (pic != null && (!Objects.equals(pic, "") || !Objects.equals(pic, "STRING_TOO_LARGE"))) {
//                Log.i("AN", "base64: " + pic);
                Base64Converter.decodeBase64StringAndSetImage(pic, profilePic);

            }
        }

        backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        logOutButton.setOnClickListener(v -> {
            logOut();

            requireActivity().onBackPressed();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    private void logOut() {
        Context context = getActivity();
        if (context == null) { return; }

        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.preference_login_key), "");
        editor.putString(getString(R.string.preference_password_key), "");
        editor.apply();
        ((MainActivity) context).invalidateOptionsMenu();
    }
}