package com.example.shooter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szugyi.circlemenu.view.CircleImageView;
import com.szugyi.circlemenu.view.CircleLayout;
import com.szugyi.circlemenu.view.CircleLayout.OnCenterClickListener;
import com.szugyi.circlemenu.view.CircleLayout.OnItemClickListener;
import com.szugyi.circlemenu.view.CircleLayout.OnItemSelectedListener;
import com.szugyi.circlemenu.view.CircleLayout.OnRotationFinishedListener;

public class GameMenu extends Activity implements OnItemSelectedListener,
		OnItemClickListener, OnRotationFinishedListener, OnCenterClickListener,
		OnClickListener {
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	int bestscore = 0;
	int currentscore = 0;
	TextView scoreTextView, bestscoreTextView, title;

	RelativeLayout background;
	CircleLayout circleMenu;
	ArrayList<String> data = new ArrayList<String>();
	Fetch_DB fdb;
	int option = 0;
	private Animation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_fast);
		initPreference();
		fetchDb();
		initViews();

	}

	private void fetchDb() {
		fdb = new Fetch_DB(getApplicationContext());
		data = fdb.get_row();

	}

	private void initViews() {
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"font3.ttf");

		title = (TextView) findViewById(R.id.textView1);
		scoreTextView = (TextView) findViewById(R.id.textView2);
		bestscoreTextView = (TextView) findViewById(R.id.textView4);
		title.setOnClickListener(this);
		title.setTypeface(custom_font);
		scoreTextView.setTypeface(custom_font);
		bestscoreTextView.setTypeface(custom_font);

		circleMenu = (CircleLayout) findViewById(R.id.main_circle_layout);
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);
		circleMenu.setOnRotationFinishedListener(this);
		circleMenu.setOnCenterClickListener(this);

		background = (RelativeLayout) findViewById(R.id.background);

		title.setText(((CircleImageView) circleMenu.getSelectedItem())
				.getName());
		changeBackground(circleMenu.getSelectedItem().getId());

	}

	private void initPreference() {
		pref = getSharedPreferences("game1", Context.MODE_PRIVATE);
		editor = pref.edit();
		if (pref.getBoolean("first", true)) {
			setDB();
		}
		bestscore = pref.getInt("bestscore", 0);

	}

	private void setDB() {
		DB_check db = new DB_check(getPackageName() + "",
				getApplicationContext());
		if (!db.isdatabaseexist()) {
			db.createfolder();
			db.copydatabase();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent dat) {
		switch (requestCode) {
		case 100:
			 animation = AnimationUtils.loadAnimation(this,
					R.anim.from_inner);
			circleMenu.startAnimation(animation);
			currentscore = dat.getIntExtra("score", 0);

			if (dat.getBooleanExtra("result", false)) {

				fdb.set_row(data.get(0));
				fetchDb();
			}
			scoreTextView.setText("" + currentscore);
			if (currentscore > bestscore) {
				bestscoreTextView.setText("High Score : " + currentscore);
				editor.putInt("bestscore", currentscore);
				editor.commit();
				editor = pref.edit();
				bestscore = pref.getInt("bestscore", 0);
			}
			circleMenu
					.rotateViewToCenter((CircleImageView) findViewById(R.id.score));
			break;

		}
		super.onActivityResult(requestCode, resultCode, dat);

	}

	private void changeBackground(int id) {
		switch (id) {
		case R.id.play:
			bestscoreTextView.setText(data.get(2));
			scoreTextView.setText(data.get(1));
			background.setBackgroundColor(Color.parseColor("#A1A1A1"));
			option = 0;
			break;
		case R.id.score:
			background.setBackgroundColor(Color.parseColor("#E54B3B"));
			scoreTextView.setText("" + currentscore);
			bestscoreTextView.setText("High Score : " + bestscore);
			option = 1;
			break;
		case R.id.achie:
			background.setBackgroundColor(Color.parseColor("#E47D21"));
			scoreTextView.setText("");
			bestscoreTextView.setText("");
			option = 2;
			break;
		case R.id.about:
			background.setBackgroundColor(Color.parseColor("#EFC20E"));
			scoreTextView.setText("Shooter");
			bestscoreTextView.setText("Factory72");
			option = 3;
			break;
		case R.id.vib:
			background.setBackgroundColor(Color.parseColor("#32B3E3"));
			scoreTextView.setText("");
			if (pref.getBoolean("vib", true)) {
				bestscoreTextView.setText("Vibaration is enabled");
			} else {
				bestscoreTextView.setText("Vibaration is disabled");
			}
			option = 4;
			break;
		case R.id.sound:
			background.setBackgroundColor(Color.parseColor("#19BA9A"));
			scoreTextView.setText("");
			if (pref.getBoolean("music", true)) {
				bestscoreTextView.setText("Music is enabled");
			} else {
				bestscoreTextView.setText("Music is disabled");
			}
			option = 5;
			break;

		}

	}

	@Override
	public void onItemSelected(View view, String name) {
		title.setText(name);
		changeBackground(view.getId());

	}

	@Override
	public void onItemClick(View view, String name) {
		switch (view.getId()) {
		case R.id.play:
			play();
			break;
		case R.id.achie:
			medals();
			break;
		case R.id.about:
			break;
		case R.id.vib:
			setVib();
			break;
		case R.id.sound:
			setMusic();
			break;

		}

	}


	private void setMusic() {
		if (pref.getBoolean("music", false)) {
			editor.putBoolean("music", false);
			editor.commit();
			editor = pref.edit();
			bestscoreTextView.setText("Music is disabled");
		} else {
			editor.putBoolean("music", true);
			editor.commit();
			editor = pref.edit();
			bestscoreTextView.setText("Music is enabled");
		}

	}

	private void setVib() {
		if (pref.getBoolean("vib", false)) {
			editor.putBoolean("vib", false);
			editor.commit();
			editor = pref.edit();
			bestscoreTextView.setText("Vibaration is disabled");
		} else {
			editor.putBoolean("vib", true);
			editor.commit();
			editor = pref.edit();
			bestscoreTextView.setText("Vibaration is enabled");
		}

	}

	private void medals() {
		Intent i = new Intent(getApplicationContext(), Achievements.class);
		startActivity(i);
	}

	private void play() {
		 animation = AnimationUtils.loadAnimation(this,
				R.anim.to_inner);
		circleMenu.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent i = new Intent(getApplicationContext(), GamePlay.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				i.putExtra("bestscore", bestscore);
				i.putExtra("challenge", Integer.parseInt(data.get(0)));
				startActivityForResult(i, 100);
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onRotationFinished(View view, String name) {
		Animation animation = new RotateAnimation(0, 360, view.getWidth() / 2,
				view.getHeight() / 2);
		animation.setDuration(250);
		view.startAnimation(animation);
	}

	@Override
	public void onCenterClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.textView1:
			
			onItemClick(((CircleImageView)circleMenu.getSelectedItem()), ((CircleImageView)circleMenu.getSelectedItem()).getName());
			break;
		}
	}
@Override
public void onBackPressed() {
	animation = AnimationUtils.loadAnimation(this,
			R.anim.to_inner);
	circleMenu.startAnimation(animation);

	animation.setAnimationListener(new AnimationListener() {
		@Override
		public void onAnimationEnd(Animation arg0) {
			finish();
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}
	});
	super.onBackPressed();
}
}
