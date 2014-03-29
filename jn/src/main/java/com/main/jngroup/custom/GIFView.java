package com.main.jngroup.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.main.jngroup.R;

import java.io.InputStream;

public class GIFView extends ImageView  {
	private Movie mMovie;
	private long movieStart;
	private int gifId;
	public GIFView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initializeView();
	}
	public GIFView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	   setAttrs(attrs);
	    initializeView();
	}

	public GIFView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	  setAttrs(attrs);
	    initializeView();
	}
	
	public void setGIFResource(int resId) {
	    this.gifId = resId;
	    initializeView();
	}

	public int getGIFResource() {
	    return this.gifId;
	}
	private void setAttrs(AttributeSet attrs) {
	    if (attrs != null) {
	        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GIFView, 0, 0);
	        String gifSource = a.getString(R.styleable.GIFView_src);
	        String sourceName = Uri.parse(gifSource).getLastPathSegment().replace(".gif", "");
	        //Log.d("SOURCENAME",sourceName);
	        setGIFResource(getResources().getIdentifier(sourceName, "drawable", getContext().getPackageName()));
	        a.recycle();
	    }
	}
	private void initializeView() {
		if(gifId!=0){
	    InputStream is = getContext().getResources().openRawResource(gifId);
	    mMovie = Movie.decodeStream(is);
	    movieStart = 0;
	    this.invalidate();
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		 canvas.drawColor(Color.TRANSPARENT);
		    super.onDraw(canvas);
		    long now = android.os.SystemClock.uptimeMillis();
		    if (movieStart == 0) {
		        movieStart = now;
		    }
		    if (mMovie != null) {
		        int relTime = (int) ((now - movieStart) % mMovie.duration());
		        mMovie.setTime(relTime);
		        mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
		        this.invalidate();
		    }
	}
}
