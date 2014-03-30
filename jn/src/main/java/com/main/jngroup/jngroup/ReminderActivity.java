package com.main.jngroup.jngroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.main.jngroup.R;

public class ReminderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
    }

    public class RemindersAdapter extends BaseAdapter {
        private String[] remind = new String[50];

        public RemindersAdapter(){
            for(int i=0;i<50;i++)
                remind[i] = "Article #"+i;
        }

        @Override
        public int getCount() {
            return remind.length;
        }

        @Override
        public String getItem( int position ) {
            return remind[position];
        }

        @Override
        public long getItemId( int position ) {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup viewGroup ) {
            Holder holder;

            if(convertView == null){
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1 , viewGroup, false );
                holder = new Holder();
                holder.articleName = (TextView)convertView.findViewById( android.R.id.text1 );
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
