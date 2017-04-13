package com.example.shilpi.nodelogin;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import com.facebook.login.widget.LoginButton;



public class LoginActivity extends  AppCompatActivity {
    EditText email1,password,res_email,code,newpass;
    Button login,cont,cont_code,cancel,cancel1,register,forpass;
    String emailtxt,passwordtxt,email_res_txt,code_txt,npass_txt;
    List<NameValuePair> params;
    SharedPreferences pref;
    Dialog reset;
    ServerRequest sr;
    private com.facebook.login.widget.LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sr = new ServerRequest();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

       email1 = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.loginbtn);
        register = (Button)findViewById(R.id.register);
        forpass = (Button)findViewById(R.id.forgotpass);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regactivity);
                finish();
            }
        });

        if (fragment == null) {
            fragment = new FacebookFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                emailtxt = email1.getText().toString();
                passwordtxt = password.getText().toString();
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", emailtxt));
                params.add(new BasicNameValuePair("password", passwordtxt));
                ServerRequest sr = new ServerRequest();
                JSONObject json = sr.getJSON("http://192.168.1.6:1000/login",params);
                if(json != null){
                try{
                String jsonstr = json.getString("response");
                    if(json.getBoolean("res")){
                        String token = json.getString("token");
                        String grav = json.getString("grav");
                        SharedPreferences.Editor edit = pref.edit();
                        //Storing Data using SharedPreferences
                        edit.putString("token", token);
                        edit.putString("grav", grav);
                        edit.commit();
                        Intent profactivity = new Intent(LoginActivity.this,MainActivity.class);

                        startActivity(profactivity);
                        finish();
                    }

                        Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                }
            }
        });

        forpass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                reset = new Dialog(LoginActivity.this);
                reset.setTitle("Reset Password");
                reset.setContentView(R.layout.reset_pass_init);
                cont = (Button)reset.findViewById(R.id.resbtn);
                cancel = (Button)reset.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reset.dismiss();
                    }
                });
                res_email = (EditText)reset.findViewById(R.id.email);

                cont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        email_res_txt = res_email.getText().toString();

                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("email", email_res_txt));

                        JSONObject json = sr.getJSON("http://192.168.1.6:1000/api/resetpass", params);

                        if (json != null) {
                            try {
                                String jsonstr = json.getString("response");
                                if(json.getBoolean("res")){
                                Log.e("JSON", jsonstr);
                                Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_LONG).show();
                                reset.setContentView(R.layout.reset_pass_code);
                                cont_code = (Button)reset.findViewById(R.id.conbtn);
                                code = (EditText)reset.findViewById(R.id.code);
                                newpass = (EditText)reset.findViewById(R.id.npass);
                                cancel1 = (Button)reset.findViewById(R.id.cancel);
                                cancel1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            reset.dismiss();
                                        }
                                    });
                                cont_code.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        code_txt = code.getText().toString();
                                        npass_txt = newpass.getText().toString();
                                        Log.d("Code",code_txt);
                                        Log.d("New pass",npass_txt);
                                        params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("email", email_res_txt));
                                        params.add(new BasicNameValuePair("code", code_txt));
                                        params.add(new BasicNameValuePair("newpass", npass_txt));

                                        JSONObject json = sr.getJSON("http://192.168.1.6:1000/api/resetpass/chg", params);
                                        if (json != null) {
                                            try {

                                                String jsonstr = json.getString("response");
                                                if(json.getBoolean("res")){
                                                reset.dismiss();
                                                Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                                                }else{
                                                    Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        }
                                });
                                }else{

                                    Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });


                reset.show();
            }
        });
    }




}
