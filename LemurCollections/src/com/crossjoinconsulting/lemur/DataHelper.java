package com.crossjoinconsulting.lemur;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.SQLException;
import android.util.Log;

public class DataHelper {

	public static final String SERVICE_KEY_ROWID = "SvcID";
    public static final String SERVICE_KEY_NAME = "SvcName";
    public static final String SERVICE_KEY_PAIDSVC = "PaidSvc";
    public static final String USER_KEY_ROWID = "UserID";
    public static final String USER_KEY_NAME = "UserName";
    public static final String USER_KEY_FIRSTNAME = "FirstName";
    public static final String USER_KEY_LASTNAME = "LastName";
    public static final String USER_KEY_PWD = "Password";
    public static final String USERSERVICE_KEY_ROWID = "UserSvcID";
    public static final String USERSERVICE_KEY_SVCID = "SvcID";
    public static final String USERSERVICE_KEY_USERID = "UserID";
    public static final String ITEM_KEY_ROWID = "ItemID";
    public static final String ITEM_KEY_UPC = "UPC";
    public static final String ITEM_KEY_NAME = "ItemName";
    public static final String ITEM_KEY_CREATED = "Created";
    public static final String ITEM_KEY_CREATOR = "CreatorID";
    public static final String ITEM_KEY_MODIFIED = "Modified";
    public static final String ITEM_KEY_MODIFIER = "ModifierID";
    public static final String ITEM_KEY_IMAGE = "ItemImage";
    public static final String SERVICEPROVIDER_KEY_ROWID = "SvcProviderID";
    public static final String SERVICEPROVIDER_KEY_SERVICEID = "SvcID";
    public static final String SERVICEPROVIDER_KEY_PROVIDER = "SvcProviderName";
    public static final String SERVICEPROVIDER_KEY_URL = "URL";
    public static final String SERVICEPROVIDER_KEY_WEBSVC = "WebSvcName";
    public static final String ITEMKEY_KEY_ITEMID = "ItemID";
    public static final String SERVICEPROVIDER_KEY_KEYID = "KeyID";
    public static final String SERVICEPROVIDER_KEY_VALUE = "Value";
    public static final String KEYS_KEY_ROWID = "KeyID";
    public static final String KEYS_KEY_NAME = "KeyName";
    
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "lemur.db";
    private static final String DATABASE_TABLE_SERVICE = "Service";
    private static final String DATABASE_TABLE_USER = "User";
    private static final String DATABASE_TABLE_USERSERVICE = "UserService";
    private static final String DATABASE_TABLE_ITEM = "Item";
    private static final String DATABASE_TABLE_SERVICEPROVIDER = "ServiceProvider";
    private static final String DATABASE_TABLE_ITEMKEY = "ItemKey";
    private static final String DATABASE_TABLE_KEYS = "Keys";
    private static final int DATABASE_VERSION = 1;

   private final Context context;
   private SQLiteDatabase db;
   private DBHelper dbHelper;
   
   public DataHelper(Context context) {
      this.context = context;
      dbHelper = new DBHelper(this.context);
      
      this.open();
      
      Cursor cService = getAllServices();
      if (cService == null || cService.getCount() == 0) {
          insertService("Books", 0);
          insertService("DVDs", 0);
          insertService("Vinyl", 1);
      }
      
      this.close();
   }

   //---opens the database---
   public DataHelper open() throws SQLException
   {
	   this.db = dbHelper.getWritableDatabase();
	   return this;
   }
   
   //---closes the database--- 
   public void close()
   {
	   dbHelper.close();
   }
   
   //---insert a service into the database---
   public long insertService(String name, long paidsvc) 
   {
       ContentValues initialValues = new ContentValues();
       initialValues.put(SERVICE_KEY_NAME, name);
       initialValues.put(SERVICE_KEY_PAIDSVC, paidsvc);
       return db.insert(DATABASE_TABLE_SERVICE, null, initialValues);
   }

   //---deletes a particular Service---
   public boolean deleteService(long rowId) 
   {
       return db.delete(DATABASE_TABLE_SERVICE, SERVICE_KEY_ROWID + "=" + rowId, null) > 0;
   }

   //---retrieves all the services---
   public Cursor getAllServices() 
   {
       return db.query(DATABASE_TABLE_SERVICE, new String[] {
    		   SERVICE_KEY_ROWID, 
    		   SERVICE_KEY_NAME,
    		   SERVICE_KEY_PAIDSVC}, 
               null, 
               null, 
               null, 
               null, 
               null);
   }

   //---retrieves a particular service---
   public Cursor getService(long rowId) throws SQLException 
   {
       Cursor mCursor =
               db.query(true, DATABASE_TABLE_SERVICE, new String[] {
            		   SERVICE_KEY_ROWID, 
            		   SERVICE_KEY_NAME,
            		   SERVICE_KEY_PAIDSVC},
            		   SERVICE_KEY_ROWID + "=" + rowId, 
               		null,
               		null, 
               		null, 
               		null, 
               		null);
       if (mCursor != null) {
           mCursor.moveToFirst();
       }
       return mCursor;
   }

   //---updates a title---
   public boolean updateService(long rowId, String name, long PaidSvc) 
   {
       ContentValues args = new ContentValues();
       args.put(SERVICE_KEY_NAME, name);
       args.put(SERVICE_KEY_PAIDSVC, PaidSvc);
       return db.update(DATABASE_TABLE_SERVICE, args, 
    		   SERVICE_KEY_ROWID + "=" + rowId, null) > 0;
   }
   
   private static class DBHelper extends SQLiteOpenHelper
   {
      DBHelper(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

      @Override
      public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_SERVICE + "(SvcID INTEGER PRIMARY KEY, SvcName TEXT, PaidSvc INTEGER)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_USER + "(UserID INTEGER PRIMARY KEY, UserName TEXT, FirstName TEXT, Password TEXT)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_USERSERVICE + "(UserSvcID INTEGER , SvcID INTEGER, UserID INTEGER)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_ITEM + "(ItemID INTEGER PRIMARY KEY, UPC TEXT, ItemName TEXT, Created TEXT, CreatorID INTEGER, Modified TEXT, ModifierID INTEGER, ItemImage BLOB)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_SERVICEPROVIDER + "(SvcProviderID INTEGER PRIMARY KEY, SvcID INTEGER, SvcProviderName TEXT, URL TEXT, WebSvcName TEXT)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_ITEMKEY + "(ItemID INTEGER, KeyID INTEGER, Value TEXT)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_KEYS + "(KeyID INTEGER PRIMARY KEY, KeyName TEXT)");
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	  Log.w(TAG, "Upgrading database from version " + oldVersion 
                  + " to "
                  + newVersion + ", which will destroy all old data");
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SERVICE);
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_USER);
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_USERSERVICE);
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ITEM);
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SERVICEPROVIDER);
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ITEMKEY);
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KEYS);
         onCreate(db);
      }
   }
}