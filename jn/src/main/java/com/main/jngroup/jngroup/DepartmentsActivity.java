package com.main.jngroup.jngroup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    private Dialog mDepartmentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        mDepartmentsListView = (ListView)findViewById( R.id.departmentsListView );
        mListOfDepartments = new ArrayList<DepartmentObject>(  );
        TextHelper.setTextTypeface( this, (TextView)findViewById( R.id.textView ) );

        if(savedInstanceState != null){
            repopulateListView( savedInstanceState );
        }else {
            initializeNewListview();
        }

        mDepartmentsListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int position, long id ) {
                int depCode = Integer.parseInt( view.getTag(R.integer.tag_two).toString());
                Log.e( getClass().getName(), depCode+"" );
                fetchDepartmentDetails( depCode );


            }
        } );
    }

    private void fetchDepartmentDetails( final int deptCode){
        final String[] data = new String[ 2 ];
        final ProgressDialog loadingDialog = ProgressDialog.show( this,
                "Please wait ...", "Loading request ...", true );
        loadingDialog.setCancelable( true );


        final Thread fetchDeptData = new Thread( new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>( 2 );
                params.add( new BasicNameValuePair( "op", "jngetdepcompctry" ) );
                params.add( new BasicNameValuePair( "iddep", String.valueOf( deptCode ) ) );
                String json = JNUtils.getJsonFromUrl( getString( R.string.jngroup_request_url ),AppConstant.GET_REQUEST, params );
                try {
                    JSONObject jsonObject = new JSONObject( json );
                    JSONArray jArray =  jsonObject.getJSONArray( "JNGroup" ).getJSONObject( 0 )
                            .getJSONArray( "relationdep" );
                    JSONObject object = jArray.getJSONObject( 0 );

                    data[0] =  object.getString( "company" );
                    data[1] =  object.getString( "country");

                } catch( Exception e ) {
                    e.printStackTrace();
                }
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {

                        loadingDialog.dismiss();
                        populateDialog( data[ 0 ], data[ 1 ] );
                    }
                } );
            }
        } );
        fetchDeptData.start();
    }

    /**
     * NEW INSTANCE OF LISTVIEW
     */
    private void initializeNewListview() {
        new FetchDepartments().execute();
    }

    /**
     * RESET VALUES OF LISTVIEW
     * @param savedInstanceState array with data
     */
    private void repopulateListView( Bundle savedInstanceState ) {
        findViewById( R.id.departmentProgressBar ).setVisibility( View.GONE );
        mDepartmentsListView.setVisibility( View.VISIBLE );

        String[] depts = savedInstanceState.getStringArray( "departments" );
        int[] ids = savedInstanceState.getIntArray( "ids" );
        for(int i =0; i<depts.length;i++){
            DepartmentObject departmentObject = new DepartmentObject();
            departmentObject.setDeptId( ids[i] );
            departmentObject.setDeptName( depts[i] );
            mListOfDepartments.add( departmentObject );
        }
        mAdapter = new DepartmentsAdapter(mListOfDepartments);
        mDepartmentsListView.setAdapter( mAdapter );
    }

    /**
     * GENERATES POPUP DIALOG
     * @param region name of country
     */
    private void populateDialog(String company, String region){
        View view = getLayoutInflater().inflate( R.layout.dialog_department,null, false );

        mDepartmentDialog = new Dialog( this );
        mDepartmentDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        mDepartmentDialog.setContentView( view );
        mDepartmentDialog.setCancelable( true );
        ((TextView) mDepartmentDialog.findViewById( R.id.departmentDetailsCompanyTextView )).setText( company );
        ((TextView) mDepartmentDialog.findViewById( R.id.departmentDetailsCountryTextView )).setText( region );
        mDepartmentDialog.findViewById( R.id.departmentDetailsCloseButton )
                .setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick( View view ) {
                        mDepartmentDialog.dismiss();
                    }
                } );
        //set layout parameters
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom( mDepartmentDialog.getWindow().getAttributes() );
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        mDepartmentDialog.getWindow().setAttributes( lp );
        mDepartmentDialog.show();
    }

    /**
     * TASk TO FETCH DEPARTMENT LISTING
     */
    private class FetchDepartments extends AsyncTask<String,Integer, String>{
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar)findViewById( R.id.departmentProgressBar );
            progressBar.setIndeterminate( true );
        }

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

            progressBar.setVisibility( View.GONE );
            mDepartmentsListView.setVisibility( View.VISIBLE );
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
                convertView.setTag( R.integer.tag_one,holder );
            }else{
                holder = (Holder)convertView.getTag(R.integer.tag_one);
            }

            holder.departmentName.setText( getItem( position ).getDeptName() );
            convertView.setTag( R.integer.tag_two, getItem( position ).getDeptId() );
            return convertView;
        }


        class Holder{
            TextView departmentName;
        }
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState( outState );

        int[] ids = new int[mListOfDepartments.size()] ;
        String[] values = new String[mListOfDepartments.size()] ;
        for(int i=0;i<mListOfDepartments.size();i++) {
            ids[i] = mListOfDepartments.get(i).getDeptId();
            values[i] =  mListOfDepartments.get( i ).getDeptName();
        }
        outState.putIntArray( "ids", ids );
        outState.putStringArray( "departments", values );
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mDepartmentDialog != null)
            if( mDepartmentDialog.isShowing())
                mDepartmentDialog.dismiss();
    }
}
