package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.main.jngroup.R;
import com.main.jngroup.jnhelper.AppConstant;
import com.main.jngroup.jnhelper.JNUtils;
import com.main.jngroup.jnhelper.TextHelper;
import com.main.jngroup.objects.DepartmentObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DepartmentsActivity extends Activity {
    private ListView mDepartmentsListView;
    private DepartmentsAdapter mAdapter;
    private List<DepartmentObject> mListOfDepartments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        mDepartmentsListView = (ListView)findViewById( R.id.departmentsListView );
        mListOfDepartments = new ArrayList<DepartmentObject>(  );
        TextHelper.setTextTypeface( this, (TextView)findViewById( R.id.textView ) );


        new FetchDepartments().execute(  );
        mDepartmentsListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int position, long id ) {
                String name = ((TextView)view.findViewById( R.id.departmentTextView )).getText().toString();
                populateDialog(name);
            }
        } );
    }


    private void populateDialog(String name){
        View view = getLayoutInflater().inflate( R.layout.dialog_department,null, false );
        final Dialog d = new Dialog( this );
        d.requestWindowFeature( Window.FEATURE_NO_TITLE );
        d.setContentView( view, new ViewGroup.LayoutParams( 450, 350 ) );
        d.setCancelable( true );
        ((TextView)d.findViewById( R.id.departmentDetailsTextView )).setText( name );
        d.show();
    }


    private class FetchDepartments extends AsyncTask<String,Integer, String>{

        @Override
        protected String doInBackground( String... strings ) {
            String url = getString( R.string.jngroup_request_url );
            List<NameValuePair> params = new ArrayList<NameValuePair>( 1 );
            params.add( new BasicNameValuePair("op","jngetdepartments") );
            return JNUtils.getJsonFromUrl( url, AppConstant.GET_REQUEST, params );
        }

        @Override
        protected void onPostExecute( String response ) {

            parseDepartmentList( response );
            mAdapter = new DepartmentsAdapter(mListOfDepartments);
            mDepartmentsListView.setAdapter( mAdapter );
        }

        // Parses the json response to feed the adapter with the departments
        public void parseDepartmentList(String json){

            try {
                JSONObject jsonObject = new JSONObject( json );
                JSONArray jArray =  jsonObject.getJSONArray( "JNGroup" ).getJSONObject( 0 )
                        .getJSONArray( "departments" );

                for(int i =0; i<jArray.length();i++){
                    DepartmentObject departmentObject = new DepartmentObject();
                    JSONObject object = jArray.getJSONObject( i );
                    departmentObject.setDeptId( object.getInt( "id" ) );
                    departmentObject.setDeptName( object.getString( "dname" ) );

                    mListOfDepartments.add( departmentObject );
                }
            } catch( JSONException e ) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Adapter for departments listview
     */
    public class DepartmentsAdapter extends BaseAdapter{
        private List<DepartmentObject> itemList;

        public DepartmentsAdapter(List<DepartmentObject> items){
            this.itemList = items;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public DepartmentObject getItem( int position ) {
            return itemList.get( position );
        }

        @Override
        public long getItemId( int position ) {
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup viewGroup ) {
          Holder holder;

            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.row_departments , viewGroup, false );
                holder = new Holder();
                holder.departmentName = (TextView)convertView.findViewById( R.id.departmentTextView );
                convertView.setTag( holder );
            }else{
                holder = (Holder)convertView.getTag();
            }

            holder.departmentName.setText( getItem( position ).getDeptName() );

            return convertView;
        }


        class Holder{
            TextView departmentName;
            TextView departmentLocation;
        }
    }
}
