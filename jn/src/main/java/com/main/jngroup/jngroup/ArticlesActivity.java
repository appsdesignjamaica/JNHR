package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.main.jngroup.R;
import com.main.jngroup.jnhelper.TextHelper;
import com.main.jngroup.objects.ArticleObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends Activity {
    private ListView mArticlesListView;
    private ArticlesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        mArticlesListView = (ListView)findViewById( R.id.articlesListView );

        TextHelper.setTextTypeface( this, (TextView) findViewById( R.id.textView ) );
        int type = getIntent().getIntExtra( "type", 2 );
        loadArticlesList(type);

        mArticlesListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int position, long id ) {
                ArticleObject object = (ArticleObject)view.getTag(R.integer.tag_two);
                Intent pdfIntent = new Intent( ArticlesActivity.this, ArticleViewActivity.class );
                pdfIntent.putExtra( "type", object.getArticleType() );
                pdfIntent.putExtra( "id", object.getArticleId() );
                pdfIntent.putExtra( "date", object.getArticleDate() );
                startActivity( pdfIntent );
            }
        } );
    }

    /**
     * LOAD ARTICLES BASED ON TYPE SELECTED
     * @param artType type of article to fetch
     */
    private void loadArticlesList( final int artType){
        AsyncHttpClient client = new AsyncHttpClient(  );
        RequestParams params = new RequestParams(  );
        params.put( "op","getjnarticlesbytype" );
        params.put( "idtype",String.valueOf( artType ) );
        client.post(this,getString( R.string.jngroup_request_url ),params,new JsonHttpResponseHandler(  ){
           ProgressDialog progressDialog = new ProgressDialog( ArticlesActivity.this );
            @Override
            public void onStart() {
               progressDialog.setMessage( "Fetching articles, just a sec" );
                progressDialog.setIndeterminate( true );
                progressDialog.show();
            }

            @Override
            public void onSuccess( JSONObject response ) {
                super.onSuccess( response );

                JSONArray jArray;
                List<ArticleObject> articleList = null;
                try {
                    jArray = response.getJSONArray( "JNGroup" ).getJSONObject( 0 )
                            .getJSONArray( "Articlebytype" );
                    if(jArray.length() > 0) {
                        articleList = new ArrayList<ArticleObject>( jArray.length() );
                        if( jArray.length() > 0 ) {
                            for( int i = 0; i < jArray.length(); i++ ) {
                                ArticleObject article = new ArticleObject();
                                JSONObject object = jArray.getJSONObject( i );
                                article.setArticleId( object.getInt( "articleid" ) );
                                article.setArticleName( object.getString( "articlename" ) );
                                article.setArticleDate( object.getString( "pubdate" ) );
                                article.setPosterFirstName( object.getString( "publisherfirstname" ) );
                                article.setPosterLastName( object.getString( "publisherlastname" ) );
                                articleList.add( article );
                            }
                        }
                    }
                } catch( JSONException e ) {
                    e.printStackTrace();
                }

                mAdapter = new ArticlesAdapter( articleList, artType );
                mArticlesListView.setAdapter( mAdapter );
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure( Throwable e, JSONObject errorResponse ) {
                super.onFailure( e, errorResponse );
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    public class ArticlesAdapter extends BaseAdapter {
        private List<ArticleObject> articleList;
        private int articleType;
        public ArticlesAdapter(List<ArticleObject> items, int artType){
           articleList = items;
            articleType = artType;
        }

        @Override
        public int getCount() {
            if(null != articleList)
                return articleList.size();
            else
                return 0;
        }

        @Override
        public ArticleObject getItem( int position ) {
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
                holder.articlePoster = (TextView)convertView.findViewById( R.id.articlePosterTextView );
                convertView.setTag( R.integer.tag_one, holder );
            }else{
                holder = (Holder)convertView.getTag(R.integer.tag_one);
            }

            ArticleObject object = getItem( position );
            object.setArticleType( articleType );

            if(articleType == 2)
                holder.articleName.setText( object.getArticleName()
                        .substring( object.getArticleName().indexOf( "_" )+1 ) );
            else
                holder.articleName.setText( object.getArticleName() );
            holder.articlePoster.setText( "Posted by: " + object.getPosterName() +
                    " On: " + object.getArticleDate().substring( 0, 10 ) );

            convertView.setTag( R.integer.tag_two, object );
            return convertView;
        }


        class Holder{
            TextView articleName;
            TextView articlePoster;
        }
    }

}
