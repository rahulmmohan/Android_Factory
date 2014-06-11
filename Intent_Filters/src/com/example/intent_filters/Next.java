package com.example.intent_filters;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class Next extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.next);
Uri url=getIntent().getData();
TextView txt=(TextView)findViewById(R.id.textView1);
txt.setText(url.toString());


	}

}
