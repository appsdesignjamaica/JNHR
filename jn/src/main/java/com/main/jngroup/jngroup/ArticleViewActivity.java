package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.main.jngroup.R;
import com.main.jngroup.jnhelper.JNPreferences;

import org.json.JSONObject;

public class ArticleViewActivity extends Activity {
    private LinearLayout mDrawerContainer;
    private boolean isMenuOpen = true;
    private int mArticleId;
    private JNPreferences mPrefs;
    private  ListView mArticleCommentsListView;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        mDrawerContainer = (LinearLayout)findViewById( R.id.menuContainer );
        mPrefs = new JNPreferences( this );
        mArticleCommentsListView  = (ListView) findViewById( R.id.articleViewListView );
        client = new AsyncHttpClient(  );

        Bundle extras = getIntent().getExtras();
        mArticleId = extras.getInt( "id" );
        int articleType = extras.getInt( "type" );
        switch( articleType ){
            case 1:
                loadVideo();
                break;
            case 2:
                loadPdfDocument();
                break;
            case 3:
                loadImage();
                break;
            default:
                break;
        }

        fetchComments();

    }

    private void loadPdfDocument() {
        WebView articleView = (WebView)findViewById( R.id.articleWebView );

        articleView.getSettings().setJavaScriptEnabled( true );
        articleView.setWebViewClient( new WebViewClient() );
        String pdfURL = getIntent().getExtras().getString( "pdf_url" );
        articleView.loadUrl( "http://docs.google.com/gview?embedded=true&url=" + pdfURL );
    }

    private void loadImage(){
        ImageView image = (ImageView)findViewById( R.id.articleImageView );
        image.setVisibility( View.VISIBLE );

    }

    private void loadVideo(){
        Log.e( "ART ID", mArticleId+"" );
        VideoView video = (VideoView)findViewById( R.id.articleVideoView );
        video.setVisibility( View.VISIBLE );
        RequestParams params = new RequestParams(  );
        params.put( "op", "getjnarticlesinfourl" );
        params.put( "idarticle", String.valueOf( mArticleId ) );
        client.post( this,getString( R.string.jngroup_request_url ),params, new JsonHttpResponseHandler(  ){
            @Override
            public void onSuccess( JSONObject response ) {
                Log.e( "VIDEO URL", response.toString() );
            }
        });
    }

    /**
     * DRAWER TOGGLE
     * @param v
     */
    public void toggleDrawer(View v){
        float weight = 1.0f;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            weight = 3.0f;
        if(isMenuOpen) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            isMenuOpen = !isMenuOpen;
            mDrawerContainer.getChildAt( 1 ).setVisibility( View.GONE );
            mDrawerContainer.getChildAt( 2 ).setVisibility( View.GONE );
            mDrawerContainer.setLayoutParams( params );
            ((Button)v).setText( "Open menu" );
        }else{
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,0, weight );
            isMenuOpen = !isMenuOpen;
            mDrawerContainer.getChildAt( 1 ).setVisibility( View.VISIBLE );
            mDrawerContainer.getChildAt( 2 ).setVisibility( View.VISIBLE );
            mDrawerContainer.setLayoutParams( params );
            ((Button)v).setText( "Close menu" );
        }
    }

    /**
     * POST COMMENT TO SERVER
     * @param v
     */
    public void makeComment(View v){
        View commentView = getLayoutInflater().inflate( R.layout.dialog_comment, null, false );

        Dialog alert = new Dialog( this );
        alert.requestWindowFeature( Window.FEATURE_NO_TITLE );
        alert.setContentView( commentView );
        final EditText commentBox = ((EditText)commentView.findViewById( R.id.commentEditText ));
        commentView.findViewById(R.id.commentButton).setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View view ) {
                String comment =  commentBox.getText().toString();
                if( comment.length() < 3 )
                    commentBox.setError( Html.fromHtml( "<font color='red'>Text was too short</font>" ) );
                else {
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put( "op", "postjnarticlecomment" );
                    params.put( "iduser", String.valueOf( 1 ) );
                    params.put( "idarticle", String.valueOf( mArticleId ) );
                    params.put( "body", comment );
                    client.post( getString( R.string.jngroup_request_url ), params,
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess( JSONObject response ) {
                                    commentBox.setText( "" );
                                    Log.e(getClass().getName(), "Sent comment " + response.toString());
                                }
                            } );
                }
            }
        } );
        commentBox.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                commentBox.setError( null );
            }
        } );
        alert.show();
    }

    /**
     * LIKE AN ARTICLE
     */
    public void updateArticleLikes(View v){
        AsyncHttpClient client = new AsyncHttpClient(  );
        RequestParams params = new RequestParams(  );
        params.put( "op", "jnarticlelike" );
        params.put( "iduser", String.valueOf( 1 ) );
        params.put( "idarticle", String.valueOf( mArticleId ) );
        client.post( getString( R.string.jngroup_request_url ), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess( JSONObject response ) {

                Log.e(getClass().getName(), response.toString());
            }
        } );
    }

    /**
     * FETCH COMMENTS
     */
    private void fetchComments(){
        RequestParams params = new RequestParams(  );
        params.put( "op", "getjnarticlescomment"  );
        params.put( "idarticle", String.valueOf( mArticleId ) );
        client.post(this,getString( R.string.jngroup_request_url ), params, new JsonHttpResponseHandler(  ){
            @Override
            public void onSuccess( JSONObject response ) {
                Log.e(getClass().getName(), response.toString());
            }
        });
    }

    /**
     * SET ARTICLE ADAPTER
     */
    public class ArticlesViewAdapter extends BaseAdapter {


        public ArticlesViewAdapter(){



        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public String getItem( int position ) {
            return null;
        }

        @Override
        public long getItemId( int position ) {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup viewGroup ) {
            Holder holder;

            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.row_article_comments , viewGroup, false );
                holder = new Holder();
                holder.comment = (TextView)convertView.findViewById( R.id.articleCommentTextView );
                holder.commenterName = (TextView)convertView.findViewById( R.id.articleCommenterTextView );
                convertView.setTag( holder );
            }else{
                holder = (Holder)convertView.getTag();
            }

            holder.comment.setText( "" );
            holder.commenterName.setText("" );

            return convertView;
        }


        class Holder{
            TextView comment;
            TextView commenterName;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != client)
            client.cancelRequests( this, true );
    }
}
