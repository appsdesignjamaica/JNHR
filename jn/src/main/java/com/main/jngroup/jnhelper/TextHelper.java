package com.main.jngroup.jnhelper;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

public class TextHelper {
	 private static Typeface typeface = null;
	  
	  public static void setTypeface(Context context, Button btn){
	    if(typeface == null){
	     typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vegur_Bold.otf");
	    }
	    btn.setTypeface(typeface); 
	  }

	  public static void setArrTypeface(Context c,Button[] btn){
		  if(typeface == null){
			     typeface = Typeface.createFromAsset(c.getAssets(), "fonts/Vegur_Bold.otf");
			    }
		  for(Button b : btn ){
			  if(b!=null)
			  b.setTypeface(typeface);
		  }
	  }

    public static void setTextTypeface(Context context, TextView view){
        if(typeface == null){
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Vegur_Bold.otf");
        }
        view.setTypeface(typeface);
    }
}
