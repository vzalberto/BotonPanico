package com.example.sendstuff;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	   
    TextView content;
    EditText SMSrecipient, msg, idUser;
    String _SMSrecipient, _msg, _idUser, lat, lon;
    final String defaultMsg = "Botón de pánico activado ";
     
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        content    =   (TextView)findViewById( R.id.content );
        SMSrecipient      =   (EditText)findViewById(R.id.SMSrecipient);
        msg      =   (EditText)findViewById(R.id.msg);
        idUser = (EditText)findViewById(R.id.idUser);         
         
        Button saveme=(Button)findViewById(R.id.save);

        saveme.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v)
            {
                try{
                     
                         // CALL GetText method to make post method call
                        GetText();
                 }
                catch(Exception ex)
                 {
                    content.setText(" url exception! " );
                 }
            }
        });  
    }
     
// Create GetText Metod
public  void  GetText()  throws  UnsupportedEncodingException
    {
        // Get user defined values
        _SMSrecipient = SMSrecipient.getText().toString();
        _msg   = msg.getText().toString();
        _idUser = idUser.getText().toString();

        Toast toast = Toast.makeText(this, "ok", Toast.LENGTH_SHORT);    	
    	toast.show();	
        
        //Get location
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
        Criteria criteria = new Criteria();
        String provider = lm.getBestProvider(criteria, false);      
        Location location = lm.getLastKnownLocation(provider);
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
       
        String SMStext = "";
        SmsManager smsManager = SmsManager.getDefault();    	
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Toast toast2 = Toast.makeText(this, "ok1", Toast.LENGTH_SHORT);    	
    	toast2.show();
    	
        if(msg == null){
        	SMStext += defaultMsg;
        }else{
        	SMStext += msg;
        }

       
        SMStext += currentDateTimeString + "Ubicación: " + lat + ", " + lon;
        String number = "+52"+_SMSrecipient;
        //smsManager.sendTextMessage(number, null, SMStext, null, null);        
       
         // Create data variable for sent values to server  
         
          String data = URLEncoder.encode("idUser", "UTF-8") 
                       + "=" + URLEncoder.encode(_idUser, "UTF-8"); 

          data += "&" + URLEncoder.encode("lat", "UTF-8") + "="
                      + URLEncoder.encode(lat, "UTF-8"); 

          data += "&" + URLEncoder.encode("lon", "UTF-8") 
                      + "=" + URLEncoder.encode(lon, "UTF-8");

          if(msg != null){
          data += "&" + URLEncoder.encode("msg", "UTF-8") 
                      + "=" + URLEncoder.encode(_msg, "UTF-8");
          }
          
          Toast toast3 = Toast.makeText(this, data, Toast.LENGTH_SHORT);    	
      	toast3.show();
          
          String text = "";
          BufferedReader reader=null;
          
       

          // Send data 
        try
        { 
          
            // Defined URL  where to send data
          URL url = new URL("http://ws.guarurapp.com/btn.php");
          URLConnection conn = url.openConnection(); 
          conn.setDoOutput(true); 
          OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
          wr.write( data ); 
          wr.flush(); 
      
          // Get the server response 
           
        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        // Read Server Response
        while((line = reader.readLine()) != null)
            {
                   // Append server response in string
                   sb.append(line + "\n");
            }
            
            
            text = sb.toString();
        }
        catch(Exception ex)
        {
             
        }
        finally
        {
            try
            {
 
                reader.close();
            }

            catch(Exception ex) {}
        }
              
        // Show response on activity
        content.setText( text  );
        
    }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
}
 
}