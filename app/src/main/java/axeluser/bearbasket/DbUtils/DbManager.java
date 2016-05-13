package axeluser.bearbasket.DbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import axeluser.bearbasket.DbUtils.Entities.BasketItem;
import axeluser.bearbasket.DbUtils.Entities.BasketList;

/**
 * Created by Alexey on 27.02.2016.
 */
public class DbManager {

    private Context context;
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat dateFormat;

    public DbManager(Context context) {
        this.context = context;
        dateFormat = new SimpleDateFormat(DATE_PATTERN);
    }

    private String DateToString(Date date){
        return dateFormat.format(date);
    }

    private Date DateFromString(String rawDate){
        try{
            return dateFormat.parse(rawDate);
        }
        catch (Exception e){
            return new Date();
        }

    }

    public ArrayList<BasketList> getAllLists(){
        ArrayList<BasketList> basketLists = new ArrayList<BasketList>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DbHelper.TableBasketLists.TABLE_NAME, null, null,
                null, null, null,
                DbHelper.TableBasketLists.KEY_ID + " DESC");
        if(cursor.moveToFirst()){
            int idId = cursor.getColumnIndex(DbHelper.TableBasketLists.KEY_ID);
            int idName = cursor.getColumnIndex(DbHelper.TableBasketLists.KEY_NAME);
            do {
                BasketList list = new BasketList();
                list.setId(cursor.getInt(idId));
                list.setName(cursor.getString(idName));
                basketLists.add(list);
            }while (cursor.moveToNext());
        }
        dbHelper.close();
        return basketLists;
    }

    public BasketList getListById(long id){
        BasketList basketList = new BasketList();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DbHelper.TableBasketLists.TABLE_NAME, null,
                DbHelper.TableBasketLists.KEY_ID+ "=" +id,
                null, null, null,
                DbHelper.TableBasketLists.KEY_ID + " DESC");
        if(cursor.moveToFirst()){
            int idId = cursor.getColumnIndex(DbHelper.TableBasketLists.KEY_ID);
            int idName = cursor.getColumnIndex(DbHelper.TableBasketLists.KEY_NAME);
            basketList.setId(cursor.getInt(idId));
            basketList.setName(cursor.getString(idName));
        }
        return basketList;
    }

    public void updateList(BasketList list){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.TableBasketLists.KEY_NAME, list.getName());
        database.update(DbHelper.TableBasketLists.TABLE_NAME, cv,
                DbHelper.TableBasketLists.KEY_ID+"="+list.getId(), null);
        dbHelper.close();
    }

    public ArrayList<BasketItem> getAllItemsForList(BasketList list){
        ArrayList<BasketItem> basketListItems = new ArrayList<BasketItem>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DbHelper.TableBasketItems.TABLE_NAME, null,
                DbHelper.TableBasketItems.KEY_LIST_ID+"="+list.getId(),
                null, null, null,
                DbHelper.TableBasketItems.KEY_ID + " DESC");
        if(cursor.moveToFirst()){
            int idId = cursor.getColumnIndex(DbHelper.TableBasketItems.KEY_ID);
            int idName = cursor.getColumnIndex(DbHelper.TableBasketItems.KEY_NAME);
            int idCount = cursor.getColumnIndex(DbHelper.TableBasketItems.KEY_COUNT);
            int idListId = cursor.getColumnIndex(DbHelper.TableBasketItems.KEY_LIST_ID);
            int idChecked = cursor.getColumnIndex(DbHelper.TableBasketItems.KEY_CHECKED);
            int idCreatedDate = cursor.getColumnIndex(DbHelper.TableBasketItems.KEY_DATE_CREATED);
            int idCheckedDate = cursor.getColumnIndex(DbHelper.TableBasketItems.KEY_DATE_CHECKED);
            do {
                BasketItem item = new BasketItem();
                item.setId(cursor.getInt(idId));
                item.setName(cursor.getString(idName));
                item.setCount(cursor.getString(idCount));
                item.setListId(cursor.getInt(idListId));
                item.setChecked(cursor.getInt(idChecked)>0);
                item.setCreationDate(DateFromString(cursor.getString(idCreatedDate)));
                item.setCheckingDate(DateFromString(cursor.getString(idCheckedDate)));
                basketListItems.add(item);
            }while (cursor.moveToNext());
        }
        dbHelper.close();
        return basketListItems;
    }

    public void updateListItem(BasketItem item){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.TableBasketItems.KEY_NAME, item.getName());
        cv.put(DbHelper.TableBasketItems.KEY_COUNT, item.getCount());
        cv.put(DbHelper.TableBasketItems.KEY_LIST_ID, item.getListId());
        cv.put(DbHelper.TableBasketItems.KEY_CHECKED, item.isChecked()?1:0);
        cv.put(DbHelper.TableBasketItems.KEY_DATE_CREATED, DateToString(item.getCreationDate()));
        cv.put(DbHelper.TableBasketItems.KEY_DATE_CHECKED, DateToString(item.getCheckingDate()));
        database.update(DbHelper.TableBasketItems.TABLE_NAME, cv,
                DbHelper.TableBasketItems.KEY_ID+"="+ item.getId(), null);
        dbHelper.close();
    }

    public void deleteList(BasketList list){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(DbHelper.TableBasketLists.TABLE_NAME,
                DbHelper.TableBasketLists.KEY_ID+"="+list.getId(), null);
        dbHelper.close();
    }

    public void deleteListItem(BasketItem item){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(DbHelper.TableBasketItems.TABLE_NAME,
                DbHelper.TableBasketItems.KEY_ID+"="+ item.getId(), null);
        dbHelper.close();
    }

    public void createList(BasketList list){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.TableBasketLists.KEY_NAME, list.getName());
        long id = database.insert(DbHelper.TableBasketLists.TABLE_NAME, null, cv);
        list.setId(id);
        dbHelper.close();
    }

    public void createListItem(BasketItem item){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.TableBasketItems.KEY_NAME, item.getName());
        cv.put(DbHelper.TableBasketItems.KEY_COUNT, item.getCount());
        cv.put(DbHelper.TableBasketItems.KEY_LIST_ID, item.getListId());
        cv.put(DbHelper.TableBasketItems.KEY_CHECKED, item.isChecked()?1:0);
        cv.put(DbHelper.TableBasketItems.KEY_DATE_CREATED, DateToString(item.getCreationDate()));
        cv.put(DbHelper.TableBasketItems.KEY_DATE_CHECKED, DateToString(item.getCheckingDate()));
        long id = database.insert(DbHelper.TableBasketItems.TABLE_NAME, null, cv);
        item.setId(id);
        dbHelper.close();
    }
}
