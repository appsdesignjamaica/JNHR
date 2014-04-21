package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.main.jngroup.R;
import com.main.jngroup.jnhelper.JNUtils;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }



    public void userLogin(View v){

        if(!JNUtils.hasInternetConnection( this )){
            new AlertDialog.Builder( this )
                    .setTitle( "Something went wrong" )
                    .setMessage( "While trying to log you in, we detected you are not currently connected to the " +
                            "internet, would you like to connect and try again?" )
                    .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick( DialogInterface dialogInterface, int i ) {
                            dialogInterface.dismiss();
                            startActivity(new Intent( Settings.ACTION_SETTINGS));
                        }
                    } )
                    .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick( DialogInterface dialogInterface, int i ) {
                            dialogInterface.dismiss();
                        }
                    } )
                    .create().show();
        }else {
            startActivity( new Intent( this, SplashScreenActivity.class ) );
            finish();
        }
    }

    public void exitApp(View v){
        finish();
    }
}
