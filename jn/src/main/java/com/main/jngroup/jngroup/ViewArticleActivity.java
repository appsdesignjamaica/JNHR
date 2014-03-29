package com.main.jngroup.jngroup;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.jngroup.R;

public class ViewArticleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);



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

            holder.comment.setText( getItem( position ) );
            holder.commenterName.setText( (position % 2 == 0)? "Mr. Guy" : "Mr. Bedward" );

            return convertView;
        }


        class Holder{
            TextView comment;
            TextView commenterName;
        }
    }

}
