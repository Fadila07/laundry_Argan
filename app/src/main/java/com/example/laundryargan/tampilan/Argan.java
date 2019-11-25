package com.example.laundryargan.tampilan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.laundryargan.R;
import com.example.laundryargan.tampilan.Login;

public class Argan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argan);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Argan.this, Login.class);
                startActivity(i);
                finish();
            }
        }, 4000);
    }
}
