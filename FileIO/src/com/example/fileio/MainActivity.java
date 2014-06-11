package com.example.fileio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button b1=(Button)findViewById(R.id.button1);
		Button b2=(Button)findViewById(R.id.button2);
		final EditText edit=(EditText)findViewById(R.id.editText1);
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			try {
				FileInputStream fin=openFileInput("text.txt");
				
				String text="";
				int c;
				while((c=fin.read())!=-1)
				{
					text+=Character.toString((char)c);
				}
				edit.setText(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					FileOutputStream fout=openFileOutput("text.txt",MODE_WORLD_READABLE);
					fout.write(edit.getText().toString().getBytes());
					fout.close();
					Toast.makeText(getApplicationContext(), "File saved", 3000).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
