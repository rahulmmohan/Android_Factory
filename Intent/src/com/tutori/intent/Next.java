package com.tutori.intent;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class Next extends Activity {
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.next);

      TextView label = (TextView) findViewById(R.id.show_data);

      Bundle bndl=getIntent().getExtras();
      
      label.setText(bndl.getCharSequence("value"));
   }
	
}