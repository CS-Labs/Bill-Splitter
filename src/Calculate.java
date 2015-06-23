package com.example.billsplitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cslabs.billsplitter.R;
/**
 * 
 * @author Christian Seely
 * @version 1.0 (3/12/15)
 * 
 * This class recives the data from the main activity class, and 
 * displays the results to the user. 
 *
 */
public class Calculate extends ActionBarActivity{
		Button buttonAgain;
		// TODO Auto-generated method stub
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.getSupportActionBar().hide();
	        setContentView(R.layout.child_activity);
	        //Obtain data from last screen. 
		Intent i = getIntent();
		String partySize = i.getStringExtra("Number_In_Party");
		String individualTotal = i.getStringExtra("Individual_Total");
		String billTotal = i.getStringExtra("Bill_Total");
		String tip = i.getStringExtra("Tip");
		
			
			TextView newBillTotal = (TextView) findViewById (R.id.editTextResult1);
			EditText newTip = (EditText) findViewById (R.id.editTextResult2);
			EditText IndiviBill = (EditText) findViewById (R.id.editTextResult4);
			EditText numberInPartyResults = (EditText) findViewById (R.id.editTextResult3);
			//Set all values into edittexts to display to user. 
			newBillTotal.setText("$ " + billTotal);
			//If the user gives no tip... add a sad face ":-("
			if(tip.equals("0.00"))
			{
				newTip.setText("$ " + tip + "  :-(");
			}
			else
			{
			newTip.setText("$ " + tip);
			}
			IndiviBill.setText("$" + individualTotal);
			numberInPartyResults.setText(partySize);
			
			//If user clicks calculate again button go back to the main screen. 
		    Button buttonAgain = (Button) findViewById(R.id.buttonAgain);
			buttonAgain.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(nextScreen);
				}
			});
			
			
		}
	
		
		
		
	}
	


