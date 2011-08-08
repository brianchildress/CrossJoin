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
        
        this.etEmail = (EditText)this.findViewById(R.id.etEmail);
        etEmail.setText("brian.p.childress@crossjoinconsulting.com");
    }
    
	// Implement the OnClickListener callback
    public void onClick(View v) {
    	this.tvError = (TextView)this.findViewById(R.id.tvError);
    	try
        {
	    	// do something when the button is clicked
	    	this.dh = new DataHelper(this);
	        dh.open();
	        
	        this.etEmail = (EditText)this.findViewById(R.id.etEmail);
	        this.etPassword = (EditText)this.findViewById(R.id.etPassword);
	        
	        Classes.User u = dh.getUser(etEmail.getText().toString());
	    	
	        if (u != null && u.Password.equals(etPassword.getText().toString()))
	        {
	        	try
	            {
	        		dh.close();
			    	Intent intent = new Intent(LoginActivity.this,UserMainActivity.class);
			
			    	//Next create the bundle and initialize it
			    	Bundle bundle = new Bundle();
			    	//Add the parameters to bundle as 
			    	bundle.putString("USERNAME", etEmail.getText().toString());
			    	//Add this bundle to the intent
			    	intent.putExtras(bundle);
			    	
			    	//Start next activity
			    	LoginActivity.this.startActivity(intent); 
	            } catch (Exception e) {
	            	tvError.setVisibility(0);
	            	tvError.setText(e.toString());
	      	   	}
	        }
	        else
	        {
	        	tvError.setVisibility(0);
	        	dh.close();
	        }  
        } catch (Exception e) {
        	tvError.setVisibility(0);
        	tvError.setText(e.toString());
        	dh.close();
  	   	}
    }
}
