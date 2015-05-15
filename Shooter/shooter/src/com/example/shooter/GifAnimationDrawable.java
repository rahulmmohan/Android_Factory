
package com.example.shooter;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;

public class GifAnimationDrawable extends AnimationDrawable
{
	private boolean decoded;
	
	private GifDecoder mGifDecoder;

	private Bitmap mTmpBitmap;

	private int height, width;
	
	public GifAnimationDrawable(File f) throws IOException
	{
		this(f, false);
	}
	
	public GifAnimationDrawable(InputStream is) throws IOException
	{
		this(is, false);
	}
	
	public GifAnimationDrawable(File f, boolean inline) throws IOException
	{
	    this(new BufferedInputStream(new FileInputStream(f), 32768), inline);
	}
	
	public GifAnimationDrawable(InputStream is, boolean inline) throws IOException
	{
		super();
		InputStream bis = is;
		if(!BufferedInputStream.class.isInstance(bis)) bis = new BufferedInputStream(is, 32768);
		decoded = false;
		mGifDecoder = new GifDecoder();
		mGifDecoder.read(bis);
		mTmpBitmap = mGifDecoder.getFrame(0);
		height = mTmpBitmap.getHeight();
    	width = mTmpBitmap.getWidth();
        addFrame(new BitmapDrawable(mTmpBitmap), mGifDecoder.getDelay(0));
        setOneShot(mGifDecoder.getLoopCount() != 0);
        setVisible(true, true);
		if(inline){
			loader.run();
		}else{
			new Thread(loader).start();
		}
	}
	
	public boolean isDecoded(){ return decoded; }
	
	private Runnable loader = new Runnable(){
		public void run() 
		{
			mGifDecoder.complete();
			int i, n = mGifDecoder.getFrameCount(), t;
	        for(i=1;i<n;i++){
	            mTmpBitmap = mGifDecoder.getFrame(i);
	            t = mGifDecoder.getDelay(i);
	            addFrame(new BitmapDrawable(mTmpBitmap), t);
	        }
	        decoded = true;
	        mGifDecoder = null;
	    }
	};
	
	public int getMinimumHeight(){ return height; }
	public int getMinimumWidth(){ return width; }
	public int getIntrinsicHeight(){ return height; }
	public int getIntrinsicWidth(){ return width; }
}
