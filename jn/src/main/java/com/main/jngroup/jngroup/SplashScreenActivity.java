package com.main.jngroup.jngroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import com.main.jngroup.R;
import com.main.jngroup.custom.GifDecoderView;
import com.main.jngroup.jnhelper.AppConstant;
import com.main.jngroup.jnhelper.GetAssetsBytes;
import com.main.jngroup.jnhelper.JNUtils;


public class SplashScreenActivity extends Activity {
	GetAssetsBytes gab = null;
	private Handler Handle = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_splash_screen);

        if( !JNUtils.hasIntenernetConnection( this )){
            Toast.makeText( this, "You must be connected to the internet in order to proceed", Toast.LENGTH_LONG ).show();
            Handle.removeCallbacks( null );
            new Handler(  ).postDelayed( new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(new Intent( Settings.ACTION_SETTINGS));
                }
            }, 2000 );

        }else {

            gab = new GetAssetsBytes( this );

            final GifDecoderView gdv = (GifDecoderView) findViewById( R.id.ani_photo );

            gdv.setBytes( gab.getByteArray( "anigif/ajax.gif" ) );
            gdv.startAnimation();
            Handle.postDelayed( new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    gdv.stopAnimation();
                    finish();
                    startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
                }

            }, AppConstant.SPLASH_TIME );
            /*Change splash time back to 10 seconds */
        }
	}

}
