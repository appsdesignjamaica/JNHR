package com.main.jngroup.jngroup;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.main.jngroup.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SearchActivity extends Activity {
    private ListView mSearchListView;
    private EditText mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchEditText = (EditText)findViewById( R.id.searchEditText );
        mSearchListView = (ListView)findViewById( R.id.searchListView );

        List<String> strings = new ArrayList<String>( 100 );
        for(int i = 0; i<100; i++){
            CharSequence[] cs = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"};
            StringBuilder word = new StringBuilder(  );
            for(int j = 0; j<5;j++) {
                int rando = new Random().nextInt( cs.length - 1 );
                word.append( cs[rando]);
            }
            strings.add( "Sample " + word.toString() + " " + i );
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1 , strings );
        mSearchListView.setAdapter( adapter );

        mSearchEditText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence charSequence, int i, int i2, int i3 ) {

            }

            @Override
            public void onTextChanged( CharSequence charSequence, int i, int i2, int i3 ) {

            }

            @Override
            public void afterTextChanged( Editable editable ) {
                adapter.getFilter().filter( editable.toString() );
            }
        } );
    }

    //Search for key words from list
    public void setSearchFilter(View v){
        mSearchListView.setFilterText( mSearchEditText.getText().toString() );
        ((ArrayAdapter) mSearchListView.getAdapter()).getFilter().filter( mSearchEditText.getText().toString() );
    }

}
