package com.crossjoinconsulting.lemur;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import android.widget.TextView;

public class LemurCollectionsActivity extends Activity implements OnClickListener 
{
	private TextView output;
	private DataHelper dh;
	private Button btnLogin;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.btnLogin = (Button)this.findViewById(R.id.btnSubmit);
        this.btnLogin.setOnClickListener(this);
        
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
        Classes.Item[] item = dh.getAllItem(0);
        Classes.ServiceProvider[] sp = dh.getAllServiceProviders(0);
        Classes.ItemServiceKey[] ik = dh.getAllItemServiceKey(0);
        Classes.ServiceKey[] sk = dh.getAllServiceKeys(0);
        Classes.Key[] k = dh.getAllKeys(0);
        
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
	        for(int i = 0; i < item.length; i++)
	        {
	        	sb.append(item[i].ItemID + " " +
	        			  item[i].UPC + " " +
	        			  item[i].ItemName + " " +
	        			  item[i].Created + " " +
	        			  item[i].CreatorID + " " +
	        			  item[i].Modified + " " +
	        			  item[i].ModifierID + " " +
	        			  item[i].ItemImage + "|");
	        }
        } catch (Exception e) {
		  sb.append(e.toString() + "\n"); 		  
	   	}
        
        sb.append("\n" + "ServiceProviders" + "\n");
        try
        {
	        for(int i = 0; i < sp.length; i++)
	        {
	        	sb.append(sp[i].SvcProviderID + " " + 
	        			  sp[i].SvcID + " " +
	        			  sp[i].SvcProvider + " " +
	        			  sp[i].URL + " " +
	        			  sp[i].WebSvcName + "|");
	        }
        } catch (Exception e) {
		  sb.append(e.toString() + "\n"); 		  
	   	}
        
        sb.append("\n" + "ItemServiceKey" + "\n");
        try
        {
	        for(int i = 0; i < ik.length; i++)
	        {
	        	sb.append(ik[i].ItemID + " " + 
	        			  ik[i].ServiceKeyID + " " +
	        			  ik[i].Value + "|");
	        }
        } catch (Exception e) {
		  sb.append(e.toString() + "\n"); 		  
	   	}
        
        sb.append("\n" + "ServiceKey" + "\n");
        try
        {
	        for(int i = 0; i < sk.length; i++)
	        {
	        	sb.append(sk[i].ServiceKeyID + " " + 
	        			  sk[i].ServiceID + " " +
	        			  sk[i].KeyID + "|");
	        }
        } catch (Exception e) {
		  sb.append(e.toString() + "\n"); 		  
	   	}
        
        sb.append("\n" + "Keys" + "\n");
        try
        {
	        for(int i = 0; i < k.length; i++)
	        {
	        	sb.append(k[i].KeyID + " " + 
	        			  k[i].KeyName + "|");
	        }
        } catch (Exception e) {
		  sb.append(e.toString() + "\n"); 		  
	   	}
        
        this.output.setText(sb.toString());
        
        dh.close();
    }
    
    // Implement the OnClickListener callback
    public void onClick(View v) {
    	// do something when the button is clicked
    	Intent intent = new Intent(LemurCollectionsActivity.this,LoginActivity.class);

    	//Start next activity
    	LemurCollectionsActivity.this.startActivity(intent); 
    }
	
}