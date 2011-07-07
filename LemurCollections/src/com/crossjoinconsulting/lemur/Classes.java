package com.crossjoinconsulting.lemur;

// Lemur Classes
public class Classes
{
	public class Service {
		public Service(){}
		public long SvcID;
		public String SvcName;
		public long PaidSvc;
	}
	public class User{
		public long UserID;
		public String UserName;
		public String FirstName;
		public String LastName;
		public String Password;
	}
	
	public class UserService{
		public long UserSvcID;
		public long SvcID;
		public long UserID;
	}
	
	public class Item{
		public long ItemID;
		public String UPC;
		public String ItemName;
		public String Created;
		public String CreatorID;
		public String Modified;
		public String ModifierID;
		public String ItemImage;
	}
	
	public class ServiceProvider{
		public long SvcProviderID;
		public long SvcID;
		public String SvcProvider;
		public String URL;
		public String WebSvcName;		
	}
	
	public class ItemServiceKey{
		public long ItemID;
		public long ServiceKeyID;
		public String Value;
	}
	
	public class ServiceKey{
		public long ServiceKeyID;
		public long KeyID;
		public long ServiceID;
	}
	
	public class Key{
		public long KeyID;
		public String KeyName;
	}
}