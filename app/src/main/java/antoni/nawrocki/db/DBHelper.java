package antoni.nawrocki.db;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void createUser(
            String username,
            String login,
            String password,
            boolean isAdmin,
            boolean isCompany,
            String profilePicture
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Users.COLUMN_NAME_USERNAME, username);
        values.put(Users.COLUMN_NAME_LOGIN, login);
        values.put(Users.COLUMN_NAME_PASSWORD, password);
        values.put(Users.COLUMN_NAME_IS_ADMIN, isAdmin);
        values.put(Users.COLUMN_NAME_IS_COMPANY, isCompany);
        values.put(Users.COLUMN_NAME_PROFILE_PICTURE, profilePicture);

        db.insert(Users.TABLE_NAME, null, values);
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
