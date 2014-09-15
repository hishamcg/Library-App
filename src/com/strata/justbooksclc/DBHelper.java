package com.strata.justbooksclc;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "MyDBName.db";
   public static final String[] TABLE_NAME = {"your_next_read","new_arrivals","top_rentals"};
   public static final String CONTACTS_COLUMN_ID = "id";
   public static final String COLUMN_DATA = "mydata";

   public DBHelper(Context context)
   {
      super(context, DATABASE_NAME , null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table "+TABLE_NAME[0]+"(id integer primary key autoincrement, mydata text)");
      db.execSQL("create table "+TABLE_NAME[1]+"(id integer primary key autoincrement, mydata text)");
      db.execSQL("create table "+TABLE_NAME[2]+"(id integer primary key autoincrement, mydata text)");}

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME[0]);
      onCreate(db);
   }
   
   public void insertAllData  (String[] data,int tab){
	   SQLiteDatabase db = this.getWritableDatabase();
	   db.delete(TABLE_NAME[tab], null, null);
	   for(int i=0; data[i] != null; i++){
	      ContentValues contentValues = new ContentValues();
	      contentValues.put(COLUMN_DATA, data[i]);
	      db.insert(TABLE_NAME[tab], null, contentValues);
	   }
   }
   public boolean insertContact  (String data,int tab)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();

      contentValues.put(COLUMN_DATA, data);

      db.insert(TABLE_NAME[tab], null, contentValues);
      return true;
   }
   public Cursor getData(int id , int tab){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME[tab]+" where id="+id+"", null );
      return res;
   }
   public int numberOfRows( int tab){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME[tab]);
      return numRows;
   }
   public boolean updateContact (Integer id, String data, int tab)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put(COLUMN_DATA, data);
      db.update(TABLE_NAME[tab], contentValues, "id = ? ", new String[] { Integer.toString(id) } );
      return true;
   }

   public Integer deleteContact (Integer id , int tab)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete(TABLE_NAME[tab], "id = ? ", new String[] { Integer.toString(id) });
   }
   public ArrayList<String> getAllCotacts(int tab)
   {
      ArrayList<String> array_list = new ArrayList<String>();
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME[tab], null );
      res.moveToFirst();
      while(res.isAfterLast() == false){
      array_list.add(res.getString(res.getColumnIndex(COLUMN_DATA)));
      res.moveToNext();
      }
   return array_list;
   }
}
