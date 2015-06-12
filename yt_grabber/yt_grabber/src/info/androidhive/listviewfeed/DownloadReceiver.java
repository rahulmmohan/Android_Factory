package info.androidhive.listviewfeed;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DownloadReceiver extends BroadcastReceiver {

	private static final String tag = DownloadReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

		} else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
			
			intent.getExtras();
		}
	}
}