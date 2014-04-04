package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.jngroup.R;

public class ArticleViewActivity extends Activity {
    private LinearLayout mDrawerContainer;
    private boolean isMenuOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        mDrawerContainer = (LinearLayout)findViewById( R.id.menuContainer );

        if(getIntent().getExtras() != null) {
            ListView mArticleViewListView = (ListView) findViewById( R.id.articleViewListView );
            ArticlesViewAdapter adapter = new ArticlesViewAdapter();
            WebView articleView = (WebView)findViewById( R.id.webView );

            articleView.getSettings().setJavaScriptEnabled( true );
            articleView.setWebViewClient( new WebViewClient() );
            String pdfURL = getIntent().getExtras().getString( "pdf_url" );
            articleView.loadUrl( "http://docs.google.com/gview?embedded=true&url=" + pdfURL );

            mArticleViewListView.setAdapter( adapter );
        }else{
            Toast.makeText(this, "Failed to load the appropriate file", Toast.LENGTH_LONG).show();
            finish();
        }
    }

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


    public void makeComment(View v){
        View commentView = getLayoutInflater().inflate( R.layout.activity_comment, null, false );
        AlertDialog.Builder alert = new AlertDialog.Builder( this )
                .setView( commentView );
        AlertDialog d = alert.create();
        d.findViewById( R.id.commentButton ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
//Push comment to server
            }
        } );
        d.show();
    }

    /**
     * Set article adapter
     */
    public class ArticlesViewAdapter extends BaseAdapter {
        private String[] comments = new String[50];

        public ArticlesViewAdapter(){


            for(int i=0;i<50;i++)
                comments[i] = "Comment #"+i;
        }

        @Override
        public int getCount() {
            return comments.length;
        }

        @Override
        public String getItem( int position ) {
            return comments[position];
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

            holder.comment.setText( getString( R.string.sample_text ) );
            holder.commenterName.setText( (position % 2 == 0)? "Mr. Guy" : "Mr. Bedward" );

            return convertView;
        }


        class Holder{
            TextView comment;
            TextView commenterName;
        }
    }

}
