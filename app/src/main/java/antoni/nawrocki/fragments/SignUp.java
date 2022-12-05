package antoni.nawrocki.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import antoni.nawrocki.R;
import antoni.nawrocki.adapters.FragmentViewPagerAdapter;

public class SignUp extends Fragment {

    public SignUp() {
        // Required empty public constructor
    }

    ViewPager2 viewPager2;


//    public static SignUp newInstance(String param1, String param2) {
//        SignUp fragment = new SignUp();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = view.findViewById(R.id.sign_up_view_pager2);

        HashMap<String, Fragment> fragmentHashMap = new HashMap<>();

        fragmentHashMap.put("Log In", new LogIn());
        fragmentHashMap.put("Register", new Register());
//        fragmentHashMap.put("Courses", new CourseList()); <- for fun

        ArrayList<Fragment> fragments = new ArrayList<>(fragmentHashMap.values());
//        fragments.add(new CourseList()); <- for fun

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(this, fragments);
        viewPager2.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    if (position < (fragmentHashMap).size()){
                        Set<String> keySet = fragmentHashMap.keySet();
                        String element = (String) keySet.toArray()[position];
                        tab.setText(element);
                    } else {
                        tab.setText("Object " + (position + 1));
                    }
                }
        ).attach();

    }
}