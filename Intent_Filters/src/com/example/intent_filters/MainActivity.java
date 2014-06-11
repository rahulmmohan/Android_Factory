package com.example.intent_filters;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button b=(Button)findViewById(R.id.but);
		final CheckBox check=(CheckBox)findViewById(R.id.check);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				if(check.isChecked())
				{
				Intent in=new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://www.google.com"));
				startActivity(in);
				}
				else
				{
					Intent in=new Intent("com.example.intent_filters.LAUNCH",Uri.parse("http://www.google.com"));
					startActivity(in);
				}
				
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
