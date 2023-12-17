package com.example.bookmarket_homework;


import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bookmarket_homework.model.CartRepository;
import com.google.android.material.navigation.NavigationView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    // 장바구니를 어떤 화면을 가도 유지할 수 있게 전역 static변수로 설정
    public static CartRepository cartRepositoryObj = new CartRepository();
    ImageButton button1, button2, button3, button4;
    private ViewPager2 sliderViewPager; // 커버 이미지 슬라이더 뷰
    private LinearLayout layoutIndicator, viewLinearLayout;
    private int[] images = new int[]{R.drawable.cover01, R.drawable.cover02, R.drawable.latte, R.drawable.banana};
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigation;
    int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // 객체와 아이디 연결
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigation = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer_layout);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);
        viewLinearLayout = findViewById(R.id.view_linear_layout);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        // 메뉴 바의 로그인, 설정, 내 정보에 onClickListener 연결
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_login:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_login, null);
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.show();

                        Button loginButton = dialogView.findViewById(R.id.loginButton);
                        loginButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView ID = dialogView.findViewById(R.id.userID);
                                TextView PW = dialogView.findViewById(R.id.userPassword);

                                dialog.dismiss();
                                String message = "아이디: " + ID.getText() + "비밀번호: " + PW.getText();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case R.id.menu_setting:
                        Toast.makeText(getApplicationContext(), "설정", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_myinfo:
                        Toast.makeText(getApplicationContext(), "내 정보", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });

        sliderViewPager.setOffscreenPageLimit(1);
        // 현재 보이지 않는 화면의 생명주기 관리, 1 일경우 좌우 하나 씩 미리 로딩된다.
        sliderViewPager.setAdapter(new ImageSliderAdapter(this, images));

        // ViewPager의 콜백함수 지정
        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                setCurrentIndicator(position);
            }
        });

        // 하단의 이미지 갯수 표시
        setupIndicators(images.length);

        // 타이머를 통해 상단 배너 화면이 자동으로 넘겨지도록 코드 수정
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(currentPage == images.length ){
                    currentPage = 0;
                }
                else{
                    sliderViewPager.setCurrentItem(currentPage++, true);
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },2000, 2000);

        setButtonsAction();
    }

    private void setButtonsAction() {
        String[] message = new String[]{"도서목록", "동영상 강좌", "고객센터", "마이페이지"};
        ImageButton[] imageButtonList = new ImageButton[]{button1, button2, button3, button4};
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            // 버튼별로 리스너 부착
            imageButtonList[i].setOnClickListener(view -> {
                Toast.makeText(getApplicationContext(), message[finalI], Toast.LENGTH_SHORT).show();

                // 도서목록 버튼은 인텐트 전환하도록 설정
                if (finalI == 0) {
                    Intent backIntent = new Intent(MainActivity.this, BooksActivity.class);
                    startActivity(backIntent);
                    finish();
                }
            });
        }
    }

    // 상단 배너의 인디케이터(화면이 넘어갔는지 알려주는 동그란 점) 셋업 코드
    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_in_active));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }
    // 현재 상태에 맞게 인디케이터를 변경해주는 코드
    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_in_active
                ));
            }
        }
    }

    // 뒤로가기 버튼을 눌렀을 시 동작
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), "종료", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true); // 태스크를 백그라운드로 이동
            // 종료
            if (Build.VERSION.SDK_INT >= 21) {
                // 액티비티 종료 + 태스크 리스트에서 지우기
                finishAndRemoveTask();
            } else {
                // 액티비티 종료
                finish();
            }
            System.exit(0);
            return true;
        }
        return false;
    }

    // 위의 onKeyDown과 겹치지만 drawerLayout을 통해 생성된 뒤로가기 버튼을 위한 동작이기에 별개로 분류
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawers();
        }
        else{
            super.onBackPressed();
        }
    }
}