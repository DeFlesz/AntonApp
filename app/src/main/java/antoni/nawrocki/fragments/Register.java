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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antoni.nawrocki.MainActivity;
import antoni.nawrocki.R;
import antoni.nawrocki.db.DBHelper;

public class Register extends Fragment {
    EditText usernameInput;
    EditText loginInput;
    EditText passwordInput;
    EditText confirmPasswordInput;

    TextView validationText;

    CheckBox isCompanyCheckbox;
    Button registerButton;

    public Register() {
        // Required empty public constructor
    }

//    public static Register newInstance(String param1, String param2) {
//        Register fragment = new Register();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameInput = view.findViewById(R.id.register_username);
        loginInput = view.findViewById(R.id.register_login);
        passwordInput = view.findViewById(R.id.register_password1);
        confirmPasswordInput = view.findViewById(R.id.register_password2);
        isCompanyCheckbox = view.findViewById(R.id.register_is_company_checkbox);
        registerButton = view.findViewById(R.id.register_button);
        validationText = view.findViewById(R.id.register_text);

        registerButton.setOnClickListener(l -> {
            validationText.setText("");
            String password1 = passwordInput.getText().toString();
            String password2 = confirmPasswordInput.getText().toString();
            boolean passwordsMatching = password1.equals(password2);

            if (!passwordsMatching) {
                String validationString = validationText.getText().toString();
                validationText.setText(validationString.isEmpty() ?
                        getString(R.string.passwords_dont_match) :
                        validationString + ", " + getString(R.string.passwords_dont_match)
                );
            }

            String login = loginInput.getText().toString();

            Pattern pattern = Pattern.compile("\\s");
            Matcher matcher = pattern.matcher(login);
            boolean found = matcher.find();

            if (found) {
                String validationString = validationText.getText().toString();
                validationText.setText(validationString.isEmpty() ?
                        getString(R.string.white_spaces_validation) :
                        validationString + ", " + getString(R.string.white_spaces_validation)
                );
            }

            String username = usernameInput.getText().toString();

            if ((!passwordsMatching || found) || (login == "" || password1 == "" || username == ""))
            {
                return;
            }

            validationText.setText("");

            DBHelper dbHelper = new DBHelper(getContext());
            boolean success = dbHelper.registerUser(
                    usernameInput.getText().toString(),
                    login,
                    password1,
                    isCompanyCheckbox.isChecked()
            );
            
            if (success) {
                saveLogin(login, password1);
//                Toast.makeText(getContext(), "Pomyślnie zarejestrowano użytkownika", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }

//            Toast.makeText(getContext(), "Nie udało się zarejestrować użytkownika", Toast.LENGTH_SHORT).show();
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