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
    public static final String ITEMSERVICEKEY_KEY_ITEMID = "ItemID";
    public static final String ITEMSERVICEKEY_KEY_SERVICEKEYID = "ServiceKeyID";
    public static final String ITEMSERVICEKEY_KEY_VALUE = "Value";
    public static final String SERVICEKEY_KEY_SERVICEKEYID = "ServiceKeyID";
    public static final String SERVICEKEY_KEY_SERVICEID = "ServiceID";    
    public static final String SERVICEKEY_KEY_KEYID = "KeyID";
    public static final String KEYS_KEY_ROWID = "KeyID";
    public static final String KEYS_KEY_NAME = "KeyName";
    
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "lemur.db";
    private static final String DATABASE_TABLE_SERVICE = "Service";
    private static final String DATABASE_TABLE_USER = "User";
    private static final String DATABASE_TABLE_USERSERVICE = "UserService";
    private static final String DATABASE_TABLE_ITEM = "Item";
    private static final String DATABASE_TABLE_SERVICEPROVIDER = "ServiceProvider";
    private static final String DATABASE_TABLE_ITEMSERVICEKEY = "ItemServiceKey";
    private static final String DATABASE_TABLE_SERVICEKEY = "ServiceKey";
    private static final String DATABASE_TABLE_KEYS = "Keys";
    private static final int DATABASE_VERSION = 1;

   private final Context context;
   private SQLiteDatabase db;
   private DBHelper dbHelper;
   
   public DataHelper(Context context) {
      this.context = context;
      dbHelper = new DBHelper(this.context);
      
      this.open();
      Classes.Service[] cService = getAllServices(0);
      if (cService == null || cService.length == 0) {
          insertService("Books", 0);
          insertService("DVDs", 0);
          insertService("Vinyl", 1);
          insertUser("jj@crossjoinconuslting.com", "JJ", "Jenkins", "pwd");
          insertUser("brian.p.childress@crossjoinconuslting.com", "Brian", "Childress", "pwd");
          insertUserService(1, 2);
          insertItem("0-345-38852-6", "Do we need an item name?", "06/27/2011", 2, "06/27/2011", 2, "");
          insertKey("Author");
          insertKey("Title");
          insertKey("Type");
          insertKey("Publisher");
          insertKey("PublishedDate");
          insertKey("Language");
          insertServiceKey(1, 1);
          insertServiceKey(1, 2);
          insertServiceKey(1, 3);
          insertServiceKey(1, 4);
          insertServiceKey(1, 5);
          insertServiceKey(1, 6);
          insertItemServiceKey(1, "Harry Turtledove");
          insertItemServiceKey(1, "WORLDWAR: IN THE BALANCE");
          insertItemServiceKey(1, "Paperback");
          insertItemServiceKey(1, "Ballantine Books");
          insertItemServiceKey(1, "11/04/1997");
          insertItemServiceKey(1, "English");
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
   private Cursor _getAllServices() 
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
   public Classes.Service[] getAllServices(long rowId)
   {
	   Classes c = new Classes();
	   Classes.Service[] loSvc = null;
	   Cursor cur;
	   if (rowId == 0)
		   cur = this._getAllServices();
	   else
		   cur = this._getService(rowId);
	   try
       {
		   if (cur != null)
		   {
			   loSvc = new Classes.Service[cur.getCount()];
			   cur.moveToFirst();
			   Integer i = 0;
			   while (cur.isAfterLast() == false) {
				   Classes.Service svc = c.new Service();
				   svc.SvcID = cur.getInt(0);				   
				   svc.SvcName = cur.getString(1);
				   svc.PaidSvc = cur.getInt(2);
				   loSvc[i] = svc;
				   i += 1;
				   cur.moveToNext();
			   }
		   }
		   cur.close();		   
       	} catch (Exception e) {}
       	return loSvc;
   }
   //---retrieves a particular service---
   private Cursor _getService(long rowId) throws SQLException 
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

 //---insert a service key into the database---
   public long insertServiceKey(long serviceid, long keyid) 
   {
       ContentValues initialValues = new ContentValues();
       initialValues.put(SERVICEKEY_KEY_KEYID, keyid);
       initialValues.put(SERVICEKEY_KEY_SERVICEID, serviceid);
       return db.insert(DATABASE_TABLE_SERVICEKEY, null, initialValues);
   }
   //---deletes a particular ServiceKey---
   public boolean deleteServiceKey(long rowId) 
   {
       return db.delete(DATABASE_TABLE_SERVICEKEY, SERVICEKEY_KEY_SERVICEKEYID + "=" + rowId, null) > 0;
   }
 //---retrieves all the service keys---
   private Cursor _getAllServiceKeys() 
   {
       return db.query(DATABASE_TABLE_SERVICEKEY, new String[] {
    		   SERVICEKEY_KEY_SERVICEKEYID, 
    		   SERVICEKEY_KEY_SERVICEID,
    		   SERVICEKEY_KEY_KEYID}, 
               null, 
               null, 
               null, 
               null, 
               null);
   }
   public Classes.ServiceKey[] getAllServiceKeys(long rowId)
   {
	   Classes c = new Classes();
	   Classes.ServiceKey[] loServiceKey = null;
	   Cursor cur;
	   if (rowId == 0)
		   cur = this._getAllServiceKeys();
	   else
		   cur = this._getServiceKey(rowId);
	   try
       {
		   if (cur != null)
		   {
			   loServiceKey = new Classes.ServiceKey[cur.getCount()];
			   cur.moveToFirst();
			   Integer i = 0;
			   while (cur.isAfterLast() == false) {
				   Classes.ServiceKey k = c.new ServiceKey();
				   k.ServiceKeyID = cur.getInt(0);
				   k.ServiceID = cur.getInt(1);
				   k.KeyID = cur.getInt(2);
				   loServiceKey[i] = k;
				   i += 1;
				   cur.moveToNext();
			   }
		   }
		   cur.close();		   
       	} catch (Exception e) {}
       	return loServiceKey;
   }
   //---retrieves a particular service key---
   private Cursor _getServiceKey(long rowId) throws SQLException 
   {
       Cursor mCursor =
               db.query(true, DATABASE_TABLE_SERVICEKEY, new String[] {
            		   SERVICEKEY_KEY_SERVICEKEYID, 
            		   SERVICEKEY_KEY_SERVICEID,
            		   SERVICEKEY_KEY_KEYID},
            		   SERVICEKEY_KEY_SERVICEKEYID + "=" + rowId, 
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
 //---updates a service key---
   public boolean updateServiceKey(long rowId, long serviceid, long keyid) 
   {
       ContentValues args = new ContentValues();
       args.put(SERVICEKEY_KEY_SERVICEID, serviceid);
       args.put(SERVICEKEY_KEY_KEYID, keyid);
       return db.update(DATABASE_TABLE_SERVICEKEY, args, 
    		   SERVICEKEY_KEY_SERVICEKEYID + "=" + rowId, null) > 0;
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
   private Cursor _getAllKeys() 
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
   public Classes.Key[] getAllKeys(long rowId)
   {
	   Classes c = new Classes();
	   Classes.Key[] loKey = null;
	   Cursor cur;
	   if (rowId == 0)
		   cur = this._getAllKeys();
	   else
		   cur = this._getKey(rowId);
	   try
       {
		   if (cur != null)
		   {
			   loKey = new Classes.Key[cur.getCount()];
			   cur.moveToFirst();
			   Integer i = 0;
			   while (cur.isAfterLast() == false) {
				   Classes.Key k = c.new Key();
				   k.KeyID = cur.getInt(0);
				   k.KeyName = cur.getString(1);
				   loKey[i] = k;
				   i += 1;
				   cur.moveToNext();
			   }
		   }
		   cur.close();		   
       	} catch (Exception e) {}
       	return loKey;
   }
   //---retrieves a particular key---
   private Cursor _getKey(long rowId) throws SQLException 
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
   
   //---insert an item service key into the database---
   public long insertItemServiceKey(long ServiceKeyID, String value) 
   {
       ContentValues initialValues = new ContentValues();
       initialValues.put(ITEMSERVICEKEY_KEY_SERVICEKEYID, ServiceKeyID);
       initialValues.put(ITEMSERVICEKEY_KEY_VALUE, value);
       return db.insert(DATABASE_TABLE_ITEMSERVICEKEY, null, initialValues);
   }
   //---deletes a particular item service key---
   public boolean deleteItemServiceKey(long rowId) 
   {
       return db.delete(DATABASE_TABLE_ITEMSERVICEKEY, ITEMSERVICEKEY_KEY_ITEMID + "=" + rowId, null) > 0;
   }
   //---retrieves all the services---
   private Cursor _getAllItemServiceKey() 
   {
       return db.query(DATABASE_TABLE_ITEMSERVICEKEY, new String[] {
    		   ITEMSERVICEKEY_KEY_ITEMID, 
    		   ITEMSERVICEKEY_KEY_SERVICEKEYID,
    		   ITEMSERVICEKEY_KEY_VALUE}, 
               null, 
               null, 
               null, 
               null, 
               null);
   }
   public Classes.ItemServiceKey[] getAllItemServiceKey(long rowId)
   {
	   Classes c = new Classes();
	   Classes.ItemServiceKey[] loItemKey = null;
	   Cursor cur;
	   if (rowId == 0)
		   cur = this._getAllItemServiceKey();
	   else
		   cur = this._getItemServiceKey(rowId);
	   try
       {
		   if (cur != null)
		   {
			   loItemKey = new Classes.ItemServiceKey[cur.getCount()];
			   cur.moveToFirst();
			   Integer i = 0;
			   while (cur.isAfterLast() == false) {
				   Classes.ItemServiceKey ik = c.new ItemServiceKey();
				   ik.ItemID = cur.getInt(0);				   
				   ik.ServiceKeyID = cur.getInt(1);
				   ik.Value = cur.getString(2);
				   loItemKey[i] = ik;
				   i += 1;
				   cur.moveToNext();
			   }
		   }
		   cur.close();		   
       	} catch (Exception e) {}
       	return loItemKey;
   }
   //---retrieves a particular item key---
   private Cursor _getItemServiceKey(long rowId) throws SQLException 
   {
       Cursor mCursor =
               db.query(true, DATABASE_TABLE_ITEMSERVICEKEY, new String[] {
            		   ITEMSERVICEKEY_KEY_ITEMID, 
            		   ITEMSERVICEKEY_KEY_SERVICEKEYID,
            		   ITEMSERVICEKEY_KEY_VALUE}, 
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
   public boolean updateItemKey(long rowId, long ServiceKeyID, String value) 
   {
       ContentValues args = new ContentValues();
       args.put(ITEMSERVICEKEY_KEY_SERVICEKEYID, ServiceKeyID);
       args.put(ITEMSERVICEKEY_KEY_VALUE, value);
       return db.update(DATABASE_TABLE_ITEMSERVICEKEY, args, 
    		   ITEMSERVICEKEY_KEY_ITEMID + "=" + rowId, null) > 0;
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
   private Cursor _getAllServiceProviders() 
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
   public Classes.ServiceProvider[] getAllServiceProviders(long rowId)
   {
	   Classes c = new Classes();
	   Classes.ServiceProvider[] loServiceProvider = null;
	   Cursor cur;
	   if (rowId == 0)
		   cur = this._getAllServiceProviders();
	   else
		   cur = this._getServiceProvider(rowId);
	   try
       {
		   if (cur != null)
		   {
			   loServiceProvider = new Classes.ServiceProvider[cur.getCount()];
			   cur.moveToFirst();
			   Integer i = 0;
			   while (cur.isAfterLast() == false) {
				   Classes.ServiceProvider u = c.new ServiceProvider();
				   u.SvcProviderID = cur.getInt(0);				   
				   u.SvcID = cur.getInt(1);
				   u.SvcProvider = cur.getString(2);
				   u.URL = cur.getString(3);
				   u.WebSvcName = cur.getString(4);
				   loServiceProvider[i] = u;
				   i += 1;
				   cur.moveToNext();
			   }
		   }
		   cur.close();		   
       	} catch (Exception e) {}
       	return loServiceProvider;
   }
   //---retrieves a particular service provider---
   private Cursor _getServiceProvider(long rowId) throws SQLException 
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
   private Cursor _getAllItem() 
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
   public Classes.Item[] getAllItem(long rowId)
   {
	   Classes c = new Classes();
	   Classes.Item[] loItem = null;
	   Cursor cur;
	   if (rowId == 0)
		   cur = this._getAllItem();
	   else
		   cur = this._getItem(rowId);
	   try
       {
		   if (cur != null)
		   {
			   loItem = new Classes.Item[cur.getCount()];
			   cur.moveToFirst();
			   Integer i = 0;
			   while (cur.isAfterLast() == false) {
				   Classes.Item item = c.new Item();
				   item.ItemID = cur.getInt(0);				   
				   item.UPC = cur.getString(1);
				   item.ItemName = cur.getString(2);
				   item.Created = cur.getString(3);				   
				   item.CreatorID = cur.getString(4);
				   item.Modified = cur.getString(5);
				   item.ModifierID = cur.getString(6);
				   item.ItemImage = cur.getString(7);
				   loItem[i] = item;
				   i += 1;
				   cur.moveToNext();
			   }
		   }
		   cur.close();		   
       	} catch (Exception e) {}
       	return loItem;
   }
   //---retrieves a particular item---
   private Cursor _getItem(long rowId) throws SQLException 
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
   public Cursor _getAllUserServices() 
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
   public Classes.UserService[] getAllUserServices(long rowId)
   {
	   Classes c = new Classes();
	   Classes.UserService[] loUserService = null;
	   Cursor cur;
	   if (rowId == 0)
		   cur = this._getAllUserServices();
	   else
		   cur = this._getUserService(rowId);
	   try
       {
		   if (cur != null)
		   {
			   loUserService = new Classes.UserService[cur.getCount()];
			   cur.moveToFirst();
			   Integer i = 0;
			   while (cur.isAfterLast() == false) {
				   Classes.UserService u = c.new UserService();
				   u.UserSvcID = cur.getInt(0);				   
				   u.SvcID = cur.getInt(1);
				   u.UserID = cur.getInt(2);
				   loUserService[i] = u;
				   i += 1;
				   cur.moveToNext();
			   }
		   }
		   cur.close();		   
       	} catch (Exception e) {}
       	return loUserService;
   }
   //---retrieves a particular user service---
   public Cursor _getUserService(long rowId) throws SQLException 
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
   private Cursor _getAllUsers() 
   {
	   return db.query(DATABASE_TABLE_USER, new String[] {
			   USER_KEY_ROWID, 
			   USER_KEY_NAME,
			   USER_KEY_FIRSTNAME,
			   USER_KEY_LASTNAME,
			   USER_KEY_PWD}, 
	           null, 
	           null, 
	           null, 
	           null, 
	           null);
   }
   public Classes.User[] getAllUsers(long rowId)
   {
	   Classes c = new Classes();
	   Classes.User[] loUser = null;
	   Cursor cur;
	   if (rowId == 0)
		   cur = this._getAllUsers();
	   else
		   cur = this._getUser(rowId);
	   try
       {
		   if (cur != null)
		   {
			   loUser = new Classes.User[cur.getCount()];
			   cur.moveToFirst();
			   Integer i = 0;
			   while (cur.isAfterLast() == false) {
				   Classes.User u = c.new User();
				   u.UserID = cur.getInt(0);				   
				   u.UserName = cur.getString(1);
				   u.FirstName = cur.getString(2);
				   u.LastName = cur.getString(3);
				   u.Password = SimpleCrypto.decrypt("LemurCJC", cur.getString(4));
				   loUser[i] = u;
				   i += 1;
				   cur.moveToNext();
			   }
		   }
		   cur.close();		   
       	} catch (Exception e) {}
       	return loUser;
   }
   public Classes.User getUser(String UserName)
   {
	   Classes c = new Classes();
	   Classes.User user = null;
	   Cursor cur = this._getUser(UserName);
	   try
       {
		   if (cur != null)
		   {
			   cur.moveToFirst();
			   Integer i = 0;
			   while (cur.isAfterLast() == false) {
				   Classes.User u = c.new User();
				   u.UserID = cur.getInt(0);				   
				   u.UserName = cur.getString(1);
				   u.FirstName = cur.getString(2);
				   u.LastName = cur.getString(3);
				   u.Password = SimpleCrypto.decrypt("LemurCJC", cur.getString(4));
				   user = u;
				   i += 1;
				   cur.moveToNext();
			   }
		   }
		   cur.close();		   
       	} catch (Exception e) {}
       	return user;
   }
   //---retrieves a particular user---
   private Cursor _getUser(long rowId) throws SQLException 
   {
	   Cursor mCursor = db.query(true, DATABASE_TABLE_USER, new String[] {
			   USER_KEY_ROWID, 
			   USER_KEY_NAME,
			   USER_KEY_FIRSTNAME,
			   USER_KEY_LASTNAME,
			   USER_KEY_PWD}, 
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
   private Cursor _getUser(String UserName) throws SQLException 
   {
	   Cursor mCursor = db.query(true, DATABASE_TABLE_USER, new String[] {
			   USER_KEY_ROWID, 
			   USER_KEY_NAME,
			   USER_KEY_FIRSTNAME,
			   USER_KEY_LASTNAME,
			   USER_KEY_PWD}, 
			   USER_KEY_NAME + "=" + "?", 
       		new String[]{UserName},
       		null, 
       		null, 
       		null, 
       		null);
	   if (mCursor != null) {
		   mCursor.moveToFirst();
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
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_USER + "(UserID INTEGER PRIMARY KEY, UserName TEXT, FirstName TEXT, LastName TEXT, Password TEXT)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_USERSERVICE + "(UserSvcID INTEGER , SvcID INTEGER, UserID INTEGER)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_ITEM + "(ItemID INTEGER PRIMARY KEY, UPC TEXT, ItemName TEXT, Created TEXT, CreatorID INTEGER, Modified TEXT, ModifierID INTEGER, ItemImage BLOB)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_SERVICEPROVIDER + "(SvcProviderID INTEGER PRIMARY KEY, SvcID INTEGER, SvcProviderName TEXT, URL TEXT, WebSvcName TEXT)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_ITEMSERVICEKEY + "(ItemID INTEGER, ServiceKeyID INTEGER, Value TEXT)");
         db.execSQL("CREATE TABLE " + DATABASE_TABLE_SERVICEKEY + "(ServiceKeyID INTEGER, KeyID INTEGER, ServiceID TEXT)");
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
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ITEMSERVICEKEY);
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SERVICEKEY);
         db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KEYS);
         onCreate(db);
      }
   }
}