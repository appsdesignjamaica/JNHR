package com.main.jngroup.jngroup;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.main.jngroup.R;
import com.main.jngroup.jnhelper.TextHelper;

public class DepartmentsActivity extends Activity {
    private ListView mDepartmentsListView;
    private DepartmentsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        mDepartmentsListView = (ListView)findViewById( R.id.departmentsListView );
        mAdapter = new DepartmentsAdapter();

        TextHelper.setTextTypeface( this, (TextView)findViewById( R.id.textView ) );
        mDepartmentsListView.setAdapter( mAdapter );

    }


    public class DepartmentsAdapter extends BaseAdapter{
        private String[] depts = new String[50];
        public DepartmentsAdapter(){
            for(int i=0;i<50;i++)
                depts[i] = "Department #"+i;
        }

        @Override
        public int getCount() {
            return depts.length;
        }

        @Override
        public String getItem( int position ) {
            return depts[position];
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
                holder.departmentName = (TextView)convertView.findViewById( android.R.id.text1 );
                convertView.setTag( holder );
            }else{
                holder = (Holder)convertView.getTag();
            }

            holder.departmentName.setText( getItem( position ) );

            return convertView;
        }


        class Holder{
            TextView departmentName;
        }
    }
}
