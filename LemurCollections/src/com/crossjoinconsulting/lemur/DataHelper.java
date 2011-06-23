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
    public static final String ITEMKEY_KEY_KEYID = "KeyID";
    public static final String ITEMKEY_KEY_VALUE = "Value";
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
   //---updates a service---
   public boolean updateService(long rowId, String name, long PaidSvc) 
   {
       ContentValues args = new ContentValues();
       args.put(SERVICE_KEY_NAME, name);
       args.put(SERVICE_KEY_PAIDSVC, PaidSvc);
       return db.update(DATABASE_TABLE_SERVICE, args, 
    		   SERVICE_KEY_ROWID + "=" + rowId, null) > 0;
   }

   //---insert a key into the database---
   public long insertKey(String name) 
   {
       ContentValues initialValues = new ContentValues();
       initialValues.put(KEYS_KEY_NAME, name);
       return db.insert(DATABASE_TABLE_KEYS, null, initialValues);
   }
   //---deletes a particular Key---
   public boolean deleteKey(long rowId) 
   {
       return db.delete(DATABASE_TABLE_KEYS, KEYS_KEY_ROWID + "=" + rowId, null) > 0;
   }
 //---retrieves all the keys---
   public Cursor getAllKeys() 
   {
       return db.query(DATABASE_TABLE_KEYS, new String[] {
    		   KEYS_KEY_ROWID, 
    		   KEYS_KEY_NAME}, 
               null, 
               null, 
               null, 
               null, 
               null);
   }
   //---retrieves a particular key---
   public Cursor getKey(long rowId) throws SQLException 
   {
       Cursor mCursor =
               db.query(true, DATABASE_TABLE_KEYS, new String[] {
            		   KEYS_KEY_ROWID, 
            		   KEYS_KEY_NAME},
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
 //---updates a key---
   public boolean updateKey(long rowId, String name) 
   {
       ContentValues args = new ContentValues();
       args.put(KEYS_KEY_NAME, name);
       return db.update(DATABASE_TABLE_KEYS, args, 
    		   KEYS_KEY_ROWID + "=" + rowId, null) > 0;
   }
   
   //---insert an item key into the database---
   public long insertItemKey(long KeyID, String value) 
   {
       ContentValues initialValues = new ContentValues();
       initialValues.put(ITEMKEY_KEY_KEYID, KeyID);
       initialValues.put(ITEMKEY_KEY_VALUE, value);
       return db.insert(DATABASE_TABLE_ITEMKEY, null, initialValues);
   }
   //---deletes a particular Service---
   public boolean deleteItemKey(long rowId) 
   {
       return db.delete(DATABASE_TABLE_ITEMKEY, ITEMKEY_KEY_ITEMID + "=" + rowId, null) > 0;
   }
   //---retrieves all the services---
   public Cursor getAllItemKey() 
   {
       return db.query(DATABASE_TABLE_SERVICE, new String[] {
    		   ITEMKEY_KEY_ITEMID, 
    		   ITEMKEY_KEY_KEYID,
    		   ITEMKEY_KEY_VALUE}, 
               null, 
               null, 
               null, 
               null, 
               null);
   }
   //---retrieves a particular item key---
   public Cursor getItemKey(long rowId) throws SQLException 
   {
       Cursor mCursor =
               db.query(true, DATABASE_TABLE_SERVICE, new String[] {
            		   ITEMKEY_KEY_ITEMID, 
            		   ITEMKEY_KEY_KEYID,
            		   ITEMKEY_KEY_VALUE}, 
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
   //---updates an item key---
   public boolean updateItemKey(long rowId, long KeyID, String value) 
   {
       ContentValues args = new ContentValues();
       args.put(ITEMKEY_KEY_KEYID, KeyID);
       args.put(ITEMKEY_KEY_VALUE, value);
       return db.update(DATABASE_TABLE_ITEMKEY, args, 
    		   ITEMKEY_KEY_ITEMID + "=" + rowId, null) > 0;
   }
   
   //---insert a service provider into the database---
   public long insertServiceProvider(long svcid, String ProviderName, String URL, String WebSvcName) 
   {
       ContentValues initialValues = new ContentValues();
       initialValues.put(SERVICEPROVIDER_KEY_SERVICEID, svcid);
       initialValues.put(SERVICEPROVIDER_KEY_PROVIDER, ProviderName);
       initialValues.put(SERVICEPROVIDER_KEY_URL, URL);
       initialValues.put(SERVICEPROVIDER_KEY_WEBSVC, WebSvcName);
       return db.insert(DATABASE_TABLE_SERVICEPROVIDER, null, initialValues);
   }
   //---deletes a particular Service Provider---
   public boolean deleteServiceProvider(long rowId) 
   {
       return db.delete(DATABASE_TABLE_SERVICEPROVIDER, SERVICEPROVIDER_KEY_ROWID + "=" + rowId, null) > 0;
   }
   //---retrieves all the service providers---
   public Cursor getAllServiceProviders() 
   {
       return db.query(DATABASE_TABLE_SERVICEPROVIDER, new String[] {
    		   SERVICEPROVIDER_KEY_ROWID, 
    		   SERVICEPROVIDER_KEY_SERVICEID,
    		   SERVICEPROVIDER_KEY_PROVIDER,
    		   SERVICEPROVIDER_KEY_URL,
    		   SERVICEPROVIDER_KEY_WEBSVC}, 
               null, 
               null, 
               null, 
               null, 
               null);
   }
   //---retrieves a particular service provider---
   public Cursor getServiceProvider(long rowId) throws SQLException 
   {
       Cursor mCursor =
               db.query(true, DATABASE_TABLE_SERVICEPROVIDER, new String[] {
            		   SERVICEPROVIDER_KEY_ROWID, 
            		   SERVICEPROVIDER_KEY_SERVICEID,
            		   SERVICEPROVIDER_KEY_PROVIDER,
            		   SERVICEPROVIDER_KEY_URL,
            		   SERVICEPROVIDER_KEY_WEBSVC}, 
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
   //---updates a service---
   public boolean updateServiceProvider(long rowId, long svcid, String ProviderName, String URL, String WebSvcName) 
   {
       ContentValues args = new ContentValues();
       args.put(SERVICE_KEY_NAME, svcid);
       args.put(SERVICE_KEY_PAIDSVC, ProviderName);
       args.put(SERVICE_KEY_NAME, URL);
       args.put(SERVICE_KEY_PAIDSVC, WebSvcName);
       return db.update(DATABASE_TABLE_SERVICEPROVIDER, args, 
    		   SERVICEPROVIDER_KEY_ROWID + "=" + rowId, null) > 0;
   }
   
   //---insert a item into the database---
   public long insertItem(String UPC, String ItemName, String Created, long CreatorID, String Modifided, long ModifierID, String ItemImage) 
   {
       ContentValues initialValues = new ContentValues();
       initialValues.put(ITEM_KEY_UPC, UPC);
       initialValues.put(ITEM_KEY_NAME, ItemName);
       initialValues.put(ITEM_KEY_CREATED, Created);
       initialValues.put(ITEM_KEY_CREATOR, CreatorID);
       initialValues.put(ITEM_KEY_MODIFIED, Modifided);
       initialValues.put(ITEM_KEY_MODIFIER, ModifierID);
       initialValues.put(ITEM_KEY_IMAGE, ItemImage);
       return db.insert(DATABASE_TABLE_ITEM, null, initialValues);
   }
   //---deletes a particular Service---
   public boolean deleteItem(long rowId) 
   {
       return db.delete(DATABASE_TABLE_ITEM, ITEM_KEY_ROWID + "=" + rowId, null) > 0;
   }
   //---retrieves all the items---
   public Cursor getAllItem() 
   {
       return db.query(DATABASE_TABLE_ITEM, new String[] {
    		   ITEM_KEY_ROWID, 
    		   ITEM_KEY_UPC,
    		   ITEM_KEY_NAME,
    		   ITEM_KEY_CREATED, 
    		   ITEM_KEY_CREATOR,
    		   ITEM_KEY_MODIFIED,
    		   ITEM_KEY_MODIFIER, 
    		   ITEM_KEY_IMAGE}, 
               null, 
               null, 
               null, 
               null, 
               null);
   }  
   //---retrieves a particular item---
   public Cursor getItem(long rowId) throws SQLException 
   {
       Cursor mCursor =
               db.query(true, DATABASE_TABLE_ITEM, new String[] {
            		   ITEM_KEY_ROWID, 
            		   ITEM_KEY_UPC,
            		   ITEM_KEY_NAME,
            		   ITEM_KEY_CREATED, 
            		   ITEM_KEY_CREATOR,
            		   ITEM_KEY_MODIFIED,
            		   ITEM_KEY_MODIFIER, 
            		   ITEM_KEY_IMAGE},
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
 //---updates an item---
   public boolean updateItem(long rowId, String UPC, String ItemName, String Created, long CreatorID, String Modifided, long ModifierID, String ItemImage) 
   {
       ContentValues args = new ContentValues();
       args.put(ITEM_KEY_UPC, UPC);
       args.put(ITEM_KEY_NAME, ItemName);
       args.put(ITEM_KEY_CREATED, Created);
       args.put(ITEM_KEY_CREATOR, CreatorID);
       args.put(ITEM_KEY_MODIFIED, Modifided);
       args.put(ITEM_KEY_MODIFIER, ModifierID);
       args.put(ITEM_KEY_IMAGE, ItemImage);
       return db.update(DATABASE_TABLE_ITEM, args, 
    		   ITEM_KEY_ROWID + "=" + rowId, null) > 0;
   }
   
   //---insert a user service into the database---
   public long insertUserService(long SvcID, long UserID) 
   {
       ContentValues initialValues = new ContentValues();
       initialValues.put(USERSERVICE_KEY_SVCID, SvcID);
       initialValues.put(USERSERVICE_KEY_USERID, UserID);
       return db.insert(DATABASE_TABLE_USERSERVICE, null, initialValues);
   }
   //---deletes a particular User Service---
   public boolean deleteUserService(long rowId) 
   {
       return db.delete(DATABASE_TABLE_USERSERVICE, USERSERVICE_KEY_ROWID + "=" + rowId, null) > 0;
   }
   //---retrieves all the user services---
   public Cursor getAllUserServices() 
   {
       return db.query(DATABASE_TABLE_USERSERVICE, new String[] {
    		   USERSERVICE_KEY_ROWID, 
    		   USERSERVICE_KEY_SVCID,
    		   USERSERVICE_KEY_USERID}, 
               null, 
               null, 
               null, 
               null, 
               null);
   }
   //---retrieves a particular user service---
   public Cursor getUserService(long rowId) throws SQLException 
   {
       Cursor mCursor =
               db.query(true, DATABASE_TABLE_USERSERVICE, new String[] {
            		    USERSERVICE_KEY_ROWID, 
            		    USERSERVICE_KEY_SVCID,
            		    USERSERVICE_KEY_USERID},
               			USERSERVICE_KEY_ROWID + "=" + rowId, 
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
   //---updates a service---
   public boolean updateUserService(long rowId, long svcid, long userid) 
   {
       ContentValues args = new ContentValues();
       args.put(USERSERVICE_KEY_SVCID, svcid);
       args.put(USERSERVICE_KEY_USERID, userid);
       return db.update(DATABASE_TABLE_USERSERVICE, args, 
    		   USERSERVICE_KEY_ROWID + "=" + rowId, null) > 0;
   }
   
   //---insert a user into the database---
   public long insertUser(String username, String firstname, String lastname, String password) 
   {
	   String encrypedPassword = password;
	   try {
		   encrypedPassword = SimpleCrypto.encrypt("LemurCJC", password);
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
       ContentValues initialValues = new ContentValues();
       initialValues.put(USER_KEY_NAME, username);
       initialValues.put(USER_KEY_FIRSTNAME, firstname);
       initialValues.put(USER_KEY_LASTNAME, lastname);
       initialValues.put(USER_KEY_PWD, encrypedPassword);
       return db.insert(DATABASE_TABLE_USER, null, initialValues);
   }
   //---deletes a particular User---
   public boolean deleteUser(long rowId) 
   {
       return db.delete(DATABASE_TABLE_USER, USER_KEY_ROWID + "=" + rowId, null) > 0;
   }
   //---retrieves all the users---
   public Cursor getAllUsers() 
   {
	   try {
		   return db.query(DATABASE_TABLE_USER, new String[] {
				   USER_KEY_ROWID, 
				   USER_KEY_NAME,
				   USER_KEY_FIRSTNAME,
				   USER_KEY_LASTNAME,
				   SimpleCrypto.decrypt("LemurCJC", USER_KEY_PWD)}, 
		           null, 
		           null, 
		           null, 
		           null, 
		           null);
       	} catch (Exception e) {
       		e.printStackTrace();
       		return null;
       	}
   }
   //---retrieves a particular user---
   public Cursor getUser(long rowId) throws SQLException 
   {
	   Cursor mCursor = null;
	   try
	   {
		   mCursor =
               db.query(true, DATABASE_TABLE_USER, new String[] {
    				   USER_KEY_ROWID, 
    				   USER_KEY_NAME,
    				   USER_KEY_FIRSTNAME,
    				   USER_KEY_LASTNAME,
    				   SimpleCrypto.decrypt("LemurCJC", USER_KEY_PWD)}, 
            		   SERVICE_KEY_ROWID + "=" + rowId, 
               		null,
               		null, 
               		null, 
               		null, 
               		null);
		   if (mCursor != null) {
			   mCursor.moveToFirst();
       }
	   } catch (Exception e) {
      		e.printStackTrace();
	   }
       return mCursor;
   }
   //---updates a user---
   public boolean updateUser(long rowId, String username, String firstname, String lastname, String password) 
   {
       ContentValues args = new ContentValues();
       String encrypedPassword = password;
	   try {
		   encrypedPassword = SimpleCrypto.encrypt("LemurCJC", password);
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
       args.put(USER_KEY_NAME, username);
       args.put(USER_KEY_FIRSTNAME, firstname);
       args.put(USER_KEY_LASTNAME, lastname);
       args.put(USER_KEY_PWD, encrypedPassword);
       return db.update(DATABASE_TABLE_USER, args, 
    		   USER_KEY_ROWID + "=" + rowId, null) > 0;
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