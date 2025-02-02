package com.example.laundryargan.tampilan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundryargan.R;
import com.example.laundryargan.app.AppController;
import com.example.laundryargan.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    Button btn_register, btn_login;
    EditText txt_iduser, txt_nama, txt_username, txt_password;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL  +"register1.php";
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if(conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()){

            } else{
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
        btn_login = (Button) findViewById(R.id.btn_link_login);
        btn_register = (Button) findViewById(R.id.btn_signup);
        txt_iduser = (EditText) findViewById(R.id.signup_input_id);
        txt_nama = (EditText) findViewById(R.id.signup_input_nama);
        txt_username = (EditText) findViewById(R.id.signup_input_username);
        txt_password = (EditText) findViewById(R.id.signup_input_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, Login.class);
                finish();
                startActivity(intent);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String iduser = txt_iduser.getText().toString();
                String nama = txt_nama.getText().toString();
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                if(conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()){
                    checkRegister(iduser, nama, username, password);
                    
                } else{
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkRegister(final String iduser, final String nama, final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
//        showDialog();
        hideDialog();
        
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
//                hideDialog();
                showDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        Log.e("Successfully Register!", jObj.toString());
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        txt_iduser.setText("");
                        txt_nama.setText("");
                        txt_username.setText("");
                        txt_password.setText("");
                        Intent intent = new Intent(RegisterActivity.this, Login.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Register Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("iduser", iduser);
                params.put("nama", nama);
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, Login.class);
        finish();
        startActivity(intent);
    }
}
