package com.crossjoinconsulting.lemur;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class UserMainActivity extends Activity 
{
	private TextView tvUserName;
	private DataHelper dh;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.dh = new DataHelper(this);
        dh.open();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermain);
        
        this.tvUserName = (TextView)this.findViewById(R.id.tvUserName);
        //this.tvUserName.setOnClickListener(this);
        
		//First Extract the bundle from intent
		Bundle bundle = getIntent().getExtras();
		
		//Next extract the values using the key as
		String username = bundle.getString("USERNAME");
		Classes.User u = dh.getUser(username);
		
		tvUserName.setText(u.FirstName + " " + u.LastName);
		dh.close();
    }
}
