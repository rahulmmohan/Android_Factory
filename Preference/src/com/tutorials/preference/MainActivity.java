package com.tutorials.preference;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.AlteredCharSequence;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button bu;
	protected int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView txt=(TextView)findViewById(R.id.txt);
		SharedPreferences pref=this.getSharedPreferences("pref", Context.MODE_PRIVATE);
		SharedPreferences.Editor Edpref=pref.edit();
		Boolean flag=pref.getBoolean("start", false);
		int val=pref.getInt("value", 0);
		if(!flag)
		{
			Edpref.putBoolean("start", true);
			AlertDialog.Builder ab=new AlertDialog.Builder(this);
			ab.setTitle("Welcome");
			ab.setMessage("Tou are using this app first time!!");
			ab.setPositiveButton("Ok", null);
			ab.show();
		}
		val++;
		Edpref.putInt("value", val);
		txt.setText("You are using this app "+val+" time" );
		Edpref.commit();

		/*
		 * bu.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub Epref.putBoolean("start", false); Epref.commit();
		 * NotificationManager nm = (NotificationManager)
		 * getSystemService(Context.NOTIFICATION_SERVICE); Notification not =
		 * new Notification( android.R.drawable.ic_dialog_alert, "importanat",
		 * System.currentTimeMillis()); Intent in= new
		 * Intent(getApplicationContext(),MainActivity.class); PendingIntent
		 * pi=PendingIntent.getActivity(getApplicationContext(), 0, in, 0);
		 * not.setLatestEventInfo(getApplicationContext(), "cleared",
		 * "your app data are cleared", pi); nm.notify(i++,not);
		 * 
		 * } });
		 */
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
