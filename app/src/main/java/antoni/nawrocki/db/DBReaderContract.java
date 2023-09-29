package antoni.nawrocki.db;

import android.provider.BaseColumns;

/**
 * purely static class used to contain DB schema
 */
public final class DBReaderContract {
    /**
     * Private constructor not to initialize this class by accident :)
     */
    private DBReaderContract() {}

    /**
     * Table containing Courses
     */
    public static class Courses implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_THUMBNAIL = "thumbnail";
        public static final String COLUMN_NAME_CREATOR_ID = "creator_id";
        public static final String COLUMN_NAME_PRICE = "price";
    }

    /**
     * Table containing CoursesOptions
     */
    public static class CoursesOptions implements BaseColumns {
        public static final String TABLE_NAME = "options";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PRICE = "price";
    }

    /**
     * Table containing Users
     */
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_LOGIN = "login";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_IS_ADMIN = "is_admin";
        public static final String COLUMN_NAME_IS_COMPANY = "is_company";
        public static final String COLUMN_NAME_PROFILE_PICTURE = "profile_picture";
    }

    /**
     * Table containing Orders
     */
    public static class Orders implements BaseColumns {
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE = "order_date";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
    }

    // As I don't want any duplicates in Options just to save them occasionally for the orders,
    // I decided to make another table to store every option selected for any given order.
    // It is later linked inside "orders" table.
    /**
     * Table containing OrderOptions
     */
    public static class OrdersOptions implements BaseColumns {
        public static final String TABLE_NAME = "orders_options";
        public static final String COLUMN_NAME_ORDER_ID = "order_id";
        public static final String COLUMN_NAME_OPTION_ID = "option_id";
    }

    // All the boiler plate for creating tables, Helper class already manages the process
    // of creating new database.
    /**
     * Query creating courses table
     */
    public static final String CREATE_TABLE_COURSES =
            "CREATE TABLE " + Courses.TABLE_NAME + " (" +
                    Courses._ID + " INTEGER PRIMARY KEY," +
                    Courses.COLUMN_NAME_TITLE + " TEXT," +
                    Courses.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    Courses.COLUMN_NAME_THUMBNAIL + " TEXT," +
                    Courses.COLUMN_NAME_CREATOR_ID + " INTEGER," +
                    Courses.COLUMN_NAME_PRICE + " REAL);";


    /**
     * Query creating orders table
     */
    public static final String CREATE_TABLE_ORDERS =
            "CREATE TABLE " + Orders.TABLE_NAME + " (" +
                Orders._ID + " INTEGER PRIMARY KEY," +
                Orders.COLUMN_NAME_AMOUNT + " INTEGER," +
                Orders.COLUMN_NAME_DATE + " DATETIME," +
                Orders.COLUMN_NAME_PRICE + " REAL," +
                Orders.COLUMN_NAME_USER_ID + " INTEGER," +
                Orders.COLUMN_NAME_COURSE_ID + " INTEGER);";

    /**
     * Query creating courses options table
     */
    public static final String CREATE_TABLE_COURSES_OPTIONS =
            "CREATE TABLE " + CoursesOptions.TABLE_NAME + " (" +
                CoursesOptions._ID + " INTEGER PRIMARY KEY," +
                CoursesOptions.COLUMN_NAME_COURSE_ID + " INTEGER," +
                CoursesOptions.COLUMN_NAME_TITLE + " TEXT," +
                CoursesOptions.COLUMN_NAME_DESCRIPTION + " TEXT," +
                CoursesOptions.COLUMN_NAME_PRICE + " REAL);";

    /**
     * Query creating orders options table
     */
    public static final String CREATE_TABLE_ORDERS_OPTIONS =
            "CREATE TABLE " + OrdersOptions.TABLE_NAME + " (" +
                OrdersOptions.COLUMN_NAME_ORDER_ID + " INTEGER," +
                OrdersOptions.COLUMN_NAME_OPTION_ID + " INTEGER);";

    /**
     * Query creating users table
     */
    public static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + Users.TABLE_NAME + " (" +
                Users._ID + " INTEGER PRIMARY KEY," +
                Users.COLUMN_NAME_USERNAME + " TEXT," +
                Users.COLUMN_NAME_LOGIN + " TEXT," +
                Users.COLUMN_NAME_PASSWORD + " TEXT," +
                Users.COLUMN_NAME_IS_ADMIN + " BOOLEAN," +
                Users.COLUMN_NAME_IS_COMPANY + " BOOLEAN," +
                Users.COLUMN_NAME_PROFILE_PICTURE + " TEXT);";


    // Not necessary, but it will help with cleaning up... maybe
    /**
     * Query deleting table
     */
    public static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS ";
}
