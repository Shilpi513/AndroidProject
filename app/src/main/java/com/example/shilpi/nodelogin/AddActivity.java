package com.example.shilpi.nodelogin;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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


public class AddActivity extends AppCompatActivity {
    public static String ip = MainActivity.ip;
    EditText Name,Author,Buy_here,Review;
    private static final String TAG = "msg";
    String n,a,p,s;
    private TextView mResult;
    SharedPreferences pref;
    String token,grav,oldpasstxt,newpasstxt;
    Button chgpass,chgpassfr,cancel,logout;
    Dialog dlg;
    EditText oldpass,newpass;
    List<NameValuePair> params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        chgpass = (Button)findViewById(R.id.chgbtn);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("token", "");
                edit.commit();
                Intent loginactivity = new Intent(AddActivity.this,LoginActivity.class);

                startActivity(loginactivity);
                finish();
            }
        });
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        grav = pref.getString("grav", "");



        chgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg = new Dialog(AddActivity.this);
                dlg.setContentView(R.layout.chgpassword_frag);
                dlg.setTitle("Change Password");
                chgpassfr = (Button)dlg.findViewById(R.id.chgbtn);

                chgpassfr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oldpass = (EditText)dlg.findViewById(R.id.oldpass);
                        newpass = (EditText)dlg.findViewById(R.id.newpass);
                        oldpasstxt = oldpass.getText().toString();
                        newpasstxt = newpass.getText().toString();
                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("oldpass", oldpasstxt));
                        params.add(new BasicNameValuePair("newpass", newpasstxt));
                        params.add(new BasicNameValuePair("id", token));
                        ServerRequest sr = new ServerRequest();
                        //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
                        JSONObject json = sr.getJSON("http://192.168.1.6:1000/api/chgpass",params);
                        if(json != null){
                            try{
                                String jsonstr = json.getString("response");
                                if(json.getBoolean("res")){

                                    dlg.dismiss();
                                    Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_SHORT).show();

                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                cancel = (Button)dlg.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlg.dismiss();
                    }
                });
                dlg.show();
            }
        });



    }


    public void submitData(View view)
    { Log.i(TAG,"inside submitdata button");
        mResult = (TextView) findViewById(R.id.result);
        Name = (EditText) findViewById(R.id.editText1);
        Author = (EditText) findViewById(R.id.editText2);
        Review = (EditText) findViewById(R.id.editText3);
        Buy_here= (EditText) findViewById(R.id.editText4);

        n = Name.getText().toString().trim();
        a = Author.getText().toString().trim();
        p = Review.getText().toString().trim();
        s = Buy_here.getText().toString().trim();


        Log.i(TAG,"b4 calling posttask");
        //make POST request
       new PostDataTask().execute("http://"+ip+":1001/api/status");
        Log.i(TAG,"after calling posttask");

    }

    class PostDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            Log.i(TAG,"inside preexec");
            super.onPreExecute();

            progressDialog = new ProgressDialog(AddActivity.this);
            progressDialog.setMessage("Inserting data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG,"inside doInBackground");
            try {
                return postData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG,"inside nPostExecute");
            super.onPostExecute(result);

            mResult.setText(result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String postData(String urlPath) throws IOException, JSONException {
            Log.i(TAG,"inside postData");
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {
                //Create data to send to server
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("Name", n);
                dataToSend.put("Author", a);
                dataToSend.put("Review", p);
                dataToSend.put("Buy_here", s);

                //Initialize and config request, then connect to server.
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);  //enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }

            return result.toString();
        }
    }

}







