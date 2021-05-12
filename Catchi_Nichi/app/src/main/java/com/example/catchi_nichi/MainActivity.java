package com.example.catchi_nichi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    //Retrofit
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(RetrofitAPI.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final RetrofitAPI apiService = retrofit.create(RetrofitAPI.class);

    String nickName;
    TextView memberNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //수신데이터
        Intent intent = getIntent();
        nickName = intent.getStringExtra("nick");
        Log.i("Main_Nick",nickName);
        memberNotice = findViewById(R.id.memNotice);
        memberNotice.setText(nickName+" 님께 추천드리는 향수입니다.");

    }


    //buttonListener 구현
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.searchButton:
                Intent intent = new Intent(getApplicationContext(), mainSearchActivity.class);
                intent.putExtra("nick",nickName);
                startActivity(intent);
                finish();
                break;

            case R.id.camera_search:
                Intent intent2 = new Intent(getApplicationContext(), CameraSearchActivity.class);
                intent2.putExtra("nick",nickName);
                startActivity(intent2);
                finish();
                break;

            case R.id.smellnote_btn:
                Intent intent3 = new Intent(getApplicationContext(), SmellNoteMainActivity.class);
                intent3.putExtra("nick",nickName);
                startActivity(intent3);
                finish();
                break;

            case R.id. home_btn:
                Intent intent4 = new Intent(getApplicationContext(), MainActivity.class);
                intent4.putExtra("nick",nickName);
                startActivity(intent4);
                finish();
                break;

            case R.id. recommend_btn:
                Intent intent5 = new Intent(getApplicationContext(), RecommendMainActivity.class);
                intent5.putExtra("nick",nickName);
                startActivity(intent5);
                finish();
                break;


        }
    }
}