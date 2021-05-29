package com.example.catchi_nichi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CameraResultActivity extends AppCompatActivity {
    //Retrofit
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(RetrofitAPI.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static final RetrofitAPI apiService = retrofit.create(RetrofitAPI.class);

    String nick;
    ArrayList<HashMap<String, String>> searchList ;
    ImageButton perfumeImageBtn;
    int temp;
    Bitmap bitmap;
    LinearLayout resultView;
    LinearLayout group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraresult);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //수신데이터
        Intent intent = getIntent();
        nick = intent.getStringExtra("nick");
        searchList = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("searchList");


        TextView resultText = findViewById(R.id.resultView);
        resultText.setTypeface(Typeface.DEFAULT_BOLD);
        resultText.setText("사진 검색 결과가 "+searchList.size()+" 개 검색되었습니다.");

        resultView = findViewById(R.id.Info);
        resultView.setOrientation(LinearLayout.VERTICAL);

        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        final int textWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 210, getResources().getDisplayMetrics());
        final int textHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());

        for(temp=0;temp<searchList.size();temp++){

            perfumeImageBtn = new ImageButton(this);
            perfumeImageBtn.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            perfumeImageBtn.setId(temp);
            perfumeImageBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
            perfumeImageBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            final int x = temp;

            perfumeImageBtn.setOnClickListener(v -> {
                Intent intent2 = new Intent(getApplicationContext(), PerfumeDataActivity.class);
                intent2.putExtra("img", searchList.get(x).get("img"));
                intent2.putExtra("kr_name", searchList.get(x).get("kr_name"));
                intent2.putExtra("en_name", searchList.get(x).get("en_name"));
                intent2.putExtra("brand", searchList.get(x).get("brand"));
                intent2.putExtra("kr_brand", searchList.get(x).get("kr_brand"));
                intent2.putExtra("likes", searchList.get(x).get("likes"));
                intent2.putExtra("countingReview", searchList.get(x).get("countingReview"));
                intent2.putExtra("avgStars", searchList.get(x).get("avgStars"));
                intent2.putExtra("nick", nick);

                //화면전환
                intent2.putExtra("Activity","camera");
                intent2.putExtra("searchList",searchList);

                startActivity(intent2);
                finish();
            });

            group = new LinearLayout(this);
            group.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5,20,5,20);
            group.setLayoutParams(params);

            Thread mThread = new Thread(){
                public void run(){
                    try{
                        URL url = new URL(searchList.get(temp).get("img"));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start();

            try{
                mThread.join();
                perfumeImageBtn.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            TextView perfumeInfo = new TextView(this);
            perfumeInfo.setGravity(Gravity.CENTER);
            perfumeInfo.setLayoutParams(new LinearLayout.LayoutParams(textWidth,textHeight));
            perfumeInfo.setText("\n  "+ searchList.get(temp).get("kr_name") + "\n  " + searchList.get(temp).get("brand") + "\n  " + "Likes : " + searchList.get(temp).get("likes")+ "\n  "+ "리뷰수 : " + searchList.get(temp).get("countingReview")+ "\n  "+ "평균별점 : " + searchList.get(temp).get("avgStars")+ "\n");
            //perfumeInfo.setTypeface(Typeface.DEFAULT_BOLD);
            group.setGravity(Gravity.CENTER);


            group.addView(perfumeImageBtn);
            group.addView(perfumeInfo);
            resultView.addView(group);
        }

        Log.i("searchResult", String.valueOf(searchList));
    }


    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.home_btn:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("nick", nick);
                startActivity(intent);
                finish();
                break;

            case R.id.smellnote_btn:
                Intent intent3 = new Intent(getApplicationContext(), SmellNoteMainActivity.class);
                intent3.putExtra("nick", nick);
                startActivity(intent3);
                finish();
                break;

            case R.id.recommend_btn:
                Intent intent5 = new Intent(getApplicationContext(), RecommendMainActivity.class);
                intent5.putExtra("nick", nick);
                startActivity(intent5);
                finish();
                break;

            case R.id.camera_search:
                Intent intent6 = new Intent(getApplicationContext(), CameraSearchActivity.class);
                intent6.putExtra("nick", nick);
                startActivity(intent6);
                finish();
                break;

            case R.id.mypage_btn:
                Intent intent7 = new Intent(getApplicationContext(), MyPageActivity.class);
                intent7.putExtra("nick", nick);
                startActivity(intent7);
                finish();
                break;
        }
    }

}
