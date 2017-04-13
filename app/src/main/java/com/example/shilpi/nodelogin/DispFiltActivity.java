package com.example.shilpi.nodelogin;

/**
 * Created by Administrator on 11-04-2017.
 */

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.support.v7.widget.RecyclerView;

public class DispFiltActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] cN;
    ArrayAdapter<String> adapter;
    public RecyclerView mRVFishPrice;
    public BooksAdapter mAdapter;
    TextView n,a,p,s;
    private TextView mResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        // new GetDataTask().execute("http://192.168.2.4:1001/api/status");
        //INITALIZE ADAPTER
/*
        adapter = new ArrayAdapter<String>(DisplayActivity.this,R.layout.activity_display,cN);
        setListAdapter(adapter);
        ListView list = getListView();
        list.setOnItemClickListener(DisplayActivity.this);*/



    }
    public void displayData(View v) throws JSONException {
        n = (TextView) findViewById(R.id.test);
       /* a = (TextView) findViewById(R.id.author);
        p = (TextView) findViewById(R.id.price);
        s = (TextView) findViewById(R.id.stock);
        mResult = (TextView) findViewById(R.id.result);
*/
        //make GET request
        new GetDataTask().execute("http://192.168.1.6:1001/api/status");
        // Spinner s = (Spinner) findViewById(R.id.books);
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cN);
        s.setAdapter(adapter);*/
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String itemval = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(DispFiltActivity.this,itemval,Toast.LENGTH_SHORT).show();
    }
/*
AsyncTask's generic types
The three types used by an asynchronous task are the following:
Params, the type of the parameters sent to the task upon execution.
Progress, the type of the progress units published during the background computation.
Result, the type of the result of the background computation.
Not all types are always used by an asynchronous task. To mark a type as unused, simply use the type Void:
*/

    class GetDataTask extends AsyncTask<String, Void, String>  {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = new ProgressDialog(DispFiltActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return getData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            List<Books> data=new ArrayList<>();
            //   Spinner s = (Spinner) findViewById(R.id.books);
            //set data response to textView
//            mResult.setText(result);
            //n.setText("onPostExe");
            //JSONObject obj = null;
            JSONArray obj ;
            try {
                obj = new JSONArray(result);
                //obj = new JSONObject(result);
                cN = new String[obj.length()];

                Books[] bookData = new Books[obj.length()];
                for (int i = 0; i < obj.length(); i++) {
                    JSONObject jsonobj = obj.getJSONObject(i);
                    // Books bookData = new Books();
                    String N1;
                    bookData[i] = new Books();
                    bookData[i].id = jsonobj.getString("_id");
                    bookData[i].Name = jsonobj.getString("Name");
                    bookData[i].Author = jsonobj.getString("Author");
                    bookData[i].Review = jsonobj.getString("Review");
                    bookData[i].Buy_here = jsonobj.getString("Buy_here");
                    data.add(bookData[i]);
                    if (i == obj.length() - 1) {
                        n.setText("total row = " + obj.length());
                    }
                }
                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new BooksAdapter(DispFiltActivity.this, data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(DispFiltActivity.this));
                //  mAdapter.notifyDataSetChanged();
             /*   ArrayAdapter<String> adapter = new ArrayAdapter<String>(DisplayActivity.this,
                        android.R.layout.linearLayout5, cN);
                s.setAdapter(adapter);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //cancel progress dialog
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String getData(String urlPath) throws IOException {
            //  n.setText("GET Data");
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader =null;

            try {
                //Initialize and config request, then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.connect();

                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    result.append(line).append("\n");
                }
            } /*catch (JSONException e) {
                e.printStackTrace();
            } */finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
            return result.toString();
        }

    }

}