package com.crossjoinconsulting.lemur;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnClickListener 
{
	private EditText etEmail;
	private EditText etPassword;
	private Button btnLogin;
	private DataHelper dh;
	private TextView tvError;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        this.btnLogin = (Button)this.findViewById(R.id.btnSubmit);
        this.btnLogin.setOnClickListener(this);
    }
    
	// Implement the OnClickListener callback
    public void onClick(View v) {
    	// do something when the button is clicked
    	this.dh = new DataHelper(this);
        dh.open();
        
        this.etEmail = (EditText)this.findViewById(R.id.etEmail);
        this.etPassword = (EditText)this.findViewById(R.id.etPassword);
        
        Classes.User u = dh.getUser(etEmail.getText().toString());
    	
        if (u.UserName == etPassword.getText().toString())
        {
	    	Intent intent = new Intent(LoginActivity.this,LemurCollectionsActivity.class);
	
	    	//Next create the bundle and initialize it
	    	Bundle bundle = new Bundle();
	    	//Add the parameters to bundle as 
	    	bundle.putString("USERNAME", etEmail.toString());
	    	//Add this bundle to the intent
	    	intent.putExtras(bundle);
	    	
	    	//Start next activity
	    	LoginActivity.this.startActivity(intent); 
        }
        else
        {
        	tvError.setVisibility(0);
        }    	
    }
}
