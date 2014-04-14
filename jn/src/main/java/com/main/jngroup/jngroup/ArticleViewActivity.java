package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.main.jngroup.R;
import com.main.jngroup.jnhelper.JNPreferences;
import com.main.jngroup.objects.CommentObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ArticleViewActivity extends Activity {
    private LinearLayout mDrawerContainer;
    private boolean isMenuOpen = true;
    private int mArticleId;
    private int mArticleLikes;
    private JNPreferences mPrefs;
    private  ListView mArticleCommentsListView;
    private AsyncHttpClient client;
    private TextView mArticleLikesTextView;
    private Dialog mCommentDialog;
    private ArrayList<CommentObject> mCommentsList;
    private ArticlesViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        mDrawerContainer = (LinearLayout)findViewById( R.id.menuContainer );
        mPrefs = new JNPreferences( this );
        mArticleCommentsListView  = (ListView) findViewById( R.id.articleViewListView );
        client = new AsyncHttpClient(  );
        mArticleLikesTextView = (TextView)findViewById( R.id.articleLikesTextView );
        mCommentsList = new ArrayList<CommentObject>(  );

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
            fetchArticleLikes();
            fetchComments();

    }

    /**
     * RESTORE ORIENTATION STATES
     * @param savedInstanceState
     */
    private void restorePreviousState( Bundle savedInstanceState ) {
     //   mCommentsList = savedInstanceState.getParcelableArrayList( "comments" );
        mArticleLikes = savedInstanceState.getInt( "likes" );

        if(null == mAdapter) {
            mAdapter = new ArticlesViewAdapter( mCommentsList );
            mArticleCommentsListView.setAdapter( mAdapter );
        }
        if(null != mArticleLikesTextView )
            mArticleLikesTextView.setText( (mArticleLikes >= 2)?
                    mArticleLikes + " people like this article" :
                    (mArticleLikes == 1)? mArticleLikes + " person like this article" :
                            mArticleLikes + " persons like this article");
    }

    private void loadPdfDocument() {
        RequestParams params = new RequestParams(  );
        params.put( "op", "getjnarticlesinfourl" );
        params.put( "idarticle", String.valueOf( mArticleId ) );
        client.post( this,getString( R.string.jngroup_request_url ),params, new JsonHttpResponseHandler(  ){
            ProgressBar progressBar = (ProgressBar)findViewById( R.id.articleProgressBar );
            @Override
            public void onStart() {
                progressBar.setInterpolator( new LinearInterpolator(  ) );

            }

            @Override
            public void onSuccess( JSONObject response ) {
                progressBar.setVisibility( View.GONE );
                String pdfUrl = null;
                try {
                    pdfUrl = response.getJSONArray( "JNGroup" ).getJSONObject( 0 )
                            .getJSONArray( "articleurl" ).getJSONObject( 0 ).getString( "articleurl" );
                } catch( JSONException e ) {
                    e.printStackTrace();
                }

                WebView articleView = (WebView)findViewById( R.id.articleWebView );
                articleView.setVisibility( View.VISIBLE );
                articleView.getSettings().setJavaScriptEnabled( true );
                articleView.setWebViewClient( new WebViewClient() );
                articleView.loadUrl( "http://docs.google.com/gview?embedded=true&url=" + pdfUrl );
            }
        });

    }

    private void loadImage(){
        RequestParams params = new RequestParams(  );
        params.put( "op", "getjnarticlesinfourl" );
        params.put( "idarticle", String.valueOf( mArticleId ) );
        client.post( this,getString( R.string.jngroup_request_url ),params, new JsonHttpResponseHandler(  ){
            ProgressBar progressBar = (ProgressBar)findViewById( R.id.articleProgressBar );
            @Override
            public void onStart() {
                progressBar.setInterpolator( new LinearInterpolator(  ) );

            }

            @Override
            public void onSuccess( JSONObject response ) {
                progressBar.setVisibility( View.GONE );
                String bmpUrl = null;
                try {
                    bmpUrl = response.getJSONArray( "JNGroup" ).getJSONObject( 0 )
                            .getJSONArray( "articleurl" ).getJSONObject( 0 ).getString( "articleurl" );
                } catch( JSONException e ) {
                    e.printStackTrace();
                }

                ImageView image = (ImageView)findViewById( R.id.articleImageView );
                image.setVisibility( View.VISIBLE );
                try {
                    image.setImageBitmap( BitmapFactory.decodeStream( new FileInputStream( bmpUrl ) ) );
                } catch( FileNotFoundException e ) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * LOADS VIDEO URL FROM SERVER
     */
    private void loadVideo(){

        RequestParams params = new RequestParams(  );
        params.put( "op", "getjnarticlesinfourl" );
        params.put( "idarticle", String.valueOf( mArticleId ) );
        client.post( this,getString( R.string.jngroup_request_url ),params, new JsonHttpResponseHandler(  ){
            ProgressBar progressBar = (ProgressBar)findViewById( R.id.articleProgressBar );
            @Override
            public void onStart() {
                progressBar.setInterpolator( new LinearInterpolator(  ) );

            }

            @Override
            public void onSuccess( JSONObject response ) {
                progressBar.setVisibility( View.GONE );
                String vidUrl = null;
                try {
                    vidUrl = response.getJSONArray( "JNGroup" ).getJSONObject( 0 )
                            .getJSONArray( "articleurl" ).getJSONObject( 0 ).getString( "articleurl" );
                } catch( JSONException e ) {
                    e.printStackTrace();
                }

                MediaController control = new MediaController( ArticleViewActivity.this );
                VideoView video = (VideoView)findViewById( R.id.articleVideoView );
                control.setEnabled( true );
                control.setAnchorView( video );
                video.setVisibility( View.VISIBLE );
                video.requestFocus();
                video.setMediaController( control );
                video.setVideoURI( Uri.parse( vidUrl ) );
                video.start();
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
            mDrawerContainer.getChildAt( 3 ).setVisibility( View.GONE );
            mDrawerContainer.setLayoutParams( params );
            ((Button)v).setText( "Open menu" );
        }else{
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,0, weight );
            isMenuOpen = !isMenuOpen;
            mDrawerContainer.getChildAt( 1 ).setVisibility( View.VISIBLE );
            mDrawerContainer.getChildAt( 2 ).setVisibility( View.VISIBLE );
            mDrawerContainer.getChildAt( 3 ).setVisibility( View.VISIBLE );
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

        mCommentDialog = new Dialog( this );
        mCommentDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
       int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT)
            mCommentDialog.setContentView( commentView,
                    new ViewGroup.LayoutParams( 650, ViewGroup.LayoutParams.MATCH_PARENT ) );
        else
            mCommentDialog.setContentView( commentView,
                    new ViewGroup.LayoutParams( 750, ViewGroup.LayoutParams.MATCH_PARENT ) );

        final EditText commentBox = ((EditText)mCommentDialog.findViewById( R.id.commentEditText ));
       mCommentDialog.findViewById(R.id.commentButton).setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View view ) {
                String comment = commentBox.getText().toString();
                if( comment.length() < 3 )
                    commentBox.setError( Html.fromHtml( "<font color='red'>Text was too short</font>" ) );
                else {
                    Log.e( getClass().getName(), comment );
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put( "op", "postjnarticlecomment" );
                    params.put( "iduser", String.valueOf( 1 ) );
                    params.put( "idarticle", String.valueOf( mArticleId ) );
                    params.put( "body", comment );
                    client.post( getString( R.string.jngroup_request_url ), params,
                            new JsonHttpResponseHandler() {
                                ProgressDialog progressDialog = new ProgressDialog( ArticleViewActivity.this );
                                @Override
                                public void onStart() {
                                    progressDialog.setIndeterminate( true );
                                    progressDialog.setMessage( "Sending message..." );
                                    progressDialog.show();
                                }

                                @Override
                                public void onSuccess( JSONObject response ) {
                                    commentBox.setText( "" );
                                    fetchComments();
                                    if(mCommentDialog.isShowing())
                                        mCommentDialog.dismiss();
                                    progressDialog.dismiss();
                                    Log.e( getClass().getName(), "Sent comment " + response.toString() );
                                }
                            }
                    );
                }
            }
        } );
        commentBox.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                commentBox.setError( null );
            }
        } );
        mCommentDialog.show();
    }

    /**
     * LIKE AN ARTICLE
     */
    public void updateArticleLikes(View v){
        Log.e(getClass().getName(), String.valueOf( mArticleId ));
        AsyncHttpClient client = new AsyncHttpClient(  );
        RequestParams params = new RequestParams(  );
        params.put( "op", "jnarticlelike" );
        params.put( "iduser", String.valueOf( 1 ) );
        params.put( "idarticle", String.valueOf( mArticleId ) );
        client.post( getString( R.string.jngroup_request_url ), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess( JSONObject response ) {
              fetchArticleLikes();
            }
        } );
    }
    private void fetchArticleLikes(){
        AsyncHttpClient client = new AsyncHttpClient(  );
        RequestParams params = new RequestParams(  );
        params.put( "op", "getjncountarticleslike" );
        params.put( "idarticle", String.valueOf( mArticleId ) );
        client.post( getString( R.string.jngroup_request_url ), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess( JSONObject response ) {

                try {
                    mArticleLikes = response.getJSONArray( "JNGroup" ).getJSONObject( 0 )
                            .getJSONArray( "Countlikes" ).getJSONObject( 0 ).getInt( "likecount" );
                } catch( JSONException e ) {
                    e.printStackTrace();
                }
                if(null != mArticleLikesTextView )
                    mArticleLikesTextView.setText( (mArticleLikes >= 2)?
                            mArticleLikes + " people like this article" :
                            (mArticleLikes == 1)? mArticleLikes + " person like this article" :
                                    mArticleLikes + " persons like this article");

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
                if(null != mAdapter)
                    mCommentsList.clear();
                JSONArray jsonArray;
                try {
                     jsonArray = response.getJSONArray( "JNGroup" ).getJSONObject( 0 )
                                .getJSONArray( "ArticleComment" );

                    for(int i=0;i<jsonArray.length();i++) {
                        CommentObject object = new CommentObject();
                       JSONObject json = jsonArray.getJSONObject( i );
                        object.setCommentBody( json.getString( "content" ) );
                        object.setCommenterName( json.getString( "publisherfirstname" ) + " " +
                                json.getString( "publisherlastname" ) );
                        object.setCommentTime( json.getString( "publishdate" ) );
                        mCommentsList.add( object );
                    }
                } catch( JSONException e ) {
                    e.printStackTrace();
                }

                if(null == mAdapter) {
                    mAdapter = new ArticlesViewAdapter( mCommentsList );
                    if(mCommentsList.isEmpty()) {
                        TextView tv = new TextView( ArticleViewActivity.this );
                        tv.setLayoutParams( new AbsListView.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        tv.setText( "No comments yet, be the first" );
                        tv.setPadding( 10,10,10,10 );
                        mArticleCommentsListView.addHeaderView( tv );
                    }
                    mArticleCommentsListView.setAdapter( mAdapter );

                }
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * SET ARTICLE ADAPTER
     */
    public class ArticlesViewAdapter extends BaseAdapter {
        List<CommentObject> items;

        public ArticlesViewAdapter(List<CommentObject> objects){

            this.items = objects;

        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CommentObject getItem( int position ) {
            return items.get( position );
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
                holder.commenteTime = (TextView)convertView.findViewById( R.id.articleTimeTextView );
                convertView.setTag( holder );
            }else{
                holder = (Holder)convertView.getTag();
            }
            CommentObject object = getItem( position );

            holder.comment.setText( object.getCommentBody() );
            holder.commenterName.setText( object.getCommenterName() );
            holder.commenteTime.setText( object.getCommentTime() );
            return convertView;
        }


        class Holder{
            TextView comment;
            TextView commenterName;
            TextView commenteTime;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(null != client)
            client.cancelRequests( this, true );
        if(null != mCommentDialog )
           mCommentDialog.dismiss();
    }
}
