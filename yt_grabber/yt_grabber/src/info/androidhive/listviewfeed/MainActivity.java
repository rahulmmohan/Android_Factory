package info.androidhive.listviewfeed;

import info.androidhive.listviewfeed.adapter.FeedListAdapter;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.FeedItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends AppCompatActivity {
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	private String URL_FEED = "https://zazkov-youtube-grabber-v1.p.mashape.com/search.video.php?";
	DrawerLayout mDrawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	//ActionBar ab;
	FeedImageView feedImageView;
	RelativeLayout mdrawer;
	ProgressBar pg;
	String queryString="",nextPageHash="";
	 TextView load_more;
	 ProgressBar loding_pr;
	 Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		initViews();
		listAdapter = new FeedListAdapter(this, feedItems);
		listView.setAdapter(listAdapter);
		//checkCache();

	}

	private void checkCache() {
		showSearch();
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get("");
		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

	}

	private void initViews() {
		/*	ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setDisplayShowTitleEnabled(true);*/
		 toolbar = (Toolbar) findViewById(R.id.toolbar);
		 if (toolbar != null) {
	            toolbar.setTitle("Navigation Drawer");
	            setSupportActionBar(toolbar);
	        }
		mdrawer = (RelativeLayout) findViewById(R.id.drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,
				R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		listView = (ListView) findViewById(R.id.list);
		feedItems = new ArrayList<FeedItem>();
		feedImageView = (FeedImageView) findViewById(R.id.full_image);
		pg = (ProgressBar) findViewById(R.id.progressBar1);
		View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.list_footer, null, false);
		 loding_pr=(ProgressBar)footerView.findViewById(R.id.Loading_progress);
		 load_more=(TextView)footerView.findViewById(R.id.load_textview);
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				load_more.setVisibility(View.GONE);
				loding_pr.setVisibility(View.VISIBLE);
				get(queryString);
			}
		});
		listView.addFooterView(footerView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				FeedItem item = feedItems.get(arg2);
				Intent i=new Intent(getApplicationContext(),FullView.class);
				i.putExtra("image", item.getImge());
				i.putExtra("title", item.getName());
				i.putExtra("id", item.getId());
				startActivity(i);
				
				
			}
		});
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	public void get(final String q) {

		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(URL_FEED);
		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			// making fresh volley request and getting json
			String url = URL_FEED + "maxResults=10&query="
					+ q.replace(" ", "%20");
			if(!nextPageHash.contentEquals(""))
			{
				url=url+"&pageToken="+nextPageHash;
			}
			JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
					url, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							parseJsonFeed(response);
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

			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonObjReq, "recent");
		}
	}

	private void parseJsonFeed(JSONObject response) {
		try {
			
			JSONArray feedArray = response.getJSONArray("data");
			
			JSONObject feedObj;
			JSONObject img=response.getJSONObject("control");
			nextPageHash=img.getString("nextPageToken");
			for (int i = 0; i < feedArray.length(); i++) {
				feedObj = (JSONObject) feedArray.get(i);
				img = (JSONObject) feedObj.getJSONObject("img");

				FeedItem item = new FeedItem();
				item.setId(feedObj.getString("videoId"));
				item.setName(feedObj.getString("title"));
				String image = img.isNull("medium") ? null : img
						.getString("medium");
				item.setImge(image);
				item.setStatus(feedObj.getString("description"));
				feedItems.add(item);
			}

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		hideSearch();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView search = (SearchView) searchItem.getActionView();
		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				nextPageHash="";
				queryString=query;
				showSearch();
				get(queryString);

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		return true;
	}

	protected void showSearch() {
		feedItems.clear();
		pg.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
	}

	protected void hideSearch() {
		listView.setVisibility(View.VISIBLE);
		pg.setVisibility(View.GONE);
		load_more.setVisibility(View.VISIBLE);
		loding_pr.setVisibility(View.GONE);
	}

}