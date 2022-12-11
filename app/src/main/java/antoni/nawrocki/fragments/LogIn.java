package antoni.nawrocki.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antoni.nawrocki.MainActivity;
import antoni.nawrocki.R;
import antoni.nawrocki.db.DBHelper;

public class LogIn extends Fragment {
    EditText loginInput;
    EditText passwordInput;

    TextView validationText;

    Button loginButton;

    public LogIn() {
        // Required empty public constructor
    }

//    public static LogIn newInstance(String param1, String param2) {
//        LogIn fragment = new LogIn();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginInput = view.findViewById(R.id.login_login);
        passwordInput = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.login_button);
        validationText = view.findViewById(R.id.login_text);

        loginButton.setOnClickListener(l -> {
            String login = loginInput.getText().toString();
            String password = passwordInput.getText().toString();

            Pattern pattern = Pattern.compile("\\s");
            Matcher matcher = pattern.matcher(login);
            boolean found = matcher.find();

            if (found) {
                validationText.setText("Login nie może zawierać białych znaków!");
                return;
            }

            DBHelper dbHelper = new DBHelper(getContext());

            boolean success = dbHelper.loginUser(login, password);

            if (success) {
//                Toast.makeText(getContext(), "Pomyślnie zalogowano użytkownika!", Toast.LENGTH_SHORT).show();
                saveLogin(login, password);
                requireActivity().onBackPressed();
            }

//            Toast.makeText(getContext(), "Nie udało się zalogować użytkownika!", Toast.LENGTH_SHORT).show();


//            Toast.makeText(
//                    getContext(),
//                    "Login: " + loginInput.getText() + ", Password: " + passwordInput.getText(),
//                    Toast.LENGTH_SHORT
//            ).show();
        });
    }

    private void saveLogin(String login, String password) {
        Context context = getActivity();
        if (context == null) { return; }

        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.preference_login_key), login);
        editor.putString(getString(R.string.preference_password_key), password);
        editor.apply();
        ((MainActivity) context).invalidateOptionsMenu();
    }
}