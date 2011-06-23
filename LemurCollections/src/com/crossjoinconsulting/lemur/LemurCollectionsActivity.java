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
        
        Cursor cServices = dh.getAllServices();
        cServices.moveToFirst();
        while (cServices.isAfterLast() == false) {
            sb.append(cServices.getString(1) + "\n");
            cServices.moveToNext();
        }
        cServices.close();
        
        this.output.setText(sb.toString());
        
        dh.close();
    }
}