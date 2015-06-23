package com.example.billsplitter;


import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cslabs.billsplitter.R;
/**
 * 
 * @author Christian Seely 
 * @version 1.0 (3/12/15)
 * 
 * This is the main class for the bill splitter app, it obtains the 
 * users data from edit texts, and does the required calculations, and
 * sends the results to the next screen. 
 *
 */
public class MainActivity extends ActionBarActivity {
	//Boolean values checking click state. 
	private boolean calcButtonClicked = false;
	private boolean exactChange = false;
	private boolean roundUp = false;
	private boolean roundDown = false;
	private int partySize = 0;
	private double billTotal = 0.0;
	private double tip = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        
        
        
        AppRater.app_launched(this); //Sends state to app rater class. 

	    Button calcButton = (Button) findViewById(R.id.CalcButton);
		calcButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				calcButtonClicked = true;
				EditText numberInPartyInput = (EditText) findViewById (R.id.numberInPartyEditTxt);
				EditText billInput = (EditText) findViewById (R.id.billTotalEditTxt);
				EditText tipInput = (EditText) findViewById (R.id.tipEditTxt);
				//Prevent user from clicking calculate before the edit texts are full. 
				if(numberInPartyInput.getText().toString().equals("") || billInput.getText().toString().equals("") || tipInput.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "Enter required Information before pressing Calculate.",
							   Toast.LENGTH_SHORT).show();
				}
				else
				{
					//Grab info from editexts. 
				partySize = Integer.parseInt(numberInPartyInput.getText().toString());
				billTotal = Double.parseDouble(billInput.getText().toString());
				tip = Integer.parseInt(tipInput.getText().toString());
				calculate(); //Call calculate method. 

				}
			}
	
		});
//		}
		//Check which button is clicked for the rounding mode. 
        Button exactButton = (Button) findViewById(R.id.exactButton);
		exactButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//Just in case the user clicks multiple rounding modes
				//before clicking calculate set the other modes to false. 
				exactChange = true;
				roundUp = false;
				roundDown = false;
				
			}
		});
		Button quarterButton = (Button) findViewById(R.id.roundUp);
		quarterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				roundUp = true;
				exactChange = false;
				roundDown = false;
				
				
			}
		});

		Button dollarButton = (Button) findViewById(R.id.roundDown);
		dollarButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				roundDown = true;
				roundUp = false;
				exactChange = false;
				
				
			}
		});		
		}

/*
 * Calculate Bill total, tax and individual bill total and pass 
 * the results to the next screen. 
 */
public void calculate()
{
	boolean partySizeCheck, billTotalCheck, tipCheck;
	partySizeCheck = billTotalCheck = tipCheck = true;
	double billTotalWithTip,  tipValue;
	billTotalWithTip =  tipValue = 0;
	double tipValueConditionalCheck = (tip/100) * billTotal;
	double individualBillTotal = 0;
	//Reasonable conditional checks for input data. 
	if(partySize <= 0 || partySize > 50)
	{ 
		partySizeCheck = false;
		Toast.makeText(getApplicationContext(), "Invalid range: Party Size must be between 1 and 50.",
				   Toast.LENGTH_SHORT).show();
	}
	//Realistic max and min range on the bill total.
	if(billTotal < 1.00 || billTotal > 100000.00)
	{
		billTotalCheck = false;
		Toast.makeText(getApplicationContext(), "Invalid range: Bill total must be between 1.00 and 100000.00.",
				   Toast.LENGTH_SHORT).show();
	}
	//Realistic range for tip. 
	if(tip < 0 || tip > 200)
	{
		tipCheck = false;
		Toast.makeText(getApplicationContext(), "Invalid range: Tip range should be between 0 and 200%.",
				   Toast.LENGTH_SHORT).show();
	}
	//Check it the user is entering tip in correct format e.g 15 not .15, also 0% can technically be a tip...
	//Even thought the edit text prevents entering decimals, this is just a preventive measure. 
	else if(tip < 1 && tip != 0)
	{
		tipCheck = false;
		Toast.makeText(getApplicationContext(), "Incorrect Tip Format: Formatting should be whole numbers not decimal e.g 15 not .15",
				   Toast.LENGTH_SHORT).show();
	}
	//If user does not select a rounding mode then by default it is set to exact change. 
	if(roundUp == false && roundDown == false)
	{
		exactChange = true;
	}
	//Avoid negative tips, which might occur if user rounds down. 
	if((float)Math.floor(billTotal+tipValueConditionalCheck) < billTotal && exactChange == false && roundUp == false)
	{
		exactChange = true;
		Toast.makeText(getApplicationContext(), "Cannot round down, Resulting total is below bill total. Setting to Exact Change",
				   Toast.LENGTH_LONG).show();
	}
	if((calcButtonClicked == true) && partySizeCheck == true && billTotalCheck == true && tipCheck == true)
	{	
		//Initialize/create variables. 
		tipValue = (tip/100) * billTotal;
		billTotalWithTip = billTotal + tipValue;
		BigDecimal roundedTip = null;
		double tempTip = 0.0;
		BigDecimal roundedIndividualBill = null;
		BigDecimal roundedBillTotal = null;
		String ChangeFormatSingleBill = "";
		String ChangeFormatWholeBill = "";
		String ChangeFormatTip = "";
 
		if (exactChange == true)
		{
			//Re-initialize all the values because the user could have clicked multiple buttons making them still
			//technically true which will mess up calculations. 
			roundUp = false;
			roundDown = false;
			billTotalWithTip = Math.round(billTotalWithTip*100.00)/100.00;
			individualBillTotal = Math.round((billTotalWithTip / partySize)*100.00)/100.00;
			tempTip = billTotalWithTip - billTotal;
			DecimalFormat df = new DecimalFormat("#.00"); //Set format. 
			ChangeFormatSingleBill = df.format(individualBillTotal); //Temp for changing format.
			ChangeFormatWholeBill = df.format(billTotalWithTip); //Temp for changing format.
			ChangeFormatTip = df.format(tempTip);	//Temp for changing format. 
			roundedIndividualBill = new BigDecimal(ChangeFormatSingleBill);
			roundedBillTotal = new BigDecimal(ChangeFormatWholeBill);
			roundedTip = new BigDecimal(ChangeFormatTip);
		}
		if (roundUp == true)
		{
			roundDown = false;
			exactChange = false;
			//Same as exact except total bill is rounded up. 
			billTotalWithTip = Math.ceil(billTotalWithTip); 
			individualBillTotal = Math.round((billTotalWithTip / partySize) * 100.00) / 100.00;
			tempTip = billTotalWithTip - billTotal;
			DecimalFormat df = new DecimalFormat("#.00");
			ChangeFormatSingleBill = df.format(individualBillTotal);
			ChangeFormatWholeBill = df.format(billTotalWithTip);
			ChangeFormatTip = df.format(tempTip);
			roundedIndividualBill = new BigDecimal(ChangeFormatSingleBill);
			roundedBillTotal = new BigDecimal(ChangeFormatWholeBill);
			roundedTip = new BigDecimal(ChangeFormatTip);
		}

		//Round to nearest dollar
		if (roundDown == true)
		{
			//Same as exact change except total bill is rounded down.
			roundUp = false;
			exactChange = false;
			billTotalWithTip = Math.floor(billTotalWithTip);
			individualBillTotal = Math.round((billTotalWithTip / partySize)*100.00)/100.00;
			tempTip = billTotalWithTip - billTotal;
			DecimalFormat df = new DecimalFormat("#.00");
			ChangeFormatSingleBill = df.format(individualBillTotal);
			ChangeFormatWholeBill = df.format(billTotalWithTip);
			ChangeFormatTip = df.format(tempTip);
			roundedIndividualBill = new BigDecimal(ChangeFormatSingleBill);
			roundedBillTotal = new BigDecimal(ChangeFormatWholeBill);
			roundedTip = new BigDecimal(ChangeFormatTip);
		}
		//Change screens, and send all calculated data. 
		Intent nextScreen = new Intent(getApplicationContext(), Calculate.class);
		nextScreen.putExtra("Number_In_Party", "" + partySize);
		nextScreen.putExtra("Individual_Total", "" + roundedIndividualBill);
		nextScreen.putExtra("Bill_Total", "" + roundedBillTotal);
		nextScreen.putExtra("Tip","" + roundedTip);
		startActivity(nextScreen);
	}
}
}
		

