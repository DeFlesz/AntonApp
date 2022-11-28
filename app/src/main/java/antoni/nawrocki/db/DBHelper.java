package antoni.nawrocki.db;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import antoni.nawrocki.models.CourseModel;
import antoni.nawrocki.models.CourseOption;
import antoni.nawrocki.models.OrderModel;
import antoni.nawrocki.models.UserModel;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AntonCerts.db";



    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        db.execSQL(DELETE_TABLE + CREATE_TABLE_COURSES);
        db.execSQL(DELETE_TABLE + CREATE_TABLE_COURSES_OPTIONS);
        db.execSQL(DELETE_TABLE + CREATE_TABLE_ORDERS);
        db.execSQL(DELETE_TABLE + CREATE_TABLE_ORDERS_OPTIONS);
        db.execSQL(DELETE_TABLE + CREATE_TABLE_USERS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        onUpgrade(db, oldVer, newVer);
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

    public ArrayList<HashMap<String, String>> getCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> queryResult = new ArrayList<>();

        String[] projection = {
                Courses._ID,
                Courses.COLUMN_NAME_TITLE,
                Courses.COLUMN_NAME_DESCRIPTION,
                Courses.COLUMN_NAME_PRICE,
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

            queryResult.add(course);
        }

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
