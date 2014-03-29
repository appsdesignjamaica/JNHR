package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.main.jngroup.R;
import com.main.jngroup.jnhelper.TextHelper;

public class UploadActivity extends Activity {
    private int UPLOAD_FILE_REQUEST_CODE = 533;
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
                                Intent uploadIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                uploadIntent.setType( "image/*" );
                                startActivityForResult( uploadIntent, UPLOAD_IMAGE_REQUEST_CODE );
                                break;
                            case 1:
                                Intent uploadIntent2 = new Intent(Intent.ACTION_GET_CONTENT);
                                uploadIntent2.setType( "video/*" );
                                startActivityForResult( uploadIntent2, UPLOAD_VIDEO_REQUEST_CODE );
                                break;
                            case 2:
                                Intent uploadIntent3 = new Intent(Intent.ACTION_GET_CONTENT);
                                uploadIntent3.setType( "application/pdf" );
                                startActivityForResult( uploadIntent3, UPLOAD_FILE_REQUEST_CODE );
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
           if(requestCode == UPLOAD_IMAGE_REQUEST_CODE){
               Toast.makeText( this, "UPLOAD IMAGE", Toast.LENGTH_LONG ).show();
           }else if(requestCode == UPLOAD_VIDEO_REQUEST_CODE){
               Toast.makeText( this, "UPLOAD VIDEO", Toast.LENGTH_LONG ).show();
           }else if(requestCode == UPLOAD_FILE_REQUEST_CODE){
               Toast.makeText( this, "UPLOAD PDF FILE", Toast.LENGTH_LONG ).show();
           }
        }
        if(resultCode == RESULT_CANCELED){
            Toast.makeText( this, "Request canceled", Toast.LENGTH_LONG ).show();
        }
    }
}
