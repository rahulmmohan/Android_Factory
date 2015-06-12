package com.example.shooter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyCustom_ListAdapter extends ArrayAdapter<String> {

	private final Context context;
	ArrayList<String> id;
	ArrayList<String> title;
	ArrayList<String> desc;
	ArrayList<String> finish;

	public MyCustom_ListAdapter(Context context,ArrayList<String> id, ArrayList<String> title,
			ArrayList<String> desc, ArrayList<String> finish ) {
		super(context, R.layout.activity_main, title);

		this.context = context;
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.finish = finish;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView;
		LayoutInflater inflater;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		try {
			if (id.get(position).contentEquals("21")
					|| id.get(position).contentEquals("22")) {
				rowView = inflater.inflate(R.layout.one_item_with_progress, parent, false);
				TextView itemTextView = (TextView) rowView
						.findViewById(R.id.title);
				TextView priceTextView = (TextView) rowView
						.findViewById(R.id.desc);
				TextView progressTextView = (TextView) rowView
						.findViewById(R.id.progress);
				ProgressBar pr = (ProgressBar) rowView.findViewById(R.id.progressBar);
				itemTextView.setText(title.get(position));
				priceTextView.setText(desc.get(position));
				int val=Integer.parseInt(finish.get(position));
				if (id.get(position).contentEquals("21"))
						{
					
					int prg =  val*100/5120;
						progressTextView.setText(finish.get(position));
						pr.setProgress(prg);
						}
				if (id.get(position).contentEquals("22"))
						{
					int prg= val*100/10240;
					progressTextView.setText(finish.get(position));
					pr.setProgress(prg);
						}
				
			} else {
				 
				rowView = inflater.inflate(R.layout.one_item, parent, false);
				TextView itemTextView = (TextView) rowView
						.findViewById(R.id.title);
				TextView priceTextView = (TextView) rowView
						.findViewById(R.id.desc);
				ImageView image = (ImageView) rowView.findViewById(R.id.image);
				itemTextView.setText(title.get(position));
				priceTextView.setText(desc.get(position));
				if (finish.get(position).contentEquals("0")) {
					rowView.setEnabled(false);
					image.setImageResource(R.drawable.ach_bw);
				}
			}

		} catch (Exception e) {
			Log.e("MyCustom_ListAdapter:getView()", e.toString());
			return null;
		}

		return rowView;
	}
}
