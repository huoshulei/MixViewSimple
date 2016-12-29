package com.hsl_4.mixviewsimple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void staggered(View view) {
        startActivity(new Intent(this, StaggeredActivity.class));
    }

    public void linear(View view) {
        startActivity(new Intent(this, LinearActivity.class));
    }

    public void grid(View view) {
        startActivity(new Intent(this, GridActivity.class));
    }
}
