package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.main.jngroup.R;
import com.main.jngroup.jnhelper.JNPreferences;
import com.main.jngroup.jnhelper.JNViewPagerAdaptor;
import com.main.jngroup.jnhelper.TextHelper;


public class MainActivity extends Activity implements ViewPager.OnPageChangeListener{
    private Button upload,chat,profile,articles,messaging,reminder,department,search;
    private JNPreferences mJnPrefs;
    private ViewPager myPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_main);

        mJnPrefs = new JNPreferences(this);

        String[] namArr = new String[]{ getResources().getString( R.string.upload ),
                getResources().getString( R.string.chat ),
                getResources().getString( R.string.profile ),
                getResources().getString( R.string.articles ), getResources().getString( R.string.messaging ),
                getResources().getString( R.string.reminder ),
                getResources().getString( R.string.department ), getResources().getString( R.string.search ) };

        int[] imgArr = new int[]{ R.drawable.upload, R.drawable.chat, R.drawable.profile, R.drawable.articles,
                R.drawable.messaging, R.drawable.reminder, R.drawable.department, R.drawable.search };

		   JNViewPagerAdaptor adapter = new JNViewPagerAdaptor(this, imgArr, namArr );

	        myPager = (ViewPager) findViewById(R.id.reviewpager);
            myPager.setOnPageChangeListener( this );
	        myPager.setAdapter(adapter);
	        myPager.setCurrentItem(0);

            if(myPager.getCurrentItem() == 0)
                findViewById( R.id.imageView1 ).setVisibility( View.INVISIBLE );


	        upload = (Button)findViewById(R.id.imageBtnupload);
	        upload.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
                    startActivity( new Intent( MainActivity.this, UploadActivity.class ) );
				}
	        	
	        });
	       // upload.setTypeface(tf)
	        chat=(Button)findViewById(R.id.imageBtn6);
	        chat.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "Chat: ", Toast.LENGTH_SHORT).show();
				}
	        	
	        });
	        profile=(Button)findViewById(R.id.imageBtn7);
	       profile.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
                    if(mJnPrefs.getFirstTimeRun())
					    startActivity( new Intent( MainActivity.this, ProfileSetup.class ) );
				}
	        	
	        });
	       articles=(Button)findViewById(R.id.imageBtn8);
	       articles.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
                    final CharSequence[] articleType = {"PDF","Images", "Videos"};
                    new AlertDialog.Builder( MainActivity.this )
                            .setTitle( "Select the type of article needed" )
                            .setSingleChoiceItems( articleType, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick( DialogInterface dialogInterface, int item ) {
                                    Intent fetchIntent = new Intent( MainActivity.this, ArticlesActivity.class );
                                    fetchIntent.putExtra( "type", item );
                                    startActivity(fetchIntent);
                                }
                            } ).create().show();

				}
	        	
	        });
	       messaging=(Button)findViewById(R.id.imageBtn1);
	       messaging.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
                    startActivity( new Intent( MainActivity.this, MessagingActivity.class ) );
				}
	        	
	        });
	       reminder=(Button)findViewById(R.id.imageBtn2);
	      reminder.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
                    startActivity( new Intent( MainActivity.this, ReminderActivity.class ) );
				}
	        	
	        });
	      department=(Button)findViewById(R.id.imageBtn3);
	       department.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					startActivity( new Intent( MainActivity.this, DepartmentsActivity.class ) );
				}
	        	
	        });
	       search=(Button)findViewById(R.id.imageBtn4);
	       search.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
                    startActivity( new Intent( MainActivity.this, SearchActivity.class ) );
				}
	        	
	        });
	       Button[] btn = {upload,chat,profile,articles,messaging,reminder,department,search};
	       TextHelper.setArrTypeface( this, btn );
	}

    /**
     * Handle next button presses
     * @param v
     */
    public void nextPage(View v){
        myPager.setCurrentItem( ( myPager.getCurrentItem() + 1 ), true );

        if( 7 == myPager.getCurrentItem()){
            findViewById( R.id.imageView2 ).setVisibility( View.GONE );
        }else {
            findViewById( R.id.imageView1 ).setVisibility( View.VISIBLE );
        }
    }

    /**
     * Handle back arrow presses
     * @param v
     */
    public void prevPage(View v){
        myPager.setCurrentItem( ( myPager.getCurrentItem() - 1 ), true );

        if( 0 == myPager.getCurrentItem()){
            findViewById( R.id.imageView1 ).setVisibility( View.GONE );
        }else {
            findViewById( R.id.imageView2 ).setVisibility( View.VISIBLE );
        }
    }

    @Override
    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ) {
        switch( position ){
            case 0:
                findViewById( R.id.imageView1 ).setVisibility( View.GONE );
                break;
            case 7:
                findViewById( R.id.imageView2 ).setVisibility( View.GONE );
                break;
            default:
                findViewById( R.id.imageView1 ).setVisibility( View.VISIBLE );
                findViewById( R.id.imageView2 ).setVisibility( View.VISIBLE );
                break;
        }


    }

    @Override
    public void onPageSelected( int position ) {


    }

    @Override
    public void onPageScrollStateChanged( int state ) { }
}
