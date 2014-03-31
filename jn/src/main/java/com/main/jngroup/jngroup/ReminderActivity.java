package com.main.jngroup.jngroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.main.jngroup.R;

import java.util.List;

public class ReminderActivity extends Activity {
    private ListView mReminderListView;
    private RemindersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        mReminderListView = ( ListView)findViewById( R.id.reminderListView );
        mAdapter = new RemindersAdapter();
        mReminderListView.setAdapter( mAdapter );
    }

    public class RemindersAdapter extends BaseAdapter {
        private String[] remind = new String[50];
        private String[] reminder = new String[50];

        public RemindersAdapter(){
            for(int i=0;i<50;i++)
                remind[i] = "Big Day #"+i;
            for(int i=0;i<50;i++)
                reminder[i] = "Meeting on Monday @ 9:10";
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
                convertView = getLayoutInflater().inflate(R.layout.row_reminder , viewGroup, false );
                holder = new Holder();
                holder.reminderName = (TextView)convertView.findViewById( R.id.reminderTextView );
                holder.reminderDetails = (TextView)convertView.findViewById( R.id.reminderDetailsTextView );
                convertView.setTag( holder );
            }else{
                holder = (Holder)convertView.getTag();
            }

            holder.reminderName.setText(remind[position] );
            holder.reminderDetails.setText( reminder[position] );
            return convertView;
        }

        class Holder{
            TextView reminderName;
            TextView reminderDetails;
        }
    }
}
