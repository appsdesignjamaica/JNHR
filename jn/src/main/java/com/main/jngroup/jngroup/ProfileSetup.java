package com.main.jngroup.jngroup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.main.jngroup.R;
import com.main.jngroup.jnhelper.JNPreferences;
import com.main.jngroup.jnhelper.TextHelper;

public class ProfileSetup extends Activity {
    private String mChosenPicturePath;
    private int REQUEST_PICTURE = 12345;
    private EditText[] mInputTextBoxes = new EditText[4];
    private ImageView mProfileImageView, mStatusImageView;
    private JNPreferences mJNPrefs;
    private TextView mStatusPresence;
    final String[] statusItems = {"Available", "Busy", "Away"};
    final int[] statusImages = {android.R.drawable.presence_online,android.R.drawable.presence_busy,
                          android.R.drawable.presence_away};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        mJNPrefs = new JNPreferences( this );
        initViews();
        initProfile();
    }

    //INITIALIZE FIELD VIEWS
    private void initViews() {
        mInputTextBoxes[0] = (EditText)findViewById( R.id.fnameEditText );
        mInputTextBoxes[1] = (EditText)findViewById( R.id.lnameEditText );
        mInputTextBoxes[2] = (EditText)findViewById( R.id.emailEditText );
        mInputTextBoxes[3] = (EditText)findViewById( R.id.telEditText );
        mStatusPresence    = (TextView)findViewById( R.id.statusPresenceText );
        mProfileImageView  = (ImageView)findViewById( R.id.profileImageView );
        mStatusImageView   = (ImageView)findViewById( R.id.statusPresenceImage );
        TextHelper.setTextTypeface( this, (TextView) findViewById( R.id.textView ) );
        mProfileImageView.requestFocus();
    }

    //INITIALIZE PROFILE DATA
    private void initProfile(){
        mStatusPresence.setText( statusItems[mJNPrefs.getStatus()] );
        mStatusImageView.setImageResource( statusImages[mJNPrefs.getStatus()] );
        mInputTextBoxes[0].setText( mJNPrefs.getFirstName() );
        mInputTextBoxes[1].setText( mJNPrefs.getLastName() );
        mInputTextBoxes[2].setText( mJNPrefs.getEmail() );
        mInputTextBoxes[3].setText( mJNPrefs.getTelephone() );
        if(!mJNPrefs.getPhoto().equalsIgnoreCase( "" ))
            mProfileImageView.setImageURI( Uri.parse(mJNPrefs.getPhoto()) );
    }

    //SUBMIT INPUT FOR UPDATE
    public void parseInput(View v){
        boolean error = false;
        for(EditText et : mInputTextBoxes){
           if( TextUtils.isEmpty(et.getText().toString())){
               error = true;
               et.setError( Html.fromHtml( "<font color='red'>This field cannot be empty</font>" ) );
           }
        }
        if(!Patterns.EMAIL_ADDRESS.matcher( mInputTextBoxes[2].getText().toString() ).matches() ) {
            error = true;
            mInputTextBoxes[2].setError( Html.fromHtml( "<font color='red'>Invalid email entered</font>" ) );
        }
        if(!error)
            writeToast( "Updated your data" );
    }

    //CHANGE PASSWORD
    @SuppressLint("InflateParams")
    public void changePassword(View v){
        View passwordView = getLayoutInflater().inflate( R.layout.dialog_password, null, false );
        final Dialog d = new Dialog( this );
        d.requestWindowFeature( Window.FEATURE_NO_TITLE );
        d.setContentView( passwordView, new ViewGroup.LayoutParams( 450, LinearLayout.LayoutParams.MATCH_PARENT ) );
        final EditText password1 = (EditText)d.findViewById( R.id.dialogPassword1 );
        final EditText password2 = (EditText)d.findViewById( R.id.dialogPassword2 );
        password1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                password1.setError( null );
            }
        } );
        password2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                password2.setError( null );
            }
        } );
        d.findViewById( R.id.dialogConfirmButton ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                String pass1 = password1.getText().toString().trim();
                String pass2 = password2.getText().toString().trim();
                if(!pass1.isEmpty()){
                    if(pass2.equals( pass1 ))
                        writeToast( "Password has been updated" );
                    else
                        password2.setError( "Does not match previous field" );
                }else
                    password1.setError( "Must not be empty" );

            }
        } );
        d.findViewById( R.id.dialogCancelButton ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                d.dismiss();
            }
        } );
       d.show();
    }

    //CANCEL BUTTON
    public void cancelProfile(View v){
            finish();
    }

    //INTENT TO PICK PHOTO
    public void pickPhoto(View v){
        Intent pickPhotoIntent = new Intent( Intent.ACTION_GET_CONTENT );
        pickPhotoIntent.setType( "image/*" );
        startActivityForResult(Intent.createChooser(pickPhotoIntent,"Select your profile photo" ), REQUEST_PICTURE);
    }


    //WRITES TOAST
    private void writeToast(String text){
        Toast.makeText( this,text, Toast.LENGTH_LONG ).show();
    }

    //CHANGE STATUS
    public void changeUserPresence(View v){

       new AlertDialog.Builder( this ).setItems( statusItems,new DialogInterface.OnClickListener() {
           @Override
           public void onClick( DialogInterface dialogInterface, int item ) {
                    mStatusPresence.setText( statusItems[item] );
                    mStatusImageView.setImageResource( statusImages[item] );
                    mJNPrefs.setStatus( item );

           }
       } ).setTitle( "I am...." ).create().show();
    }

    //RESAMPLE IMAGE
    private void scaleDownUserPhoto(){

    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_PICTURE){
                mChosenPicturePath = data.getData().toString();
                mProfileImageView.setImageURI( Uri.parse( mChosenPicturePath ) );
                //TODO:update user photo on backend
            }
        }
    }
}
