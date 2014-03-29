package com.main.jngroup.jnhelper;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GetAssetsBytes {
	Context c;
	public GetAssetsBytes(Context c){
		this.c=c;
	}

public  byte[] getByteArray(String url){
	 AssetManager assetManager = c.getAssets();

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try {
      byte[] chunk = new byte[4096];
      int bytesRead;
      InputStream stream = assetManager.open(url);

      while ((bytesRead = stream.read(chunk)) > 0) {
          outputStream.write(chunk, 0, bytesRead);
      }

    } catch (IOException e) {
           e.printStackTrace();
       }

    return outputStream.toByteArray();
}
}
