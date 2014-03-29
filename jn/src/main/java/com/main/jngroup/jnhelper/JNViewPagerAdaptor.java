package com.main.jngroup.jnhelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.main.jngroup.R;
import com.main.jngroup.jngroup.ArticlesActivity;
import com.main.jngroup.jngroup.DepartmentsActivity;
import com.main.jngroup.jngroup.ProfileSetup;
import com.main.jngroup.jngroup.SearchActivity;
import com.main.jngroup.jngroup.UploadActivity;

public class JNViewPagerAdaptor extends PagerAdapter {
	int []arr;
	String[]narr;
    Activity act;
    View layout;
    TextView pagenumber;
    Button click;
    JNPreferences mJnPrefs;

    public JNViewPagerAdaptor(Activity a, int []arr,String[]narr){
    	this.act = a;
    	this.arr = arr;
    	this.narr = narr;
        this.mJnPrefs = new JNPreferences( a );
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.arr.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == ((View) arg1);
	}
	@Override
	public Object instantiateItem(View container, final int position) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.pages, null);
        click = (Button)layout.findViewById(R.id.imageBtn2_Upload);
        click.setText(narr[position]);
        TextHelper.setTypeface(this.act.getApplicationContext(), click);
        click.setCompoundDrawablesWithIntrinsicBounds(0, arr[position], 0, 0);
        click.getBackground().setAlpha(128);
        click.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(position){
				case 0:
                    act.startActivity( new Intent( act, UploadActivity.class ) );
					break;
				case 1:
					 Toast.makeText(act, "Chat: "+position, Toast.LENGTH_SHORT).show();
				break;
				case 2:
                        act.startActivity( new Intent( act, ProfileSetup.class ) );
					 break;
				case 3:
                    act.startActivity( new Intent( act, ArticlesActivity.class ) );
					break;
				case 4:
					 Toast.makeText(act, "Messaging: "+position, Toast.LENGTH_SHORT).show();
					 break;
				case 5:
					 Toast.makeText(act, "Reminder: "+position, Toast.LENGTH_SHORT).show();
					 break;
				case 6:
                    act.startActivity( new Intent( act, DepartmentsActivity.class ) );
					 break;
				case 7:
                    act.startActivity( new Intent( act, SearchActivity.class ) );
                    break;
                default:
						 Toast.makeText(act, "Invalid choice selected", Toast.LENGTH_SHORT).show();
					
				}
				
			}
		});
        ((ViewPager) container).addView(layout, 0);
        return layout;
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((View) object);
	}

}
