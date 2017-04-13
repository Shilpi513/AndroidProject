package com.example.shilpi.nodelogin;

/**
 * Created by Administrator on 11-04-2017.
 */
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DeleteActivity extends AppCompatActivity {

    TextView mResult;
    EditText delid;
    String did;
    SharedPreferences pref;
    String token,grav,oldpasstxt,newpasstxt;
    Button chgpass,chgpassfr,cancel,logout;
    Dialog dlg;
    EditText oldpass,newpass;
    List<NameValuePair> params;
    public static String ip=MainActivity.ip;
    // String ex = "http://192.168.1.15:1000/api/status/"+del;
    //ex.concate(del);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        mResult = (TextView) findViewById(R.id.textView);
        delid = (EditText) findViewById(R.id.delid);
        did = delid.getText().toString();
        chgpass = (Button)findViewById(R.id.chgbtn);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("token", "");
                edit.commit();
                Intent loginactivity = new Intent(DeleteActivity.this,LoginActivity.class);

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
                dlg = new Dialog(DeleteActivity.this);
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

    public void delete(View v)
    {
        String ex = "http://"+ip+":1001/api/status/"+did;
        //make DELETE request
        new DeleteDataTask().execute(ex);

    }

    class DeleteDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DeleteActivity.this);
            progressDialog.setMessage("Deleting data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return deleteData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mResult.setText(result);

            if(progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String deleteData(String urlPath) throws IOException {

            String result =  null;

            //Initialize and config request, then connect to server.
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();

            //Check update successful or not
            if (urlConnection.getResponseCode() == 204) {
                result = "Delete successfully !";
            } else {
                result = "Delete failed !";
            }
            return result;
        }
    }


}