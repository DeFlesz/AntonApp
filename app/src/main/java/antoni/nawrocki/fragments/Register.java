package antoni.nawrocki.fragments;

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
import android.widget.Toast;

import antoni.nawrocki.R;

public class Register extends Fragment {
    EditText usernameInput;
    EditText loginInput;
    EditText passwordInput;
    EditText confirmPasswordInput;
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

        registerButton.setOnClickListener(l -> {
            Toast.makeText(
                    getContext(),
                    "Username: " + usernameInput.getText() +
                    ", Login: " + loginInput.getText() +
                    (
                        passwordInput.getText() == confirmPasswordInput.getText() ?
                           "Passwords match: " + passwordInput.getText() :
                           "Passwords don't match: [" + passwordInput.getText() +
                           ", " + confirmPasswordInput.getText() + "]"
                    ) +
                    ", Firma?:" + (isCompanyCheckbox.isChecked() ? "Tak" : "Nie")
                    ,
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}