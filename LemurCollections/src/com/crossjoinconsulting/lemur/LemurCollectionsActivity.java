package com.crossjoinconsulting.lemur;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import android.widget.TextView;

public class LemurCollectionsActivity extends Activity 
{
	private TextView output;
	private DataHelper dh;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GridView gridview = (GridView) findViewById(R.id.gvImages);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() 
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
            {
                Toast.makeText(LemurCollectionsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
    	});

        this.output = (TextView) this.findViewById(R.id.out_text);
                
        this.dh = new DataHelper(this);
        dh.open();
        StringBuilder sb = new StringBuilder();
        
        Classes.Service[] svc = dh.getAllServices(0);
        Classes.User[] u = dh.getAllUsers(0);
        Classes.UserService[] us = dh.getAllUserServices(0);
        
        sb.append("Services" + "\n");
        try
        {
	        for(int i = 0; i < svc.length; i++)
	        {
	        	sb.append(svc[i].SvcID + " " + 
	        			  svc[i].SvcName + " " + 
	        			  svc[i].PaidSvc + "|");
	        }
        } catch (Exception e) {
		  sb.append(e.toString() + "\n"); 		  
	   	}
        
        sb.append("\n" + "Users" + "\n");
        Cursor cur = null;
        try
        {
        	for(int i = 0; i < u.length; i++)
            {
            	sb.append(u[i].UserID + " " + 
            			  u[i].UserName + " " + 
            			  u[i].FirstName + " " + 
            			  u[i].LastName + " " + 
            			  u[i].Password + "|");
            }
        } catch (Exception e) {
 		  sb.append(e.toString() + "\n"); 		  
 	   	}
        
        sb.append("\n" + "UserServices" + "\n");
        try
        {
	        for(int i = 0; i < us.length; i++)
	        {
	        	sb.append(us[i].UserSvcID + " " + 
	        			  us[i].SvcID + " " + 
	        			  us[i].UserID + "|");
	        }
        } catch (Exception e) {
		  sb.append(e.toString() + "\n"); 		  
	   	}
        
        sb.append("\n" + "Items" + "\n");
        try
        {
        cur = dh.getAllItem();
        if (cur != null)
        {
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            sb.append(cur.getString(1) + "\n");
            cur.moveToNext();
        }
        }
        cur.close();
        } catch (Exception e) {
   		  sb.append(e.toString() + "\n"); 		  
   	   	}
        
        sb.append("\n" + "ServiceProviders" + "\n");
        try
        {
        cur = dh.getAllServiceProviders();
        if (cur != null)
        {
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            sb.append(cur.getString(1) + "\n");
            cur.moveToNext();
        }
        }
        cur.close();
        } catch (Exception e) {
   		  sb.append(e.toString() + "\n"); 		  
   	   	}
        
        sb.append("\n" + "ItemKey" + "\n");
        try
        {
        cur = dh.getAllItemKey();
        if (cur != null)
        {
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            sb.append(cur.getString(1) + "\n");
            cur.moveToNext();
        }
        }
        cur.close();
        } catch (Exception e) {
   		  sb.append(e.toString() + "\n"); 		  
   	   	}
        
        sb.append("\n" + "Keys" + "\n");
        try
        {
        cur = dh.getAllKeys();
        if (cur != null)
        {
        cur.moveToFirst();
        while (cur.isAfterLast() == false) {
            sb.append(cur.getString(1) + "\n");
            cur.moveToNext();
        }
        }
        cur.close();
        } catch (Exception e) {
   		  sb.append(e.toString() + "\n"); 		  
   	   	}
        
        this.output.setText(sb.toString());
        
        dh.close();
    }
}