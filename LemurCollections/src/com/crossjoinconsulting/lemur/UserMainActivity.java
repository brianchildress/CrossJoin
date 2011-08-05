package com.crossjoinconsulting.lemur;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserMainActivity extends Activity implements OnClickListener
{
	private TextView tvUserName;
	private DataHelper dh;
	private Button btnScan;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	this.dh = new DataHelper(this);
        dh.open();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermain);
     
        this.btnScan = (Button)this.findViewById(R.id.btnSubmit);
        this.btnScan.setOnClickListener(this);
        
        this.tvUserName = (TextView)this.findViewById(R.id.tvUserName);
        
		//First Extract the bundle from intent
		Bundle bundle = getIntent().getExtras();
		
		//Next extract the values using the key as
		String username = bundle.getString("USERNAME");
		Classes.User u = dh.getUser(username);
		
		tvUserName.setText(u.FirstName + " " + u.LastName);
		dh.close();
    }
    
 // Implement the OnClickListener callback
    public void onClick(View v) {
    	IntentIntegrator.initiateScan(this); 
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
	    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
	    if (scanResult != null) {
	      // handle scan result
	    }
	    // else continue with any other code you need in the method
	 
	 }
}
