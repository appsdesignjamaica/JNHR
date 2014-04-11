package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.main.jngroup.R;
import com.main.jngroup.jnhelper.TextHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

public class UploadActivity extends Activity {
    private int UPLOAD_PDF_REQUEST_CODE = 533;
    private int UPLOAD_VIDEO_REQUEST_CODE = 534;
    private int UPLOAD_IMAGE_REQUEST_CODE = 535;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        TextHelper.setTextTypeface( this, (TextView) findViewById( R.id.textView ) );
    }


    //Handle upload button
    public void uploadFile(View v){
        CharSequence[] cs = {"Image","Video","PDF"};
        new AlertDialog.Builder( this ).setTitle( "Select file type..." )
                .setItems( cs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int item ) {
                        switch( item ){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult( intent, UPLOAD_IMAGE_REQUEST_CODE );
                                break;
                            case 1:
                                Intent uploadIntent2 = new Intent(Intent.ACTION_PICK);
                                uploadIntent2.setType( "video/*" );
                                startActivityForResult( uploadIntent2, UPLOAD_VIDEO_REQUEST_CODE );
                                break;
                            case 2:
                                Intent uploadIntent3 = new Intent(Intent.ACTION_GET_CONTENT);
                                uploadIntent3.addCategory(Intent.CATEGORY_OPENABLE);
                                uploadIntent3.setType( "*/*" );
                                startActivityForResult( uploadIntent3, UPLOAD_PDF_REQUEST_CODE );
                                break;
                            default:
                                break;
                        }
                    }
                } ).create().show();
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if(resultCode == RESULT_OK){
            AsyncHttpClient client = new AsyncHttpClient(  );
            int fileType = 0;
            String fileExt = null;
            boolean error = false;
           if(requestCode == UPLOAD_IMAGE_REQUEST_CODE){
               fileType = 3;
               fileExt = ".jpg";

           }else if(requestCode == UPLOAD_VIDEO_REQUEST_CODE){
               fileType = 1;
               fileExt = ".mp4";
           }else{
                   if( data.getData().getLastPathSegment().endsWith( "pdf" ) ||
                           data.getData().getLastPathSegment().endsWith( "PDF" ) ) {
                       fileType = 2;
                       fileExt = "test.pdf";
                   }else{
                       Toast.makeText( this, "Invalid file type chosen", Toast.LENGTH_LONG ).show();
                       error = true;
                   }
           }
            if(!error)
                uploadImageToServer( client, data, fileType, fileExt );
        }
        if(resultCode == RESULT_CANCELED){
            Toast.makeText( this, "Request canceled", Toast.LENGTH_LONG ).show();
        }
    }

    private void uploadImageToServer( final AsyncHttpClient client, Intent data, int fileType, String fileExt ) {
        String url = getString( R.string.jngroup_request_url );

        RequestParams params = new RequestParams(  );
        params.put(  "op","jnuploadarticle" );
        params.put( "iduser", String.valueOf( 1 ) );
        params.put( "type", String.valueOf( fileType ) );
        try {
            if(fileType != 2)
                params.put( "file", getContentResolver().openInputStream( data.getData() ), fileExt );
            else
                params.put( "file", new File(  data.getData().getPath() ) );
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        }

        Log.e( getClass().getName(), data.getData().getPath() );
        client.post(UploadActivity.this, url, params, new JsonHttpResponseHandler() {
            boolean userCancel = true;
            ProgressDialog progressDialog = new ProgressDialog( UploadActivity.this  );
            @Override
            public void onStart() {
                progressDialog.setIndeterminate( true );
                progressDialog.setMessage( "Uploading file, please wait..." );
                progressDialog.setOnDismissListener( new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss( DialogInterface dialogInterface ) {
                        if(userCancel) {
                            client.cancelRequests( UploadActivity.this, true );
                            Toast.makeText( UploadActivity.this, "Upload canceled", Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
                progressDialog.show();
            }

            @Override
            public void onSuccess( JSONObject response ) {
                super.onSuccess( response );
                String status = null;
                try {
                    status = response.getJSONObject( "JNGroup" ).getString( "success" );
                } catch( JSONException e ) {
                    e.printStackTrace();
                }
                if( "true".equals( status ) ) {
                    createDialog( "File uploaded successfully" );
                } else {
                    createDialog( "There was an error while uploading your image, please try again" );
                }
                userCancel = false;
                progressDialog.dismiss();
                Log.e( getClass().getName(), response.toString() + status );
            }

            @Override
            public void onFailure( Throwable e, JSONObject errorResponse ) {
                super.onFailure( e, errorResponse );
                userCancel = false;
                progressDialog.dismiss();
                Log.e( getClass().getName(), e.getMessage() );
            }

            @Override
            public void onProgress( int bytesWritten, int totalSize ) {
                super.onProgress( bytesWritten, totalSize );
                Log.e( getClass().getName(), bytesWritten*100/totalSize+"" );
            }
        } );
    }

    private void createDialog(String message){
        new AlertDialog.Builder( this )
                .setTitle( "Upload status" )
                .setMessage( message )
                .setNeutralButton( "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                        dialogInterface.dismiss();
                    }
                } ).create().show();
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
