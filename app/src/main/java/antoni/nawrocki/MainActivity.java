package antoni.nawrocki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import antoni.nawrocki.db.DBHelper;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
    }

//    @Override
//    protected void onDestroy() {
//        if (dbHelper != null) {
//            dbHelper.close();
//        }
//        super.onDestroy();
//    }
}