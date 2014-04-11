package com.main.jngroup.jngroup;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.main.jngroup.R;
import com.main.jngroup.jnhelper.AppConstant;
import com.main.jngroup.jnhelper.JNUtils;
import com.main.jngroup.jnhelper.TextHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends Activity {
    private ListView mArticles;
    private ArticlesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        mArticles = (ListView)findViewById( R.id.articlesListView );

        TextHelper.setTextTypeface( this, (TextView) findViewById( R.id.textView ) );

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground( Void... voids ) {
                List<NameValuePair> params = new ArrayList<NameValuePair>(  );
               // params.add( new BasicNameValuePair(  ) );
               String json = JNUtils.getJsonFromUrl( getString( R.string.jngroup_request_url ),
                        AppConstant.POST_REQUEST, params );
                return null;
            }

            @Override
            protected void onPostExecute( Void aVoid ) {
                super.onPostExecute( aVoid );

                mAdapter = new ArticlesAdapter(null);
            }
        }.execute(  );

        mArticles.setAdapter( mAdapter );
        mArticles.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int position, long id ) {
                Intent pdfIntent = new Intent( ArticlesActivity.this, ArticleViewActivity.class );
                pdfIntent.putExtra( "pdf_url", "http://www.energy.umich.edu/sites/default/files/pdf-sample.pdf" );
                startActivity( pdfIntent );
            }
        } );
    }


    public class ArticlesAdapter extends BaseAdapter {
        private List<String> articleList;

        public ArticlesAdapter(List<String> items){
           articleList = items;
        }

        @Override
        public int getCount() {
            return articleList.size();
        }

        @Override
        public String getItem( int position ) {
            return articleList.get( position );
        }

        @Override
        public long getItemId( int position ) {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup viewGroup ) {
            Holder holder;

            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.row_article , viewGroup, false );
                holder = new Holder();
                holder.articleName = (TextView)convertView.findViewById( R.id.articleTextView );
                convertView.setTag( holder );
            }else{
                holder = (Holder)convertView.getTag();
            }

            holder.articleName.setText( getItem( position ) );

            return convertView;
        }


        class Holder{
            TextView articleName;
        }
    }

}
