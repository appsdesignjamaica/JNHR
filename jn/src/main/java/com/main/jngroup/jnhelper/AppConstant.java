package com.main.jngroup.jnhelper;

import android.os.Environment;

import java.io.File;

public class AppConstant {
	  public static final int SPLASH_TIME = 2000;
      public static final String PICTURE_FOLDER = Environment.getExternalStorageDirectory()+ File.separator+"JNGroup"+
                                                  File.separator+"Pictures";
    public static final int GET_REQUEST = 2;
    public static final int POST_REQUEST = 1;

}
