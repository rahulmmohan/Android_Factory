package com.example.torch;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	ImageButton but;
	Camera cam;
	boolean on = false, has;
	Parameters param;
	MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		but = (ImageButton) findViewById(R.id.button);
		mp = MediaPlayer.create(MainActivity.this, R.raw.on_off);
		has = getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH);
		if (!has) {
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle("Oooops!!");
			ab.setMessage("Sorry\nYour device not have flash!");
			ab.setPositiveButton("I Understand",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							finish();
						}
					});
			ab.show();
		}
		getCamera();

		but.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!on) {
					onFlash();
				} else {
					offFlash();
				}
			}
		});
	}

	void getCamera() {
		if (cam == null) {

			cam = Camera.open();
			param = cam.getParameters();

		}
	}

	void onFlash() {
		if (!on) {
			param = cam.getParameters();
			param.setFlashMode(Parameters.FLASH_MODE_TORCH);
			cam.setParameters(param);
			cam.startPreview();
			on = true;
			changeImage();
			mp.start();

		}
	}

	void offFlash() {
		if (on) {
			param = cam.getParameters();
			param.setFlashMode(Parameters.FLASH_MODE_OFF);
			cam.setParameters(param);
			cam.stopPreview();
			on = false;
			changeImage();
			mp.start();

		}
	}

	void changeImage() {
		if (on) {
			but.setImageResource(R.drawable.on);
		} else {
			but.setImageResource(R.drawable.off);
		}

	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// on pause turn off the flash
		offFlash();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// on resume turn on the flash
		if (has)
			onFlash();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// on starting the app get the camera params
		getCamera();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// on stop release the camera
		if (cam != null) {
			cam.release();
			cam = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}