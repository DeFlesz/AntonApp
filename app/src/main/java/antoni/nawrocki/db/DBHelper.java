package antoni.nawrocki.db;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import antoni.nawrocki.R;
import antoni.nawrocki.models.CourseModel;
import antoni.nawrocki.models.CourseOption;
import antoni.nawrocki.models.OrderModel;
import antoni.nawrocki.models.UserModel;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AntonCerts.db";
    Context context;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_COURSES_OPTIONS);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_ORDERS_OPTIONS);
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE + Users.TABLE_NAME);
        db.execSQL(DELETE_TABLE + Courses.TABLE_NAME);
        db.execSQL(DELETE_TABLE + Orders.TABLE_NAME);
        db.execSQL(DELETE_TABLE + OrdersOptions.TABLE_NAME);
        db.execSQL(DELETE_TABLE + CoursesOptions.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        onUpgrade(db, oldVer, newVer);
    }

    public boolean registerUser(String username, String login, String password, boolean isCompany, String profilePic){
        if (getUser(login) != null) {
            return false;
        }
        UserModel newUser = new UserModel(
                null,
                username,
                login,
                password,
                false,
                isCompany,
                profilePic
        );

        createUser(newUser);
        return true;
    }

    public void createUser(
            UserModel user
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Users.COLUMN_NAME_USERNAME, user.getUsername());
        values.put(Users.COLUMN_NAME_LOGIN, user.getLogin());
        values.put(Users.COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(Users.COLUMN_NAME_IS_ADMIN, user.isAdmin());
        values.put(Users.COLUMN_NAME_IS_COMPANY, user.isCompany());
        values.put(Users.COLUMN_NAME_PROFILE_PICTURE, user.getProfilePicture());

        db.insert(Users.TABLE_NAME, null, values);
    }

    public boolean loginUser(String login, String password1){
        if (getUser(login) == null) {

            return false;
        }

        UserModel userModel = getUser(login);

        if (userModel == null) {
            return false;
        }

        String password2 = userModel.getPassword();

        if (password2 == null) {
            return false;
        }

        if (!password2.equals(password1)) {
//            Log.e("AN", "loginUser: " + password1 + " " + password2);
            return false;
        }

        return true;
    }

    public UserModel getUser(String login) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                Users.COLUMN_NAME_USERNAME,
                Users.COLUMN_NAME_LOGIN,
                Users.COLUMN_NAME_PASSWORD,
                Users._ID,
                Users.COLUMN_NAME_IS_ADMIN,
                Users.COLUMN_NAME_IS_COMPANY,
                Users.COLUMN_NAME_PROFILE_PICTURE
        };

        String selection = Users.COLUMN_NAME_LOGIN + " = ?";
        String[] selectionArgs = {login + ""};

        Cursor cursor = db.query(
                Users.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // hopefully that will fix cursor 0 size data exception :)
        if (cursor != null && cursor.moveToFirst()) {
            UserModel userModel = new UserModel(
                    cursor.getString(cursor.getColumnIndexOrThrow(Users._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Users.COLUMN_NAME_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Users.COLUMN_NAME_LOGIN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Users.COLUMN_NAME_PASSWORD)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(Users.COLUMN_NAME_IS_ADMIN))),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(Users.COLUMN_NAME_IS_COMPANY))),
                    cursor.getString(cursor.getColumnIndexOrThrow(Users.COLUMN_NAME_PROFILE_PICTURE))
            );

            cursor.close();

            return userModel;
        }
//        Log.e("AN", "DZIAŁA2");

        return null;
    }

    //remove price it will be counted later
    public void createOrder(OrderModel order, long userID, long courseID, long[] selectedOptionsIDs){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues orderValues = new ContentValues();
        orderValues.put(Orders.COLUMN_NAME_AMOUNT, order.getAmount());
        orderValues.put(Orders.COLUMN_NAME_DATE, order.getDate().toString());
        orderValues.put(Orders.COLUMN_NAME_PRICE, order.getPrice());
        orderValues.put(Orders.COLUMN_NAME_USER_ID, userID);
        orderValues.put(Orders.COLUMN_NAME_COURSE_ID, courseID);

        long orderID = db.insert(Orders.TABLE_NAME, null, orderValues);

        for (long optionID :
                selectedOptionsIDs) {
            ContentValues orderOptionValues = new ContentValues();
            orderOptionValues.put(OrdersOptions.COLUMN_NAME_ORDER_ID, orderID);
            orderOptionValues.put(OrdersOptions.COLUMN_NAME_OPTION_ID, optionID);
            db.insert(OrdersOptions.TABLE_NAME, null, orderOptionValues);
        }
    }

    public void createCourse(CourseModel courseModel, CourseOption[] courseOptions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues courseValues = new ContentValues();
        courseValues.put(Courses.COLUMN_NAME_TITLE, courseModel.getTitle());
        courseValues.put(Courses.COLUMN_NAME_DESCRIPTION, courseModel.getDescription());
        courseValues.put(Courses.COLUMN_NAME_THUMBNAIL, courseModel.getThumbnail());
        courseValues.put(Courses.COLUMN_NAME_PRICE, courseModel.getPrice());
        courseValues.put(Courses.COLUMN_NAME_CREATOR_ID, courseModel.getUserID());

        long courseID = db.insert(Courses.TABLE_NAME, null, courseValues);

        for (CourseOption courseOption :
                courseOptions) {
            ContentValues optionValues = new ContentValues();
            optionValues.put(CoursesOptions.COLUMN_NAME_COURSE_ID, courseID);
            optionValues.put(CoursesOptions.COLUMN_NAME_TITLE, courseOption.getTitle());
            optionValues.put(CoursesOptions.COLUMN_NAME_DESCRIPTION, courseOption.getDescription());
            optionValues.put(CoursesOptions.COLUMN_NAME_PRICE, courseOption.getPrice());
            db.insert(CoursesOptions.TABLE_NAME, null, optionValues);
        }
    }

    public ArrayList<CourseOption> getOptions(long courseID) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = new String[] {
                CoursesOptions._ID,
                CoursesOptions.COLUMN_NAME_TITLE,
                CoursesOptions.COLUMN_NAME_DESCRIPTION,
                CoursesOptions.COLUMN_NAME_PRICE,
                CoursesOptions.COLUMN_NAME_COURSE_ID
        };

        String selection = CoursesOptions.COLUMN_NAME_COURSE_ID + " = ?";
        String[] selectionArgs = new String[] {courseID + ""};

        String sortOrder = CoursesOptions.COLUMN_NAME_PRICE + " ASC";

        Cursor cursor = db.query(
                CoursesOptions.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        ArrayList<CourseOption> options = new ArrayList<>();

        while (cursor.moveToNext()){
            CourseOption course = new CourseOption(
                    cursor.getString(cursor.getColumnIndexOrThrow(CoursesOptions._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CoursesOptions.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CoursesOptions.COLUMN_NAME_DESCRIPTION)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(CoursesOptions.COLUMN_NAME_PRICE)))
            );

            options.add(course);
        }

        cursor.close();

        return options;
    }

    public HashMap<String, String> getCourse(long courseID) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, String> courseData = new HashMap<>();

        String[] projection = {
                Courses.COLUMN_NAME_TITLE,
                Courses.COLUMN_NAME_DESCRIPTION,
                Courses.COLUMN_NAME_PRICE,
                Courses.COLUMN_NAME_THUMBNAIL
        };

        String selection = Courses._ID + " = ?";
        String[] selectionArgs = {courseID + ""};

        Cursor cursor = db.query(
                Courses.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToNext();
        courseData.put(Courses.COLUMN_NAME_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_TITLE)));
        courseData.put(Courses.COLUMN_NAME_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_DESCRIPTION)));
        courseData.put(Courses.COLUMN_NAME_PRICE, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_PRICE)));
        courseData.put(Courses.COLUMN_NAME_THUMBNAIL, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_THUMBNAIL)));

        cursor.close();
        return courseData;
    }

    public ArrayList<HashMap<String, String>> getCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> queryResult = new ArrayList<>();

        String[] projection = {
                Courses._ID,
                Courses.COLUMN_NAME_TITLE,
                Courses.COLUMN_NAME_DESCRIPTION,
                Courses.COLUMN_NAME_PRICE,
                Courses.COLUMN_NAME_THUMBNAIL
        };

        String sortOrder = Courses.COLUMN_NAME_PRICE + " ASC";

        Cursor cursor = db.query(
                Courses.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()){
            HashMap<String, String> course = new HashMap<>();

            course.put(Courses._ID, cursor.getString(cursor.getColumnIndexOrThrow(Courses._ID)));
            course.put(Courses.COLUMN_NAME_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_TITLE)));
            course.put(Courses.COLUMN_NAME_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_DESCRIPTION)));
            course.put(Courses.COLUMN_NAME_PRICE, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_PRICE)));
            course.put(Courses.COLUMN_NAME_THUMBNAIL, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_THUMBNAIL)));

            queryResult.add(course);
        }

        cursor.close();
        return queryResult;
    }

    public ArrayList<HashMap<String, String>> getCourses(long userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> queryResult = new ArrayList<>();

        String[] projection = {
                Courses._ID,
                Courses.COLUMN_NAME_TITLE,
                Courses.COLUMN_NAME_DESCRIPTION,
                Courses.COLUMN_NAME_PRICE,
                Courses.COLUMN_NAME_THUMBNAIL
        };

        ArrayList<HashMap<String, String>> userOrders = getOrders(userID);
        String statement = "";
        String[] strings = new String[userOrders.size() ];

        if (userOrders.size() < 1) {
            return getCourses();
        }

        for (int i = 0; i < userOrders.size(); i++){
            HashMap<String, String> hashMap = userOrders.get(i);
            strings[i] = (hashMap.get(Orders.COLUMN_NAME_COURSE_ID));

            statement += "?";

            if (i == userOrders.size()-1){
                break;
            }

            statement += ", ";
        }

//        Toast.makeText(context, statement, Toast.LENGTH_SHORT).show();

        String selection = Courses._ID + " NOT IN ( "+ statement +" )";
        String[] selectionArgs = strings;

        if (userOrders.size() == 1) {
            selection = Courses._ID + " != ?";
            selectionArgs = new String[]{userOrders.get(0).get(Orders.COLUMN_NAME_COURSE_ID)};
        }

        String sortOrder = Courses.COLUMN_NAME_PRICE + " ASC";

        Cursor cursor = db.query(
                Courses.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()){
            HashMap<String, String> course = new HashMap<>();

            course.put(Courses._ID, cursor.getString(cursor.getColumnIndexOrThrow(Courses._ID)));
            course.put(Courses.COLUMN_NAME_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_TITLE)));
            course.put(Courses.COLUMN_NAME_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_DESCRIPTION)));
            course.put(Courses.COLUMN_NAME_PRICE, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_PRICE)));
            course.put(Courses.COLUMN_NAME_THUMBNAIL, cursor.getString(cursor.getColumnIndexOrThrow(Courses.COLUMN_NAME_THUMBNAIL)));

            queryResult.add(course);
        }

        cursor.close();
        return queryResult;
    }

    public String getCourseName(long courseID) {
        HashMap<String, String> courseData = getCourse(courseID);

        return courseData.get(Courses.COLUMN_NAME_TITLE);
    }

    public long getUserID() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(context.getString(R.string.preference_login_key), "");

        if (login == "") {
            return -1;
        }

        UserModel userData = getUser(login);

        if (userData == null || userData.getId() == null) {
            return -1;
        }

        return Long.parseLong(userData.getId());
    }

    public ArrayList<Long> getOptionIDs(long orderID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Long> queryResult = new ArrayList<>();

        String[] projection = {
                OrdersOptions.COLUMN_NAME_ORDER_ID,
                OrdersOptions.COLUMN_NAME_OPTION_ID
        };

        String selection = OrdersOptions.COLUMN_NAME_ORDER_ID + " = ?";
        String[] selectionArgs = {orderID + ""};

        String sortOrder = OrdersOptions.COLUMN_NAME_OPTION_ID + " DESC";

        Cursor cursor = db.query(
                OrdersOptions.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()){
            queryResult.add(cursor.getLong(cursor.getColumnIndexOrThrow(OrdersOptions.COLUMN_NAME_OPTION_ID)));
        }

        return queryResult;
    }

    public CourseOption getOptionData(long optionID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                CoursesOptions._ID,
                CoursesOptions.COLUMN_NAME_TITLE,
                CoursesOptions.COLUMN_NAME_DESCRIPTION,
                CoursesOptions.COLUMN_NAME_PRICE
        };

        String selection = CoursesOptions._ID + " = ?";
        String[] selectionArgs = {optionID + ""};

        Cursor cursor = db.query(
                CoursesOptions.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            CourseOption courseOption = new CourseOption(
                    cursor.getString(cursor.getColumnIndexOrThrow(CoursesOptions._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CoursesOptions.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CoursesOptions.COLUMN_NAME_DESCRIPTION)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(CoursesOptions.COLUMN_NAME_PRICE)))
            );

            cursor.close();

            return courseOption;
        }

        return null;
    }

    public ArrayList<HashMap<String, String>> getOrders(long userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> queryResult = new ArrayList<>();

        String[] projection = {
                Orders._ID,
                Orders.COLUMN_NAME_USER_ID,
                Orders.COLUMN_NAME_COURSE_ID,
                Orders.COLUMN_NAME_DATE,
                Orders.COLUMN_NAME_PRICE,
                Orders.COLUMN_NAME_AMOUNT,
        };

        String selection = Orders.COLUMN_NAME_USER_ID + " = ?";
        String[] selectionArgs = {userID + ""};

        String sortOrder = Orders.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.query(
                Orders.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()){
            HashMap<String, String> course = new HashMap<>();

            course.put(Orders._ID, cursor.getString(cursor.getColumnIndexOrThrow(Orders._ID)));
//            course.put(Orders.COLUMN_NAME_COURSE_ID, cursor.getString(cursor.getColumnIndexOrThrow(Orders.COLUMN_NAME_COURSE_ID)));

            String courseID = cursor.getString(cursor.getColumnIndexOrThrow(Orders.COLUMN_NAME_COURSE_ID));

            String courseName = getCourseName(Long.parseLong(courseID));
            course.put(Courses.COLUMN_NAME_TITLE, courseName);
            course.put(Orders.COLUMN_NAME_COURSE_ID, courseID);

            course.put(Orders.COLUMN_NAME_DATE, cursor.getString(cursor.getColumnIndexOrThrow(Orders.COLUMN_NAME_DATE)));
            course.put(Orders.COLUMN_NAME_PRICE, cursor.getString(cursor.getColumnIndexOrThrow(Orders.COLUMN_NAME_PRICE)));

            queryResult.add(course);
        }

        cursor.close();
        return queryResult;
    }

    // TODO
    // create new course (course data)
    // register (username, login, password1, password2)
    // login (login, password)
    // order (course, options, user, amount, current_date)
    // get orders (username)
    // get courses
    // get courseOptions
}
