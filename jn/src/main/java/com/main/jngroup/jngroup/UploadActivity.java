package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.main.jngroup.R;
import com.main.jngroup.jnhelper.TextHelper;

import org.json.JSONException;
import org.json.JSONObject;

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
                                Intent uploadIntent3 = new Intent(Intent.ACTION_PICK);
                                uploadIntent3.setType( "application/pdf" );
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
           if(requestCode == UPLOAD_IMAGE_REQUEST_CODE){
              fileType = 3;
           }else if(requestCode == UPLOAD_VIDEO_REQUEST_CODE){
            fileType = 1;
           }else if(requestCode == UPLOAD_PDF_REQUEST_CODE ){
              fileType = 2;
           }
            uploadImageToServer( client, data, fileType );
        }
        if(resultCode == RESULT_CANCELED){
            Toast.makeText( this, "Request canceled", Toast.LENGTH_LONG ).show();
        }
    }

    private void uploadImageToServer( AsyncHttpClient client, Intent data, int fileType ) {
        String url = getString( R.string.jngroup_request_url );

        RequestParams params = new RequestParams(  );
        params.put(  "op","jnuploadarticle" );
        params.put( "iduser", String.valueOf( 1 ) );
        params.put( "type", String.valueOf( fileType ) );
        try {
            params.put( "file", getContentResolver().openInputStream( data.getData() ),".jpg" );
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
        Log.e( getClass().getName(), data.getData().toString() );
        client.post( url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess( JSONObject response ) {
                super.onSuccess( response );
                String status = null;
                try {
                    status = response.getJSONObject( "JNGroup" ).getString( "success" );
                } catch( JSONException e ) {
                    e.printStackTrace();
                }
                if( "false".equals( status ) ) {
                    createDialog( "There was an error while uploading your image, please try again" );
                } else {
                    createDialog( "Image uploaded successfully" );
                }
                Log.e( getClass().getName(), response.toString() + status );
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


}
