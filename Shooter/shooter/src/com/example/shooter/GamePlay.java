package com.example.shooter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.games.Games;

public class GamePlay extends Activity implements OnClickListener {
	InputStream gifinput;
	String[] options;
	int a[][] = new int[4][4];
	int previ[] = new int[16];
	int prevj[] = new int[16];
	int front = 0, rear = 0;
	int previ2[] = new int[16];
	int prevj2[] = new int[16];
	int front2 = 0, rear2 = 0;
	GifAnimationDrawable gif[][] = new GifAnimationDrawable[4][4];
	Random r1 = new Random();
	Random r2 = new Random();
	int threadsleep = 1000, threadsleepnew = 1000;
	int i, j, bi, bj;
	ImageView b[][] = new ImageView[4][4];
	LinearLayout p[][] = new LinearLayout[4][4];
	ProgressBar pr;
	TextView scoretTextView, multTextView, highscoreTextView;
	int score = 0, mul = 1;
	float initialX, initialY, finalX, finalY;
	String hint = "";
	boolean firewithoutbullets = false;
	boolean threadstarted = false;
	Handler mHandler = new Handler();
	Runnable mUpdateResults1;
	Runnable killMe, showNonEnemy, hideNonEnemy, clear, close;
	int bullets = 0;
	RelativeLayout reload;
	LinearLayout canvas;
	Timer t[][] = new Timer[4][4];
	Vibrator v;
	Thread thread;
	boolean gameover = false;
	int enemies = 0, maxenemies = 6;
	int nonenemies = 0, maxnonenemies = 1;
	int progress = 0;
	String colors[] = { "#D50505", "#EA15BA", "#8315EA", "#2E15EA", "#15ABEA",
			"#15EADD", "#15EA88", "#15EA33", "#83EA15", "#E7EA15", "#EA8815",
			"#EA3315" };
	Animation anim,rotation,load_bulets;
	int higscore = 0;
	int chall = 0;
	int poopkill = 0;
	int bulletwaste = 0;
	int reloadcount1 = 0, reloadcount2 = 0;
	Bitmap imageOriginal;
	ImageView dialer,no_bullets;
	int r = 1;
	boolean challegecompleted=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		higscore = getIntent().getIntExtra("bestscore", 0);
		chall = getIntent().getIntExtra("challenge", 0);
		options = getResources().getStringArray(R.array.ids);
		initArray();
		initGif();
		initImageViews();
		initViews();
		initRunnables();
		v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		anim = AnimationUtils.loadAnimation(this, R.anim.holder_bottom);
		reload.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.from_inner);
		dialer.startAnimation(anim);
		anim = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
		rotation = AnimationUtils.loadAnimation(this, R.anim.reload);
		load_bulets = AnimationUtils.loadAnimation(this, R.anim.rotate);
	}

	private void initGif() {
		try {
			InputStream is = getApplicationContext().getResources().getAssets()
					.open("ex1.gif");

			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					gif[i][j] = new GifAnimationDrawable(is);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void initRunnables() {
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (!gameover) {
						threadsleep = threadsleepnew;
						if (newItem()) {
							if ((r1.nextInt(2) == 1)
									&& (nonenemies < maxnonenemies)) {
								a[i][j] = -1;
								nonenemies++;
								mHandler.post(showNonEnemy);
								try {
									previ2[rear2] = i;
									prevj2[rear2] = j;
									rear2 = (rear2 + 1) % 16;
									t[i][j] = new Timer();
									t[i][j].schedule(new TimerTask() {

										@Override
										public void run() {
											mHandler.post(hideNonEnemy);

										}
									}, 2000);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								a[i][j] = 1;
								enemies++;
								mHandler.post(mUpdateResults1);
								try {
									t[i][j] = new Timer();
									t[i][j].schedule(new TimerTask() {

										@Override
										public void run() {
											mHandler.post(killMe);
										}
									}, 5000);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (enemies == maxenemies) {
									enemies = 0;
									maxenemies += 2;
									nonenemies = 0;
									maxnonenemies++;
									threadsleepnew -= 100;
									threadsleep = 3000;

								}
							}
							Thread.sleep(threadsleep);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mUpdateResults1 = new Runnable() {
			public void run() {

				try {
					gifinput = getApplicationContext().getResources()
							.getAssets().open("anim" + r2.nextInt(5) + ".gif");
					gif[i][j] = new GifAnimationDrawable(gifinput);
				} catch (Exception e) {
					e.printStackTrace();
				}
				gif[i][j].setOneShot(false);
				b[i][j].setImageDrawable(gif[i][j]);
				gif[i][j].setVisible(true, true);
				b[i][j].setBackgroundColor(Color.parseColor(colors[r1
						.nextInt(12)]));

			}
		};
		showNonEnemy = new Runnable() {
			public void run() {
				try {

					gif[i][j] = new GifAnimationDrawable(getResources()
							.openRawResource(R.drawable.shit));
				} catch (Exception e) {
					e.printStackTrace();
				}
				gif[i][j].setOneShot(true);
				b[i][j].setImageDrawable(gif[i][j]);
				gif[i][j].setVisible(true, true);

			}
		};
		hideNonEnemy = new Runnable() {
			public void run() {
				b[previ2[front2]][prevj2[front2]]
						.setImageResource(R.drawable.clear);
				a[previ2[front2]][prevj2[front2]] = 0;
				front2 = (front2 + 1) % 16;
			}
		};
		clear = new Runnable() {
			public void run() {
				b[previ[front]][prevj[front]]
						.setImageResource(R.drawable.clear);
				a[previ[front]][prevj[front]] = 0;
				front = (front + 1) % 16;
			}
		};
		close = new Runnable() {
			public void run() {
				gameover = true;
				anim = AnimationUtils.loadAnimation(getApplicationContext(),
						R.anim.holder_bottom_back);
				reload.startAnimation(anim);
				challenge(chall);
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						a[i][j] = 0;
						t[i][j].cancel();
					}
				}
				anim.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation arg0) {
						Intent returnIntent = new Intent();
						returnIntent.putExtra("score", score);
						returnIntent.putExtra("result", challegecompleted);
						setResult(100, returnIntent);
						finish();
						overridePendingTransition(0, 0);
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
					}

					@Override
					public void onAnimationStart(Animation arg0) {
					}
				});

			}
		};
		killMe = new Runnable() {
			public void run() {

				try {
					gif[i][j] = new GifAnimationDrawable(getResources()
							.openRawResource(R.drawable.ex6));
				} catch (Exception e) {
					e.printStackTrace();
				}
				gif[i][j].setOneShot(true);
				canvas.setBackgroundDrawable(gif[i][j]);
				gif[i][j].setVisible(true, true);

				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						mHandler.post(close);
					}
				}, 3000);
				v.vibrate(1500);
				gameover = true;
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						t[i][j].cancel();
						t[i][j] = new Timer();
					}
				}
			}

		};
	}

	private void initViews() {
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"font3.ttf");
		pr = (ProgressBar) findViewById(R.id.progressBar1);
		scoretTextView = (TextView) findViewById(R.id.score);
		multTextView = (TextView) findViewById(R.id.mul);
		highscoreTextView = (TextView) findViewById(R.id.highscore);

		canvas = (LinearLayout) findViewById(R.id.canvas);
		reload = (RelativeLayout) findViewById(R.id.reload);
		dialer = (ImageView) findViewById(R.id.imageView1);
		no_bullets = (ImageView) findViewById(R.id.nobull);
		scoretTextView.setTypeface(custom_font);
		multTextView.setTypeface(custom_font);
		highscoreTextView.setTypeface(custom_font);

		highscoreTextView.setText("" + higscore);
		// ////////////////////////
		pr.setMax(6);
		pr.setProgress(bullets);
		dialer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				reload();
				challenge(chall);
			}
		});
		reload.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getActionMasked();

				switch (action) {

				case MotionEvent.ACTION_DOWN:
					initialX = event.getX();
					initialY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					float finalX = event.getX();
					float finalY = event.getY();
					if (initialX != finalX || initialY != finalY) {
						reload();
					}
					challenge(chall);
					break;
				}
				return true;
			}
		});

	}

	boolean newItem() {
		i = r1.nextInt(4);
		j = r2.nextInt(4);
		if (a[i][j] == 0) {
			return true;
		} else {
			return false;
		}
	}

	private void initArray() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				a[i][j] = 0;
				t[i][j] = new Timer();
			}
		}
	}

	private void initImageViews() {
		b[0][0] = (ImageView) findViewById(R.id.b1);
		b[0][1] = (ImageView) findViewById(R.id.b2);
		b[0][2] = (ImageView) findViewById(R.id.b3);
		b[0][3] = (ImageView) findViewById(R.id.b4);
		b[1][0] = (ImageView) findViewById(R.id.b5);
		b[1][1] = (ImageView) findViewById(R.id.b6);
		b[1][2] = (ImageView) findViewById(R.id.b7);
		b[1][3] = (ImageView) findViewById(R.id.b8);
		b[2][0] = (ImageView) findViewById(R.id.b9);
		b[2][1] = (ImageView) findViewById(R.id.b10);
		b[2][2] = (ImageView) findViewById(R.id.b11);
		b[2][3] = (ImageView) findViewById(R.id.b12);
		b[3][0] = (ImageView) findViewById(R.id.b13);
		b[3][1] = (ImageView) findViewById(R.id.b14);
		b[3][2] = (ImageView) findViewById(R.id.b15);
		b[3][3] = (ImageView) findViewById(R.id.b16);
		//
		p[0][0] = (LinearLayout) findViewById(R.id.p1);
		p[0][1] = (LinearLayout) findViewById(R.id.p2);
		p[0][2] = (LinearLayout) findViewById(R.id.p3);
		p[0][3] = (LinearLayout) findViewById(R.id.p4);
		p[1][0] = (LinearLayout) findViewById(R.id.p5);
		p[1][1] = (LinearLayout) findViewById(R.id.p6);
		p[1][2] = (LinearLayout) findViewById(R.id.p7);
		p[1][3] = (LinearLayout) findViewById(R.id.p8);
		p[2][0] = (LinearLayout) findViewById(R.id.p9);
		p[2][1] = (LinearLayout) findViewById(R.id.p10);
		p[2][2] = (LinearLayout) findViewById(R.id.p11);
		p[2][3] = (LinearLayout) findViewById(R.id.p12);
		p[3][0] = (LinearLayout) findViewById(R.id.p13);
		p[3][1] = (LinearLayout) findViewById(R.id.p14);
		p[3][2] = (LinearLayout) findViewById(R.id.p15);
		p[3][3] = (LinearLayout) findViewById(R.id.p16);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				b[i][j].setOnClickListener(this);
				p[i][j].setOnClickListener(this);
			}
		}

	}

	@Override
	public void onClick(View v) {

		if (bullets > 0) {

			load();
			this.v.vibrate(50);
			bullets--;
			bulletwaste++;
			pr.setProgress(bullets);
			try {
				ImageView clicked = (ImageView) findViewById(v.getId());
				hint = clicked.getTag().toString();
			} catch (Exception e) {
				LinearLayout clicked = (LinearLayout) findViewById(v.getId());
				hint = clicked.getTag().toString();
			}
			bi = Integer.parseInt("" + hint.charAt(0));
			bj = Integer.parseInt("" + hint.charAt(1));
			int val = a[bi][bj];
			if (val == 1 || val == -1) {
				if (val == 1) {
					score += mul;
					scoretTextView.setText(score + "");
				} else {
					poopkill++;
					score -= 10;
					scoretTextView.setText(score + "");
					mul = 1;
					multTextView.setText(mul + "x");
					multTextView.startAnimation(anim);
				}
				a[bi][bj] = 2;
				try {
					gifinput = getApplicationContext().getResources()
							.getAssets().open("ex" + r2.nextInt(8) + ".gif");
					gif[bi][bj] = new GifAnimationDrawable(gifinput);
				} catch (Exception e) {
					e.printStackTrace();
				}
				gif[bi][bj].setOneShot(true);
				b[bi][bj].setImageDrawable(gif[bi][bj]);
				gif[bi][bj].setVisible(true, true);
				b[bi][bj].setBackgroundResource(R.drawable.clear);
				previ[rear] = bi;
				prevj[rear] = bj;
				rear = (rear + 1) % 16;
				t[bi][bj].cancel();
				t[bi][bj] = new Timer();
				t[bi][bj].schedule(new TimerTask() {

					@Override
					public void run() {
						mHandler.post(clear);
					}
				}, 2000);

			} else {
				mul = 1;
				multTextView.setText(mul + "x");
				multTextView.startAnimation(anim);

			}
			challenge(chall);
		} else {

			mul = 1;
			multTextView.setText(mul + "x");
			multTextView.startAnimation(anim);
			firewithoutbullets = true;
			no_bullets.setVisibility(View.VISIBLE);
			no_bullets.startAnimation(anim);
			dialer.startAnimation(load_bulets);
			

		}
	}

	@Override
	public void onBackPressed() {
		gameover = true;
		anim = AnimationUtils.loadAnimation(this, R.anim.holder_bottom_back);
		reload.startAnimation(anim);
		challenge(chall);
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("score", score);
				returnIntent.putExtra("result", challegecompleted);
				setResult(100, returnIntent);
				finish();
				overridePendingTransition(0, 0);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationStart(Animation arg0) {
			}
		});
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				a[i][j] = 0;
				t[i][j].cancel();
			}
		}

	}

	private void challenge(int id) {
		switch (id) {
		case 1:
			showAchievement(chall);
			break;

		case 2:
			if (score >= 32) {
				showAchievement(chall);
			}
			break;
		case 3:
			if (poopkill == 1) {
				showAchievement(chall);
			}
			break;
		case 4:
			if (score == 0) {
				showAchievement(chall);
			}
			break;
		case 5:
			if (score >= 64) {
				showAchievement(chall);
			}
			break;
		case 6:
			if (reloadcount2 >= 5) {
				showAchievement(chall);
			}
			break;
		case 7:
			if (poopkill == 5) {
				showAchievement(chall);
			}
			break;
		case 8:
			showAchievement(chall);
			break;
		case 9:
			if (score >= 128) {
				showAchievement(chall);
			}
			break;
		case 10:
			if (reloadcount1 >= 10) {
				showAchievement(chall);
			}
			break;
		case 11:
			if (score >= 256) {
				showAchievement(chall);
			}
			break;

		case 12:
			if (poopkill == 10) {
				showAchievement(chall);
			}
			break;
		case 13:
			if (score <= -100) {
				showAchievement(chall);
			}
			break;
		case 14:
			if ((higscore - score) < 5) {
				showAchievement(chall);
			}
			break;
		case 15:
			if (bulletwaste >= 60) {
				showAchievement(chall);
			}
			break;

		case 16:
			if (bullets == 0) {
				showAchievement(chall);
			}
			break;
		case 17:
			if (score >= 512) {
				showAchievement(chall);
			}
			break;

		case 18:
			if (poopkill == 25) {
				showAchievement(chall);
			}
			break;

		case 19:
			if (bulletwaste >= 100) {
				showAchievement(chall);
			}
			break;
		case 20:
			if (score >= 1024) {
				showAchievement(chall);
			}
			break;

		}

	}

	private void showAchievement(int chall2) {
		challegecompleted=true;
	
	}

	private void load() {
		imageOriginal = getBitmapFromAsset("re" + r + ".png");
		r++;
		dialer.setImageBitmap(imageOriginal);
		dialer.startAnimation(load_bulets);
	}

	
	private void reload() {

		if (bullets == 0 && !firewithoutbullets && threadstarted) {
			mul++;
			multTextView.setText(mul + "x");
			multTextView.startAnimation(anim);
			reloadcount1++;
		} else {
			mul = 1;
			multTextView.setText(mul + "x");
			multTextView.startAnimation(anim);
			firewithoutbullets = false;
			reloadcount2++;
		}
		bullets = 6;
		r = 1;
		no_bullets.setVisibility(View.GONE);
		imageOriginal = getBitmapFromAsset("re0.png");

		dialer.setImageBitmap(imageOriginal);
	
		dialer.startAnimation(rotation);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int ii = 1; ii <= 6; ii++) {
					pr.setProgress(ii);
					try {
						Thread.currentThread().sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
		if (!threadstarted) {
			thread.start();
			threadstarted = true;

		}

	}

	private Bitmap getBitmapFromAsset(String strName) {
		AssetManager assetManager = getAssets();
		InputStream istr = null;
		try {
			istr = assetManager.open(strName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(istr);
		return bitmap;
	}

	
}
