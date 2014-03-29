package com.main.jngroup.jnhelper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nove1398 on 3/22/2014.
 */
public class JNPreferences {
    private final static String PREFERENCES_TITLE = "jnPrefs";
    private final static String FIRST_TIME_RUN    = "firstRun";
    private final static String FIRST_NAME       = "firstName";
    private final static String LAST_NAME        = "lastName";
    private final static String EMAIL            = "email";
    private final static String PASSWORD         = "password";
    private final static String TELEPHONE        = "password";
    private final static String PHOTO            = "password";
    private final static String STATUS           = "status";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEdit;

    public JNPreferences( Context ctx ){
        this.mPrefs = ctx.getSharedPreferences( PREFERENCES_TITLE, 0 );
        this.mEdit  = mPrefs.edit();
    }


    public void setFirstTimeRun(boolean run){
        mEdit.putBoolean( FIRST_TIME_RUN, run ).commit();
    }

    public boolean getFirstTimeRun(){
        return mPrefs.getBoolean( FIRST_TIME_RUN, true );
    }

    public void setFirstName(String fName){
        mEdit.putString( FIRST_NAME, fName ).commit();
    }

    public String getFirstName(){
        return mPrefs.getString( FIRST_NAME, "" );
    }

    public void setLastName(String lName){
        mEdit.putString( LAST_NAME, lName ).commit();
    }

    public String getLastName(){
        return mPrefs.getString( LAST_NAME, "" );
    }

    public void setEmail(String email){
        mEdit.putString( EMAIL, email ).commit();
    }

    public String getEmail(){
        return mPrefs.getString( EMAIL, "" );
    }

    public void setTelephone(String phone){
        mEdit.putString( TELEPHONE, phone ).commit();
    }

    public String getTelephone(){
        return mPrefs.getString( TELEPHONE, "" );
    }

    public void setPassword(String password){
        mEdit.putString( PASSWORD, password ).commit();
    }

    public String getPassword(){
        return mPrefs.getString( PASSWORD, "" );
    }

    public void setPhoto(String photoPath){
        mEdit.putString( PHOTO, photoPath ).commit();
    }

    public String getPhoto(){
        return mPrefs.getString( PHOTO, "" );
    }

    public void setStatus(int status){
        mEdit.putInt( STATUS, status ).commit();
    }

    public int getStatus(){
        return mPrefs.getInt( STATUS, 0 );
    }
}
