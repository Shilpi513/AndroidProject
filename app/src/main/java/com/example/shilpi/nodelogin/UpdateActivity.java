package com.example.shilpi.nodelogin;

/**
 * Created by Administrator on 11-04-2017.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    EditText Name,Author,Review,Buy_here,uid;
    private static final String TAG = "msg";
    String n,a,p,s,u;
    public static String ip=MainActivity.ip;
    SharedPreferences pref;
    String token,grav,oldpasstxt,newpasstxt;
    Button chgpass,chgpassfr,cancel,logout;
    Dialog dlg;
    EditText oldpass,newpass;
    List<NameValuePair> params;
    private TextView mResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        chgpass = (Button)findViewById(R.id.chgbtn);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("token", "");
                edit.commit();
                Intent loginactivity = new Intent(UpdateActivity.this,LoginActivity.class);

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
                dlg = new Dialog(UpdateActivity.this);
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
        uid = (EditText) findViewById(R.id.updateid);

        u = uid.getText().toString();
        n = Name.getText().toString().trim();
        a = Author.getText().toString().trim();
        p = Review.getText().toString().trim();
        s = Buy_here.getText().toString().trim();


        Log.i(TAG,"b4 calling posttask");
        String ex = "http://+ip+:1001/api/status/"+u;
        //make PUT request
        new PutDataTask().execute(ex);

    }

    class PutDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(UpdateActivity.this);
            progressDialog.setMessage("Updating data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return putData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            } catch (JSONException ex) {
                return "Data invalid !";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mResult.setText(result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String putData(String urlPath) throws IOException, JSONException {

            BufferedWriter bufferedWriter = null;
            String result = null;

            try {


                //Create data to send to server
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("Name", n);
                dataToSend.put("Author", a);
                dataToSend.put("Review", p);
                dataToSend.put("Buy_here", s);

                //Initialize and config request, then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);  //enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //Check update successful or not
                if (urlConnection.getResponseCode() == 200) {
                    return "Update successfully !";
                } else {
                    return "Update failed !";
                }
            } finally {
                if(bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }
        }
    }

}