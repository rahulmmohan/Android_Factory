package info.androidhive.listviewfeed;

import java.io.InputStream;
import java.text.Format.Field;
import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

public class MyCustom_GridAdapter extends ArrayAdapter<String> {

	private final Context context;
	ArrayList<String> video_format;

	public MyCustom_GridAdapter(Context context, 
			ArrayList<String> item) {
		super(context, R.layout.activity_main, item);

		this.context = context;
		this.video_format = item;


	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		 View rowView;

		try {
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.download_option_grid_item, parent, false);
			
			ImageView image = (ImageView) rowView.findViewById(R.id.format_image);
			image.setScaleType(ImageView.ScaleType.FIT_XY);
			String imagename=video_format.get(position).substring(6);
			InputStream ims = context.getAssets().open(imagename+".png");
		    Drawable d = Drawable.createFromStream(ims, null);
		    image.setImageDrawable(d);

			
		} catch (Exception e) {
			Log.e("MyCustom_ListAdapter:getView()", e.toString());
			return null;
		}

		return rowView;
	}

}
