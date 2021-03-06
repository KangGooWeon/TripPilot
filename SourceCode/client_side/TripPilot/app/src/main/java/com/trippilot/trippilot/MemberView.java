package com.trippilot.trippilot;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by arden on 2018-10-22.
 */

public class MemberView extends AppCompatActivity {
    private EditText id_edt;
    private EditText password_edt;
    private EditText verify_edt;
    private Button overlap_btn;
    private CheckBox attraction_chk,cultural_facility_chk,event_chk,leports_chk,shopping_chk,cafeteria_chk;
    private Button join_btn,retire_btn;
    private TextView member_title;
    private String member_id;
    private int type;
    private boolean isChecked = false, isPwdChanged= false;
    private ApplicationInfo appInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.register_actionbar);
        actionBar.setElevation(10);
        member_title = (TextView)findViewById(R.id.member_title);
        id_edt = (EditText)findViewById(R.id.id_edt);
        password_edt = (EditText)findViewById(R.id.password_edt);
        verify_edt = (EditText)findViewById(R.id.verify_edt);
        Intent intent = getIntent();
        type = intent.getExtras().getInt("type");
        member_id = intent.getStringExtra("member_id");
        overlap_btn = (Button) findViewById(R.id.overlap_btn);
        attraction_chk = (CheckBox)findViewById(R.id.attraction_chk);
        cultural_facility_chk = (CheckBox)findViewById(R.id.cultural_faclity_chk);
        event_chk = (CheckBox)findViewById(R.id.event_chk);
        leports_chk = (CheckBox)findViewById(R.id.leports_chk);
        shopping_chk = (CheckBox)findViewById(R.id.shopping_chk);
        cafeteria_chk = (CheckBox)findViewById(R.id.cafeteria_chk);
        join_btn = (Button) findViewById(R.id.join_btn);
        retire_btn = (Button)findViewById(R.id.retire_btn);
        try {
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(type ==0) {
            member_title.setText("????????????");
            retire_btn.setVisibility(View.GONE);
            id_edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    overlap_btn.setFocusable(true);
                    overlap_btn.setClickable(true);
                    overlap_btn.setText("????????????");
                    isChecked = false;
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            overlap_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(id_edt.getText().toString().equals("")){
                            errorHandle("ID??? ???????????????");
                        }
                        else {
                            JSONObject json_data = new ServerConnector(1).execute(appInfo.metaData.getString("select_member"), "member_id", member_id).get();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            join_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (id_edt.equals("")) {
                        errorHandle("ID??? ???????????? ???????????????.");
                        id_edt.post(new Runnable() {
                            @Override
                            public void run() {
                                id_edt.setFocusableInTouchMode(true);
                                id_edt.requestFocus();
                                InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                                imm.showSoftInput(id_edt,0);
                            }
                        });
                    }
                    else if(password_edt.equals("")){
                        errorHandle("??????????????? ???????????? ???????????????.");
                        password_edt.post(new Runnable() {
                            @Override
                            public void run() {
                                password_edt.setFocusableInTouchMode(true);
                                password_edt.requestFocus();
                                InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                                imm.showSoftInput(password_edt,0);
                            }
                        });
                    }
                    else if(verify_edt.equals("")){
                        errorHandle("???????????? ????????? ?????? ???????????????.");
                        verify_edt.post(new Runnable() {
                            @Override
                            public void run() {
                                verify_edt.setFocusableInTouchMode(true);
                                verify_edt.requestFocus();
                                InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                                imm.showSoftInput(verify_edt,0);
                            }
                        });
                    }
                    else if(!attraction_chk.isChecked()&&!cultural_facility_chk.isChecked()&&!event_chk.isChecked()&&!leports_chk.isChecked()&&!shopping_chk.isChecked()&&!cafeteria_chk.isChecked()){
                        errorHandle("???????????? 1??? ?????? ????????? ?????????.");
                    }
                    else {
                        if (isChecked) {
                            if (password_edt.getText().toString().equals(verify_edt.getText().toString())) {
                                try {
                                    String ac = "N", cc = "N", ec = "N", lc = "N", sc = "N", fc = "N";
                                    if (attraction_chk.isChecked())ac="Y";
                                    if (cultural_facility_chk.isChecked())cc="Y";
                                    if (event_chk.isChecked())ec="Y";
                                    if (leports_chk.isChecked())lc="Y";
                                    if (shopping_chk.isChecked())sc="Y";
                                    if (cafeteria_chk.isChecked())fc="Y";
                                    new ServerConnector(1).execute(appInfo.metaData.getString("insert_member"), "member_id", id_edt.getText().toString(),
                                            "password", password_edt.getText().toString(),"AC",ac,"CC",cc,"EC",ec,"LC",lc,"SC",sc,"FC",fc);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent();
                                intent.putExtra("id", id_edt.getText().toString());
                                intent.putExtra("password", password_edt.getText().toString());
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                errorHandle("??????????????? ???????????? ????????????.");
                            }
                        } else {
                            errorHandle("ID ?????? ????????? ????????????.");
                        }
                    }
                }
            });
        }

        //?????? ?????? ????????? ???
        else {
            member_title.setText("?????? ??????");
            id_edt.setText(member_id);
            overlap_btn.setVisibility(View.GONE);
            id_edt.setFocusable(false);
            id_edt.setClickable(false);
            join_btn.setText("????????????");
            retire_btn.setVisibility(View.VISIBLE);
            retire_btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MemberView.this);
                    builder.setTitle("?????? ??????");
                    builder.setMessage("?????? ????????? ?????????.\n ????????? ????????? ????????? ????????? ???????????????. ?????????????????????????");
                    builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new ServerConnector(1).execute(appInfo.metaData.getString("delete_member"),"member_id",member_id);
                            Intent intent = new Intent(getApplicationContext(), LoginView.class);
                            startActivity(intent);
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                    builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            });
            password_edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(password_edt.getText().toString().equals(""))
                        isPwdChanged = false;
                    else
                        isPwdChanged = true;
                }
                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            try {
                new ServerConnector(2).execute(appInfo.metaData.getString("select_interest"), "member_id", member_id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            join_btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isPwdChanged &&!password_edt.getText().toString().equals(verify_edt.getText().toString()))
                        errorHandle("??????????????? ???????????????");
                    else {
                        String pwd = "N";
                        if(isPwdChanged)
                            pwd = password_edt.getText().toString();

                        String ac = "N", cc = "N", ec = "N", lc = "N", sc = "N", fc = "N";
                        if (attraction_chk.isChecked()) ac = "Y";
                        if (cultural_facility_chk.isChecked()) cc = "Y";
                        if (event_chk.isChecked()) ec = "Y";
                        if (leports_chk.isChecked()) lc = "Y";
                        if (shopping_chk.isChecked()) sc = "Y";
                        if (cafeteria_chk.isChecked()) fc = "Y";
                        new ServerConnector(1).execute(appInfo.metaData.getString("update_member"), "member_id", id_edt.getText().toString(),
                                "password", pwd, "AC", ac, "CC", cc, "EC", ec, "LC", lc, "SC", sc, "FC", fc);
                        finish();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = null;
        if(type ==0) {
            SharedPreferences pref = getSharedPreferences("TripPilotAutoLogin", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            intent = new Intent(this, LoginView.class);
        }
        else{
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void errorHandle(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage(msg);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    class ServerConnector extends AsyncTask<String,Void,JSONObject> {
        int type;
        public ServerConnector(int type){
            this.type = type;
        }
        @Override
        protected JSONObject doInBackground(String... strings) {
            HttpURLConnection con = null;
            try {
                URL myurl = new URL(strings[0]);
                con = (HttpURLConnection) myurl.openConnection();
                con.setDefaultUseCaches(false);
                con.setDoInput(true);                         // ???????????? ?????? ?????? ??????
                con.setDoOutput(true);                       // ????????? ?????? ?????? ??????
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
                StringBuffer buffer = new StringBuffer();

                for(int i =1; i<strings.length;i+=2){
                    buffer.append(strings[i]).append("=").append(strings[i+1]);
                    if(i < strings.length-2)
                        buffer.append("&");
                }

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                pw.write(buffer.toString());
                pw.flush();

                int response = con.getResponseCode();
                if (response >=200 && response <=300) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(con.getInputStream(),"UTF-8"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new JSONObject(builder.toString());
                } else {
                    Log.e("TAG-Server-error", "Connection Error!");
                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {
                con.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            JSONObject json_data = jsonObject;
            if(type == 1){
                try {
                    if (json_data.getString("result").equals("fail")) {
                        overlap_btn.setFocusable(false);
                        overlap_btn.setClickable(false);
                        overlap_btn.setText("?????? ??????");
                        isChecked = true;
                    } else {
                        errorHandle("ID??? ?????????????????????.");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if(type==2){
                try {
                    JSONArray list = json_data.getJSONArray("list");
                    int size = list.length();
                    for (int i = 0; i < size; i++) {
                        json_data = list.getJSONObject(i);
                        int interest = Integer.parseInt(json_data.getString("interest"));
                        if (interest == 12) attraction_chk.setChecked(true);
                        else if (interest == 14) cultural_facility_chk.setChecked(true);
                        else if (interest == 15) event_chk.setChecked(true);
                        else if (interest == 28) leports_chk.setChecked(true);
                        else if (interest == 38) shopping_chk.setChecked(true);
                        else if (interest == 39) cafeteria_chk.setChecked(true);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
