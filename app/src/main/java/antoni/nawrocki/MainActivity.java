package antoni.nawrocki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.sql.Date;
import java.sql.Time;

import antoni.nawrocki.db.DBHelper;
import antoni.nawrocki.fragments.CourseList;
import antoni.nawrocki.fragments.SignUp;
import antoni.nawrocki.models.CourseModel;
import antoni.nawrocki.models.CourseOption;
import antoni.nawrocki.models.OrderModel;
import antoni.nawrocki.models.UserModel;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        fragmentContainerView = findViewById(R.id.fragment_container);

//        FragmentManager fm = getSupportFragmentManager();
//        fm.beginTransaction()
//                .add(R.id.fragment_container, CourseList)



        setUpDB();
    }

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
                        "Super Firma Gruz",
                        "gruz",
                        "1234",
                        false,
                        true,
                        ""
                )
        );
        dbHelper.createCourse(
                new CourseModel(
                        "Jak opowiedzieć żart",
                        "W tym kursie zaprezentuje jak opowiadać żarty",
                        "",
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
                        "",
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
                        "",
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_signup:
                getSupportFragmentManager().popBackStack();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new SignUp())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    //    @Override
//    protected void onDestroy() {
//        if (dbHelper != null) {
//            dbHelper.close();
//        }
//        super.onDestroy();
//    }
}