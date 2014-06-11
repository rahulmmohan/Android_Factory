package com.example.notification;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				NotificationManager nmgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				Notification nfn = new Notification(
						android.R.drawable.stat_notify_error, "Important",
						System.currentTimeMillis());
				Intent intt=new Intent(getApplicationContext(),MainActivity.class);
				PendingIntent pintt=PendingIntent.getActivity(getApplicationContext(), 0, intt, 0);
				nfn.setLatestEventInfo(getApplicationContext(), "Notification", "This is a notification",pintt);
				nmgr.notify(0,nfn);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
