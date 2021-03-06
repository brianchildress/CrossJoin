package com.crossjoinconsulting.lemur;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
//import com.google.api.client.googleapis.json.GoogleJsonError;
//import com.google.api.client.googleapis.json.GoogleJsonError.ErrorInfo;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonError.ErrorInfo;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.Json;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.books.v1.Books;
import com.google.api.services.books.v1.Books.Volumes.List;
import com.google.api.services.books.v1.model.Volume;
import com.google.api.services.books.v1.model.VolumeSaleInfo;
import com.google.api.services.books.v1.model.VolumeVolumeInfo;
import com.google.api.services.books.v1.model.Volumes;

//import java.net.URLEncoder;
//import java.text.NumberFormat;

public class UserMainActivity extends Activity implements OnClickListener
{
	private TextView tvUserName;
	private DataHelper dh;
	private Button btnScan;
	private TextView tvContents;
	private TextView tvFormat;
	private TextView tvTitle;
	
	private static final String API_KEY = "AIzaSyD6B91qHu4WtB54v2FtM1DRnCeqysaupSM";
	
	//  private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
	//  private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();
	
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
		
		//query("0385729359");
		query("--author", "nicholas sparks");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.books_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.new_book:
        	IntentIntegrator.initiateScan(this);
            return true;
        case R.id.save_book:
            //showHelp();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
 // Implement the OnClickListener callback
    public void onClick(View v) {
    	IntentIntegrator.initiateScan(this); 
    }
    
    /**
     * Called when the barcode scanner exits
     *
     * @param requestCode		The request code originally supplied to startActivityForResult(),
     * 							allowing you to identify who this result came from.
     * @param resultCode		The integer result code returned by the child activity through its setResult().
     * @param intent			An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
    	String Contents = "";
    	
	    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
	    if (scanResult != null) 
	    {
	    	try
	    	{
		    	this.tvContents = (TextView)this.findViewById(R.id.tvContents);
	            this.tvFormat = (TextView)this.findViewById(R.id.tvFormat);
	            
	            tvContents.setText(scanResult.getContents());
	            tvFormat.setText(scanResult.getFormatName());
	            Contents = scanResult.getContents();
	    	}
	    	catch(Throwable t)
	    	{
	    		
	    	}
	    }
	    // else continue with any other code you need in the method
	    {
	    	
	    }
        query("--isbn", Contents);
	 }
    
    private void query(String arg, String Contents)
    {
    	try {
        	JsonFactory jsonFactory = new JacksonFactory();
        	
        	// Parse command line parameters into a query.
            // Query format: "[<author|isbn|intitle>:]<query>"
            String prefix = null;
            String query = "";
            if ("--author".equals(arg)) {
                prefix = "inauthor:";
              } else if ("--isbn".equals(arg)) {
                prefix = "isbn:";
              } else if ("--title".equals(arg)) {
                prefix = "intitle:";
              } else if (arg.startsWith("--")) {
                System.err.println("Unknown argument: " + arg);
                System.exit(1);
              } else {
                query = arg;
              }
            if (prefix != null) {
              query = prefix + Contents;
            }          

          try {
            queryGoogleBooks(jsonFactory, query);
            // Success!
            return;
          } catch (HttpResponseException e) 
          {
        	
            if (!Json.CONTENT_TYPE.equals(e.response.contentType)) {
              System.err.println(e.response.parseAsString());
            } else {
              GoogleJsonError errorResponse = GoogleJsonError.parse(jsonFactory, e.response);
              System.err.println(errorResponse.code + " Error: " + errorResponse.message);
              for (ErrorInfo error : errorResponse.errors) {
                System.err.println(jsonFactory.toString(error));
              }              
            }
            
          }
        } catch (Throwable t) {
          t.printStackTrace();
        }	 
    }
    
    private void queryGoogleBooks(JsonFactory jsonFactory, String query) throws Exception {
        // Set up Books client.
    	String bookInfo = "";
        final Books books = new Books(new NetHttpTransport(), jsonFactory);
        books.setApplicationName("Lemur Collections");
        books.accessKey = API_KEY;

        // Set query string and filter only Google eBooks.
        bookInfo.concat("Query: [" + query + "]");
        List volumesList = books.volumes.list(query);
        volumesList.filter = "ebooks";

        // Execute the query.
        Volumes volumes = volumesList.execute();
        if (volumes.totalItems == 0 || volumes.items == null) 
        {
        	bookInfo = bookInfo.concat("No matches found.");
        	this.tvTitle = (TextView)this.findViewById(R.id.tvTitle);
            tvTitle.setText(bookInfo.toString());
          return;
        }

        // Output results.
        for (Volume volume : volumes.items) {
          VolumeVolumeInfo volumeInfo = volume.volumeInfo;
          VolumeSaleInfo saleInfo = volume.saleInfo;

          bookInfo = bookInfo.concat("Title: " + volumeInfo.title);
          
          // Author(s).
          java.util.List<String> authors = volumeInfo.authors;
          if (authors != null && !authors.isEmpty()) {
        	  bookInfo = bookInfo.concat("Author(s): ");
            for (int i = 0; i < authors.size(); ++i) {
            	bookInfo = bookInfo.concat(authors.get(i));
              if (i < authors.size() - 1) {
            	  bookInfo = bookInfo.concat(", ");
              }
            }
            //System.out.print();
          }
          // Description (if any).
          if (volumeInfo.description != null && volumeInfo.description.length() > 0) {
        	  bookInfo = bookInfo.concat("Description: " + volumeInfo.description);
          }
          // Ratings (if any).
          if (volumeInfo.ratingsCount != null && volumeInfo.ratingsCount > 0) {
            int fullRating = (int) Math.round(volumeInfo.averageRating.doubleValue());
            //System.out.print("User Rating: ");
            for (int i = 0; i < fullRating; ++i) {
              //System.out.print("*");
            }
            //System.out.print(" (" + volumeInfo.ratingsCount + " rating(s))");
          }
          // Price (if any).
          if ("FOR_SALE".equals(saleInfo.saleability)) 
          {
            double save = saleInfo.listPrice.amount - saleInfo.retailPrice.amount;
            if (save > 0.0) 
            {
              //System.out.print("List: " + CURRENCY_FORMATTER.format(saleInfo.listPrice.amount)
              //    + "  ");
            }
            //System.out.print("Google eBooks Price: "+ CURRENCY_FORMATTER.format(saleInfo.retailPrice.amount));
            if (save > 0.0) {
              //System.out.print("  You Save: " + CURRENCY_FORMATTER.format(save) + " ("
              //    + PERCENT_FORMATTER.format(save / saleInfo.listPrice.amount) + ")");
            }
            //System.out.print();
          }
          // Access status.
          /*
          String accessViewStatus = volume.accessInfo.accessViewStatus;
          String message = "Additional information about this book is available from Google eBooks at:";
          if ("FULL_PUBLIC_DOMAIN".equals(accessViewStatus)) {
            message = "This public domain book is available for free from Google eBooks at:";
          } else if ("SAMPLE".equals(accessViewStatus)) {
            message = "A preview of this book is available from Google eBooks at:";
          }
          //System.out.print(message);
          */
          // Link to Google eBooks.
          //System.out.print(volumeInfo.infoLink);
        }
        //System.out.print("==========");
        //System.out.print(
        //    volumes.totalItems + " total results at http://books.google.com/ebooks?q="
        //    + URLEncoder.encode(query, "UTF-8"));
        
        this.tvTitle = (TextView)this.findViewById(R.id.tvTitle);
        tvTitle.setText(bookInfo.toString());
      }
}
