package axeluser.bearbasket.DbUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexey on 27.02.2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "bearBasketDb";

    public static class TableBasketLists {
        public static final String TABLE_NAME = "list";
        public static final String KEY_ID = "_id";
        public static final String KEY_NAME = "name";
    }

    public static class TableBasketItems {
        public static final String TABLE_NAME = "basket_item";
        public static final String KEY_ID = "_id";
        public static final String KEY_NAME = "name";
        public static final String KEY_LIST_ID = "list_id";
        public static final String KEY_COUNT = "count";
        public static final String KEY_CHECKED = "is_checked";
        public static final String KEY_DATE_CREATED = "date_created";
        public static final String KEY_DATE_CHECKED = "date_checked";

    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    void CreateTableLists(SQLiteDatabase db){
        String createSQL = "create table " + TableBasketLists.TABLE_NAME + " ("
                + TableBasketLists.KEY_ID + " integer primary key autoincrement, "
                + TableBasketLists.KEY_NAME + " text not null);";
        db.execSQL(createSQL);
    }

    void CreateTableBasketItems(SQLiteDatabase db){
        String createSQL = "create table " + TableBasketItems.TABLE_NAME + " ("
                + TableBasketItems.KEY_ID + " integer primary key autoincrement, "
                + TableBasketItems.KEY_NAME + " text not null, "
                + TableBasketItems.KEY_COUNT + " text, "
                + TableBasketItems.KEY_LIST_ID + " integer, "
                + TableBasketItems.KEY_CHECKED + " integer default 0, "
                + TableBasketItems.KEY_DATE_CREATED + " text, "
                + TableBasketItems.KEY_DATE_CHECKED + " text, "
                + "FOREIGN KEY ("+TableBasketItems.KEY_LIST_ID+") "
                + "REFERENCES " + TableBasketLists.TABLE_NAME + "(" + TableBasketLists.KEY_ID + ")"
                + ");";
        db.execSQL(createSQL);
    }

    void DropTables(SQLiteDatabase db){
        db.execSQL("drop table if exists " + TableBasketLists.TABLE_NAME);
        db.execSQL("drop table if exists " + TableBasketItems.TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CreateTableLists(db);
        CreateTableBasketItems(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DropTables(db);
        onCreate(db);
    }
}
