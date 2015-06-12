package info.androidhive.listviewfeed;

import info.androidhive.listviewfeed.ScrollViewX.OnScrollViewListener;
import info.androidhive.listviewfeed.app.AppController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class FullView extends AppCompatActivity implements
		OnScrollViewListener, OnClickListener {
	Toolbar toolbar, toolbar2;
	ScrollViewX scrollView;
	View HeaderView;
	private int MinHeaderTranslation;
	private int HeaderHeight, headertitlewidth;
	private ActionBar ab;
	private TextView headerTitle;
	private TextView headerSubtitle;
	int toolbarTitleLeftMargin;
	RelativeLayout fake_toolbar;
	ColorDrawable cd, cd2;
	// ////////////////
	String image, title, id;
	ImageView feedImageView;
	AQuery aq;
	Bitmap originalImage;
	String m_chosenDir = "";
	ProgressBar pb, load;
	EditText dir;
	TextView download;
	LinearLayout download_opt;
	GridView grid;
	ImageView play;
	String URL_FEED1 = "https://zazkov-youtube-grabber-v1.p.mashape.com/download.mp3.php?id=";
	String URL_FEED2 = "https://zazkov-youtube-grabber-v1.p.mashape.com/download.video.php?id=";
	ArrayList<String> video_urls = new ArrayList<String>();
	ArrayList<String> video_size = new ArrayList<String>();
	ArrayList<String> video_format = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_full_view);
		initViews();
		Bundle bndl = getIntent().getExtras();
		image = bndl.getString("image");
		title = bndl.getString("title");
		id = bndl.getString("id");
		aq = new AQuery(getApplicationContext());
		setViews();
		initValues();
	}

	private void initValues() {
		URL_FEED1 += id;
		URL_FEED2 += id;
		getURLS(URL_FEED2);
		// getURLS(URL_FEED2);

	}

	private void getURLS(String url) {
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, url,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						parseJsonforAudioURL(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d("jsonRequest",
								"Error: " + error.getMessage());
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("X-Mashape-Key",
						"ZxBlbPEIovmshh09pbD9fLNLEfqCp1fFOLAjsnbADaxqf80tjA");
				headers.put("Accept", "application/json");

				return headers;
			}

		};
		AppController.getInstance().addToRequestQueue(jsonObjReq, "recent");
	}

	protected void parseJsonforAudioURL(JSONObject response) {
		video_urls.clear();
		try {
			JSONArray feedArray = response.getJSONArray("map");
			JSONArray video;
			for (int i = 0; i < feedArray.length(); i++) {
				video = feedArray.getJSONArray(i);
				video_format.add(video.get(0).toString());
				video_urls.add(video.get(2).toString());
				video_size.add(video.get(4).toString());
			}

			MyCustom_GridAdapter mc = new MyCustom_GridAdapter(FullView.this,
					video_format);
			grid.setAdapter(mc);
			load.setVisibility(View.GONE);
			download_opt.setVisibility(View.VISIBLE);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void setViews() {
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();

		ImageContainer newContainer = imageLoader.get(image,
				new ImageListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}

					@Override
					public void onResponse(final ImageContainer response,
							boolean isImmediate) {
						if (response.getBitmap() != null) {
							originalImage = response.getBitmap();
						}
					}
				});

		aq.id(feedImageView).image(originalImage, (float) 1.5);
		headerTitle.setText(title);

	}

	private void initViews() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		scrollView = (ScrollViewX) findViewById(R.id.scrollView);
		cd = new ColorDrawable(Color.parseColor("#2196F3"));
		cd2 = new ColorDrawable(Color.parseColor("#2196F3"));
		toolbar.setBackgroundDrawable(cd2);
		headerTitle = (TextView) findViewById(R.id.header_title);
		headertitlewidth = headerTitle.getWidth();
		fake_toolbar = (RelativeLayout) findViewById(R.id.header_fake);
		play = (ImageView) findViewById(R.id.play);
		play.setOnClickListener(this);
		fake_toolbar.setBackgroundDrawable(cd);
		feedImageView = (ImageView) findViewById(R.id.feedImage1);
		cd.setAlpha(0);
		cd2.setAlpha(0);
		toolbarTitleLeftMargin = getResources().getDimensionPixelSize(
				R.dimen.toolbar_left_margin);
		HeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.header_height);
		MinHeaderTranslation = -HeaderHeight
				+ getResources().getDimensionPixelOffset(
						R.dimen.action_bar_height);
		scrollView.setOnScrollViewListener(this);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		dir = (EditText) findViewById(R.id.dir);
		dir.setOnClickListener(this);
		download = (TextView) findViewById(R.id.button1);
		download.setOnClickListener(this);
		download_opt = (LinearLayout) findViewById(R.id.download);
		load = (ProgressBar) findViewById(R.id.load);
		grid = (GridView) findViewById(R.id.grid);

	}

	/*
	 * void UpdateItemsAlpha(float offset) { // Alpha 255=100%, 0=0%
	 * //cd.setAlpha((int)(offset*255)); fake_toolbar.setAlpha(offset);
	 * headerTitle.setTranslationX(toolbarTitleLeftMargin * offset);
	 * 
	 * // Alpha 1=100%, 0=0%
	 * 
	 * HeaderFab.Alpha = 1 - offset; HeaderTitle.Alpha = 1 - offset;
	 * HeaderSubtitle.Alpha = 1 - offset;
	 * 
	 * }
	 * 
	 * // Method that allows us to get the scroll Y position of the ListView
	 * public int getScrollY(ScrollViewX view) { View c = view.getChildAt(0); if
	 * (c == null) return 0; int firstVisiblePosition =
	 * view.getFirstVisiblePosition(); int top = c.getTop(); int headerHeight =
	 * 0; if (firstVisiblePosition >= 1) headerHeight = this.HeaderHeight;
	 * return -top + firstVisiblePosition * c.getHeight() + headerHeight; }
	 */

	@Override
	public void onScrollChanged(ScrollViewX v, int l, int t, int oldl, int oldt) {
		int scrollY = v.getScrollY();
		int alpha = getAlphaforActionBar(scrollY);
		cd.setAlpha(alpha);
		cd2.setAlpha(alpha);
		float offset = 1 - Math.max((float) (-MinHeaderTranslation - scrollY)
				/ -MinHeaderTranslation, 0f);
		headerTitle.setTranslationX(toolbarTitleLeftMargin * offset);
		headerTitle.setScaleX(offset);
		float r = (float) (1.5) - offset;
		if (r > 1.0) {

			aq.id(feedImageView).image(originalImage, r);
		}

		/*
		 * int scrollY = getScrollY(view);
		 * 
		 * // This will collapse the header when scrolling, until its height //
		 * reaches // the toolbar height HeaderView.setTranslationY(Math.max(0,
		 * scrollY + MinHeaderTranslation));
		 * 
		 * // Scroll ratio (0 <= ratio <= 1). // The ratio value is 0 when the
		 * header is completely expanded, // 1 when it is completely collapsed
		 * 
		 * 
		 * // Update items alpha from offset UpdateItemsAlpha(offset);
		 */

	}

	private int getAlphaforActionBar(int scrollY) {
		int minDist = 0, maxDist = HeaderHeight;
		if (scrollY > maxDist) {
			return 255;
		} else if (scrollY < minDist) {
			return 0;
		} else {
			int alpha = 0;
			alpha = (int) ((255.0 / maxDist) * scrollY);
			return alpha;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);

		return true;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.dir:
			openDirectoryChooser();
			break;

		case R.id.button1:
			Toast.makeText(getApplicationContext(), "downloading..", 1000)
					.show();
			download();
			break;
		case R.id.play:
			Intent i = new Intent(getApplicationContext(), YoutubePlay.class);
			i.putExtra("id", id);
			startActivity(i);
			break;
		}

	}

	private void download() {
		Toast.makeText(getApplicationContext(), video_urls.get(5), 1000).show();
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(video_urls.get(5)));

		request.setDescription(title);
		request.setTitle("Download");
		request.allowScanningByMediaScanner();
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		File ext = new File(dir.getText().toString());
		request.setDestinationInExternalPublicDir(ext.getAbsolutePath(),
				"abc.3gp");

		final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

		final long downloadId = manager.enqueue(request);
		new Thread(new Runnable() {

			@Override
			public void run() {

				boolean downloading = true;

				while (downloading) {

					DownloadManager.Query q = new DownloadManager.Query();
					q.setFilterById(downloadId);

					Cursor cursor = manager.query(q);
					cursor.moveToFirst();
					int bytes_downloaded = cursor.getInt(cursor
							.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
					int bytes_total = cursor.getInt(cursor
							.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

					if (cursor.getInt(cursor
							.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
						downloading = false;
					}

					final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							pb.setProgress((int) dl_progress);

						}
					});

					cursor.close();
				}

			}
		}).start();

		/*
		 * File target = new File(ext,"fds.3gp"); AjaxCallback<File> call =new
		 * AjaxCallback<File>(){
		 * 
		 * public void callback(String url, File file, AjaxStatus status) {
		 * Toast.makeText(getApplicationContext(), status.toString(),
		 * 1000).show(); if(file != null){
		 * 
		 * // showResult("File:" + file.length() + ":" + file, status); }else{
		 * // showResult("Failed", status); } }
		 * 
		 * }; aq.progress(pb).download(video_urls.get(5), target,call );
		 * call.abort();
		 */

	}

	private void openDirectoryChooser() {

		boolean m_newFolderEnabled = true;
		DirectoryChooserDialog directoryChooserDialog = new DirectoryChooserDialog(
				FullView.this,
				new DirectoryChooserDialog.ChosenDirectoryListener() {
					@Override
					public void onChosenDir(String chosenDir) {
						m_chosenDir = chosenDir;
						dir.setText(chosenDir);
					}
				});
		directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
		directoryChooserDialog.chooseDirectory("");
		m_newFolderEnabled = !m_newFolderEnabled;
	}

}
