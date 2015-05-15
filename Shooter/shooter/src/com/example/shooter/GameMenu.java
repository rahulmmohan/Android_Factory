package com.example.shooter;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.szugyi.circlemenu.view.CircleLayout;
import com.szugyi.circlemenu.view.CircleImageView;
import com.szugyi.circlemenu.view.CircleLayout.OnCenterClickListener;
import com.szugyi.circlemenu.view.CircleLayout.OnItemClickListener;
import com.szugyi.circlemenu.view.CircleLayout.OnItemSelectedListener;
import com.szugyi.circlemenu.view.CircleLayout.OnRotationFinishedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameMenu extends Activity
implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
View.OnClickListener, RealTimeMessageReceivedListener,
RoomStatusUpdateListener, RoomUpdateListener, OnInvitationReceivedListener,
		OnItemSelectedListener, OnItemClickListener,
		OnRotationFinishedListener, OnCenterClickListener {
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	int bestscore = 0;
	int currentscore = 0;
	TextView scoreTextView, bestscoreTextView, title;

	RelativeLayout background;
	CircleLayout circleMenu;
	ArrayList<String> data = new ArrayList<String>();
	Fetch_DB fdb;
	public static GoogleApiClient client;
	// Request code used to invoke sign in user interactions.
	  private static final int RC_SIGN_IN = 9001;

	  // Client used to interact with Google APIs.
	  private GoogleApiClient mGoogleApiClient;

	  // Are we currently resolving a connection failure?
	  private boolean mResolvingConnectionFailure = false;

	  // Has the user clicked the sign-in button?
	  private boolean mSignInClicked = false;

	  // Set to true to automatically start the sign in flow when the Activity starts.
	  // Set to false to require the user to click the button in order to sign in.
	  private boolean mAutoStartSignInFlow = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_fast);
		initPreference();
		fetchDb();
		initViews();
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Games.API).addScope(Games.SCOPE_GAMES)
        .build();
		
		 mSignInClicked = true;
         mGoogleApiClient.connect();
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
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.from_inner);
			circleMenu.startAnimation(animation);
			currentscore = dat.getIntExtra("score", 0);
			if (dat.getBooleanExtra("result", false)) {
				Toast.makeText(getApplicationContext(), "vd", 1000).show();
				fdb.set_row(data.get(0));
				fetchDb();
			}
			scoreTextView.setText("" + currentscore);
			if (currentscore > bestscore) {
				bestscoreTextView.setText("High Score : " + currentscore);
				editor.putInt("bestscore", currentscore);
				editor.commit();
				editor = pref.edit();
			}
			circleMenu
					.rotateViewToCenter((CircleImageView) findViewById(R.id.score));
			break;
		case RC_SIGN_IN:
           
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
              mGoogleApiClient.connect();
            } 
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
			break;
		case R.id.score:
			background.setBackgroundColor(Color.parseColor("#E54B3B"));
			scoreTextView.setText("" + currentscore);
			bestscoreTextView.setText("High Score : " + bestscore);
			break;
		case R.id.achie:
			background.setBackgroundColor(Color.parseColor("#E47D21"));
			scoreTextView.setText("");
			bestscoreTextView.setText("");
			break;
		case R.id.about:
			background.setBackgroundColor(Color.parseColor("#EFC20E"));
			scoreTextView.setText("Shooter");
			bestscoreTextView.setText("Factory72");
			break;
		case R.id.vib:
			background.setBackgroundColor(Color.parseColor("#32B3E3"));
			scoreTextView.setText("");
			bestscoreTextView.setText("Vibaration is enabled");
			break;
		case R.id.sound:
			background.setBackgroundColor(Color.parseColor("#19BA9A"));
			scoreTextView.setText("");
			bestscoreTextView.setText("Music is enabled");
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

			break;
		case R.id.sound:

			break;

		}

	}

	private void medals() {
		if (mGoogleApiClient.isConnected()) {
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(mGoogleApiClient), 1);
		}

	}

	private void play() {
		Animation animation = AnimationUtils.loadAnimation(this,
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
public void onBackPressed() {
	client.disconnect();
	super.onBackPressed();
}

@Override
public void onInvitationReceived(Invitation arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onInvitationRemoved(String arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onJoinedRoom(int arg0, Room arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onLeftRoom(int arg0, String arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onRoomConnected(int arg0, Room arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onRoomCreated(int arg0, Room arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onConnectedToRoom(Room arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onDisconnectedFromRoom(Room arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onP2PConnected(String arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onP2PDisconnected(String arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPeerDeclined(Room arg0, List<String> arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPeerInvitedToRoom(Room arg0, List<String> arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPeerJoined(Room arg0, List<String> arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPeerLeft(Room arg0, List<String> arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPeersConnected(Room arg0, List<String> arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPeersDisconnected(Room arg0, List<String> arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onRoomAutoMatching(Room arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onRoomConnecting(Room arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onRealTimeMessageReceived(RealTimeMessage arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
}

@Override
public void onConnectionFailed(ConnectionResult arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onConnected(Bundle arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onConnectionSuspended(int arg0) {
	// TODO Auto-generated method stub
	
}
}
