package com.example.catchi_nichi;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback;
    Retrofit retrofit;
    RetrofitAPI apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        //Session.getCurrentSession().checkAndImplicitOpen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    public void onClick(View view) {
        switch(view.getId()){

            case R.id.join_btn:
                Intent join = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(join);
                finish();
                break;

            case R.id.login_btn:
                // 로그인 정보 서버로 보내기
                Intent login = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(login);
                finish();
                break;
        }
    }

    /**@Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }**/

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    if(getInfo(result)){
                        //Retrofit
                        retrofit = new Retrofit.Builder()
                                .baseUrl(RetrofitAPI.API_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        apiService = retrofit.create(RetrofitAPI.class);

                        Call<Post> Info = apiService.userInfoAPI(userInfo);
                        Info.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                if (response.isSuccessful() || response.body().getSuccess() == true){
                                    Log.i("response","success");
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    //intent.putExtra("name", result.getNickname());
                                    //intent.putExtra("profile", result.getProfileImagePath());
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                Log.i("response","failure");
                            }
                        });

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "가입하지 않은 아이디이거나 비밀번호가 일치하지 않습니다. ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    HashMap<String, Object> userInfo = new HashMap<>();
    private boolean getInfo(MeV2Response result) {
        PackageInfo packageInfo = null;

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            if (packageInfo == null) {
                Log.e("Info", "Info:null");
                return false;
            }

            for (Signature signature : packageInfo.signatures) {

                String snsId = String.valueOf(result.getId());
                String email = String.valueOf(result.getKakaoAccount().getEmail());
                String[] low = result.getKakaoAccount().getAgeRange().getValue().split("~");
                int age = (Integer.parseInt(low[0]) + Integer.parseInt(low[1])) / 2;
                String gender = String.valueOf(result.getKakaoAccount().getGender());

                userInfo.put("snsId", snsId);
                userInfo.put("email", email);
                userInfo.put("age", age);
                userInfo.put("gender", gender);
            }
            return true;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    }


