package antoni.nawrocki;

import static antoni.nawrocki.db.DBReaderContract.Courses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import antoni.nawrocki.db.DBHelper;
import antoni.nawrocki.fragments.CourseView;
import antoni.nawrocki.fragments.Credits;
import antoni.nawrocki.fragments.ProfileView;
import antoni.nawrocki.fragments.SignUp;
import antoni.nawrocki.models.CourseModel;
import antoni.nawrocki.models.CourseOption;
import antoni.nawrocki.models.UserModel;

/**
 * Main activity, base of the application
 *
 * @author Antoni Nawrocki
 */
public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    FragmentContainerView fragmentContainerView;

    public String login;
    public String password;

    /**
     * Overridden method, called on creation of the object
     * @param savedInstanceState State from the saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        fragmentContainerView = findViewById(R.id.fragment_container);

//        FragmentManager fm = getSupportFragmentManager();
//        fm.beginTransaction()
//                .add(R.id.fragment_container, CourseList)
        loadPrefs();

        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String currentCourse = sharedPreferences.getString(getString(R.string.current_course_key), "");

        if (currentCourse != "") {
            Bundle bundle = new Bundle();
            bundle.putString(Courses._ID, currentCourse);

            CourseView courseView = new CourseView();
            courseView.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, courseView)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit();
        }
//        Toast.makeText(this, login + " " + password, Toast.LENGTH_SHORT).show();

//        getSupportActionBar().setHomeButtonEnabled(true);

        setUpDB();
    }

    /**
     * Overridden method, called when an options item is selected
     * @param item Menu item selected
     * @return Returns whether an item was selected
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()) {
            case R.id.menu_signup:
                getSupportFragmentManager().popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new SignUp())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
//            case R.id.menu_shopcart:
//                Toast.makeText(this, "Shopping Cart", Toast.LENGTH_SHORT).show();
//                break;

            case R.id.menu_profile:
                getSupportFragmentManager().popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new ProfileView())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;

            case R.id.about:
                getSupportFragmentManager().popBackStack();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new Credits())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Overridden method, called when the menu is created
     * @param menu Menu class object
     * @return Always returns true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Overridden method, called when the options menu is prepared
     * @param menu Menu class object
     * @return Returns whether the menu was created
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        loadPrefs();

        if (login != "" || password != "") {
            menu.clear();
            getMenuInflater().inflate(R.menu.logged_in_menu, menu);
        } else {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Overridden method, called when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * Overridden method, called when the instance is restored
     * @param savedInstanceState State saved
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        loadPrefs();
//        Toast.makeText(this, login + " " + password, Toast.LENGTH_SHORT).show();

        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Loads preferences into memory
     */
    public void loadPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        login = sharedPreferences.getString(getString(R.string.preference_login_key), "");
        password = sharedPreferences.getString(getString(R.string.preference_password_key), "");
    }
    //    @Override
//    protected void onDestroy() {
//        if (dbHelper != null) {
//            dbHelper.close();
//        }
//        super.onDestroy();
//    }

    /**
     * Sets up the database
     */
    public void setUpDB() {
        if (dbHelper.getCourses().size() != 0) {
            return;
        }

        dbHelper.createUser(
                new UserModel(
                        "Anton Nawarocki",
                        "admin",
                        "1234",
                        true,
                        false,
                        ""
                )
        );
        dbHelper.createUser(
                new UserModel(
                        "Tacos z Sosem",
                        "tacos",
                        "1234",
                        false,
                        false,
                        ""
                )
        );
        dbHelper.createUser(
                new UserModel(
                        "What's the dog doin'?",
                        "dog",
                        "1234",
                        false,
                        false,
                        getString(R.string.jokes_img)
                )
        );

//        getResources().dr
//        drawable.get

        dbHelper.createCourse(
                new CourseModel(
                        "Jak opowiedzieć żart",
                        "W tym kursie zaprezentuje jak opowiadać żarty",
                        getString(R.string.jokes_img),
                        1,
                        40
                ),
                new CourseOption[]{
                        new CourseOption(
                                "Anton-żarty dodatek",
                                "Anton żarty ostatnio cieszą się większą popularnością",
                                20
                        ),
                        new CourseOption(
                                "inglisz",
                                "żarty o najpopularniejszym języku na świecie",
                                60
                        )
                }
        );

        dbHelper.createCourse(
                new CourseModel(
                        "Kurs Vue z [Redacted]",
                        "W tym kursie zaprezentuje podstawy vue",
                        getString(R.string.vue_img),
                        1,
                        40
                ),
                new CourseOption[]{
                        new CourseOption(
                                "Routing",
                                "w tym dodatku przedstawię ci jak korzystać z routingu",
                                20
                        ),
                        new CourseOption(
                                "Nuxt",
                                "podstawy i wprowadzenie do nuxt.js",
                                180
                        ),
                        new CourseOption(
                                "UX design",
                                "podstawy UX",
                                60
                        )
                }
        );

        dbHelper.createCourse(
                new CourseModel(
                        "Javunia z kawunią",
                        "Lubisz kawę? To na pewno polubisz, też jave!",
                        getString(R.string.java_img),
                        1,
                        120
                ),
                new CourseOption[]{
                        new CourseOption(
                                "JVM - podstawy działania",
                                "warto wiedzieć jak będzie zachowywał się kod podczas emulacji",
                                20
                        ),
                        new CourseOption(
                                "javac",
                                "wszystko o kompilatorze javy",
                                60
                        )
                }
        );

//        dbHelper.createOrder(
//                new OrderModel(
//                        10,
//                        new Date(2022, 12, 30),
//                        300
//                ),
//                1,
//                1,
//                new long[] {
//                        1, 2
//                }
//        );

    }
}