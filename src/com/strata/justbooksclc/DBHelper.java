package com.strata.justbooksclc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "MyDBName.db";
	public static final String[] TAG_TABLE_NAME = {"next_read","new_arrival","top_rentals"};
	
	//private static final String TAG_COLUMN_ID = "id";
	private static final String TAG_COLUMN_DATA = "data";
	/*private static final String TAG_WISHLIST = "titles";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ID = "book_id";
	private static final String TIMES_RENTED = "no_of_times_rented";
	private static final String AVG_READING = "avg_reading_times";
	private static final String IMAGE_URL = "image_url";
    private static final String SUMMARY = "summary";*/

   public DBHelper(Context context)
   {
      super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
      /*db.execSQL(
      "create table top_rentals " +
      "(id integer primary key, "+TAG_WISHLIST+" text,"+TAG_AUTHOR+" text,"+TAG_CATEGORY+" text,"+TAG_PAGE+" text,"+TAG_LANGUAGE+" text,"+TAG_TITLE+" text,"+TAG_ID+" text,"+TIMES_RENTED+" text,"+AVG_READING+" text,"+IMAGE_URL+" text,"+SUMMARY+" text)"
      );*/
	  db.execSQL( "create table "+TAG_TABLE_NAME[0]+" (id integer primary key, "+TAG_COLUMN_DATA+" text)");
	  db.execSQL( "create table "+TAG_TABLE_NAME[1]+" (id integer primary key, "+TAG_COLUMN_DATA+" text)");
	  db.execSQL( "create table "+TAG_TABLE_NAME[2]+" (id integer primary key, "+TAG_COLUMN_DATA+" text)");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      db.execSQL("DROP TABLE IF EXISTS "+TAG_TABLE_NAME[0]);
      db.execSQL("DROP TABLE IF EXISTS "+TAG_TABLE_NAME[1]);
      db.execSQL("DROP TABLE IF EXISTS "+TAG_TABLE_NAME[2]);
      onCreate(db);
   }
   public boolean deleteAll(int table_no){
	   SQLiteDatabase db = this.getWritableDatabase();
	   db.delete(TAG_TABLE_NAME[table_no],null,null);
	   return true;
   }
   public boolean insertData(String data, int table_no){
	  SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();

      contentValues.put(TAG_COLUMN_DATA, data);

      db.insert(TAG_TABLE_NAME[table_no], null, contentValues);
      return true;
   }
   public boolean insertContact  (String name, String phone, String email, String street,String place)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();

      contentValues.put("name", name);
      contentValues.put("phone", phone);
      contentValues.put("email", email);	
      contentValues.put("street", street);
      contentValues.put("place", place);

      db.insert(TAG_TABLE_NAME[0], null, contentValues);
      return true;
   }
   public Cursor getData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+TAG_TABLE_NAME+" where id="+id+"", null );
      return res;
   }
   public int numberOfRows(int table_no){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, TAG_TABLE_NAME[table_no]);
      return numRows;
   }
   public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("name", name);
      contentValues.put("phone", phone);
      contentValues.put("email", email);
      contentValues.put("street", street);
      contentValues.put("place", place);
      db.update(TAG_TABLE_NAME[0], contentValues, "id = ? ", new String[] { Integer.toString(id) } );
      return true;
   }

   public Integer deleteContact (Integer id, int table_no)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(TAG_TABLE_NAME[table_no], 
      "id = ? ", 
      new String[] { Integer.toString(id) });
   }
   public String[] getAllContacts(int table_no)
   {
      String[] array_list = new String[numberOfRows(table_no)];
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+TAG_TABLE_NAME[table_no], null );
      res.moveToFirst();
      for (int i=0;res.isAfterLast() == false;i++){
      array_list[i]=res.getString(res.getColumnIndex(TAG_COLUMN_DATA));
      res.moveToNext();
      }
   return array_list;
   }
}