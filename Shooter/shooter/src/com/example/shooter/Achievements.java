package com.example.shooter;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Achievements extends Activity{
	ListView list;
	Fetch_DB db;
	ArrayList<ArrayList> achieve=new ArrayList<ArrayList>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.achievements);
		initViews();
		db=new Fetch_DB(getApplicationContext());
		achieve=db.get_achievements();
		MyCustom_ListAdapter mc=new MyCustom_ListAdapter(getApplicationContext(), achieve.get(0), achieve.get(1),achieve.get(2),achieve.get(3));
		list.setAdapter(mc);
	}
	private void initViews() {
		list=(ListView)findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
		
	}
@Override
public void onBackPressed() {
	finish();
	overridePendingTransition(0, 0);
	
	super.onBackPressed();
}
}
