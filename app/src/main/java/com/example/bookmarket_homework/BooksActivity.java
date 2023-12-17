package com.example.bookmarket_homework;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmarket_homework.book.Book;
import com.example.bookmarket_homework.book.BookAdapter;
import com.example.bookmarket_homework.book.BookGridAdapter;
import com.example.bookmarket_homework.book.BookRepository;

import java.util.Random;


public class BooksActivity extends AppCompatActivity {
    GridView gridView; // 무한 스크롤 그리드 뷰
    BookGridAdapter bookGridAdapter; // 무한 스크롤 리사이클 뷰를 위한 어댑터
    ImageView menu1, menu2; // 레이아웃 선택 버튼
    LinearLayout bookPage1, bookPage2; // 도서 레이아웃
    Integer state; // 화면 전환 시 이전 화면이 그리드 뷰였는지, 리사이클 뷰 였는지 저장하기 위한 변수
    BookAdapter bookAdapter;
    Book book1, book2, book3, book4;
    // 책들의 값을 어떤 화면에서도 고정으로 사용하기 위해 이 역시 public static으로 선언
    public static BookRepository bookRepository = new BookRepository();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        // 책을 DB에서 받아왔다고 가정하고 생성
        setBooks();
        addRandomBooks();

        gridView = findViewById(R.id.book_grid_view);
        bookGridAdapter = new BookGridAdapter();
        gridView.setAdapter(bookGridAdapter);

        // 리사이클 뷰에 어댑터를 적용하여 무한 스크롤 화면 구현
        RecyclerView recyclerViewBook = findViewById(R.id.book_recyclerview);
        bookAdapter = new BookAdapter(BooksActivity.this);
        recyclerViewBook.setAdapter(bookAdapter);
        LinearLayoutManager bookLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewBook.setLayoutManager(bookLinearLayoutManager);


        // 초기 화면은 리사이클 뷰 이므로 0
        state = 0;
        // 전환 애니메이션 설정
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(500);

        // 객체와 아이디 연결
        menu1 = findViewById(R.id.menu_1);
        menu2 = findViewById(R.id.menu_2);
        bookPage1 = findViewById(R.id.book_page_1);
        bookPage2 = findViewById(R.id.book_page_2);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu1.setImageResource(R.drawable.list_type1); // 버튼 1 활성화 아이콘으로 변경
                menu2.setImageResource(R.drawable.list_type22); // 버튼 2 비활성화 아이콘으로 변경
                state = 0;
                // 도서 그리드 숨기기
                bookPage2.clearAnimation();
                bookPage2.setVisibility(View.GONE);
                // 도서 리스트 보이기
                bookPage1.setVisibility(View.VISIBLE);
                bookPage1.startAnimation(animation);
            }
        });

        // 그리드로 보기 버튼 클릭할 때
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu1.setImageResource(R.drawable.list_type12); // 버튼 1 비활성화 아이콘으로 변경
                menu2.setImageResource(R.drawable.list_type2); // 버튼 2 활성화 아이콘으로 변경
                state = 1;
                // 도서 리스트 숨기기
                bookPage1.clearAnimation();
                bookPage1.setVisibility(View.GONE);
                // 도서 그리드 보이기
                bookPage2.setVisibility(View.VISIBLE);
                bookPage2.startAnimation(animation);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // 안드로이드의 뒤로 가기 버튼 누를 때 메인화면으로 인텐트 전환
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goHome();
            return true;
        }
        return false;
    }

    // state값을 다시 받아옴으로써 이전 상태 복구
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(state == 0){
                bookPage2.setVisibility(View.GONE);
                bookPage1.setVisibility(View.VISIBLE);
            }
            else{
                bookPage1.setVisibility(View.GONE);
                bookPage2.setVisibility(View.VISIBLE);
            }
        } else {   // RESULT_CANCEL
            Toast.makeText(getApplicationContext(), "Something is wrong....", Toast.LENGTH_SHORT).show();
        }
    }

    // 검색 기능, 현재는 껍데기만 구현 DB가 없기 때문
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_books, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("검색어(도서명)을 입력해주세요.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // 우측 옵션 기능 구현
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
            case R.id.menu_home:
                goHome();
                break;
            case R.id.menu_video:
                Toast.makeText(getApplicationContext(),"동영상 강좌", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_customer:
                Toast.makeText(getApplicationContext(),"고객센터", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_mypage:
                Toast.makeText(getApplicationContext(),"마이페이지", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 메인 화면으로 나가는 코드
    private void goHome(){
        Toast.makeText(getApplicationContext(), "메인 메뉴", Toast.LENGTH_SHORT).show();
        Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backIntent);
    }
    // 책을 DB에서 받아왔다고 가정 하고 설정하는 코드
    private void setBooks(){
        String book1id = "BOOK1234";
        String book1name = "자바 코딩의 기술";
        String book1price = "22000";
        String book1date = "2020-07-30";
        String book1writer = "사이먼 하러,리누스 디에츠,요르그 레너드/심지현";
        String book1page = "264쪽";
        String book1shortDescribe = "현장에서 뽑은 70가지 예제로 배우는 코드 잘 짜는 법";
        String book1description = "코딩 스킬을 개선하는 가장 좋은 방법은 전문가의 코드를 읽는 것이다. 오픈 소스 코드를 읽으면서 이해하면 좋지만, 너무 방대하고 스스로 맥락을 찾는 게 어려울 수 있다. 그럴 땐 이 책처럼 현장에서 자주 발견되는 문제 유형 70가지와 해법을 비교하면서 자신의 코드에서 개선할 점을 찾는 것이 좋다.";
        String book1category = "프로그래밍/오픈소스";
        book1 = new Book(book1id, book1name, book1price, book1date, book1writer, book1page, book1shortDescribe, book1description, book1category);

        String book2id = "BOOK1235";
        String book2name = "머신 러닝을 다루는 기술 with 파이썬, 사이킷런";
        String book2price = "34000";
        String book2date = "2020-06-03";
        String book2writer = "마크 페너/황준식";
        String book2page = "624쪽";
        String book2shortDescribe = " with 파이썬, 사이킷런";
        String book2description = "저자는 오랫동안 다양한 사람들에게 머신 러닝을 가르치면서 효과적인 학습 방법을 고안했고, 그대로 책에 담았다. 이 책은 그림과 스토리로 개념을 설명하고 바로 파이썬 코드로 구현하는 것에서 시작한다. 수학적 증명을 깊게 파고들거나 개념을 설명하기 위해 수식에 의존하지 않으며, 필요한 수학은 고등학교 수준으로 그때마다 첨가하여 설명한다. 또한, 바닥부터 모델을 구현하지 않고, 넘파이, 판다스, 사이킷런처럼 잘 구현된 강력한 파이썬 라이브러리를 사용해 실용적으로 접근한다. 개념과 기술을 잘 보여주는 양질의 예제를 직접 실행하며 머신 러닝 개념을 이해할 수 있다. ";
        String book2category = "데이터베이스/데이터분석";
        book2 = new Book(book2id, book2name, book2price, book2date, book2writer, book2page, book2shortDescribe, book2description, book2category);

        String book3id = "BOOK1236";
        String book3name = "모던 리눅스 관리";
        String book3price = "30000";
        String book3date = "2019-10-10";
        String book3writer = "데이비드 클린턴(David Cliton)/강석주";
        String book3page = "472쪽";
        String book3shortDescribe = "그림으로 이해하고 만들면서 익히는";
        String book3description = "이 책은 최신 기술을 활용한 리눅스 관리 방법을 가상화, 연결, 암호화, 네트워킹, 이미지관리, 시스템 모니터링의 6가지 주제로 나눠 설명한다. 가상 머신에 리눅스를 설치하고 서버를 구축하는 방법뿐만 아니라 구축 이후에 리눅스를 관리하고 운영하며 겪을수 있는 다양한 문제를 해결하는 방법까지 다룬다. VM과 컨테이너를 이용한 가상화, AWS S3를 이용한 데이터 백업, Nextcloud를 이용한 파일공유 서버 구축, 앤서블을 이용한 데브옵스 환경 구축 등 최신 기술을 활용한 실용적인 12가지 프로젝트로 실무에 필요한 리눅스 관리 방법을 배울 수 있다.";
        String book3category = "임베디드/시스템/네트워크";
        book3 = new Book(book3id, book3name, book3price, book3date, book3writer, book3page, book3shortDescribe, book3description, book3category);

        String book4id = "BOOK1237";
        String book4name = "유니티 교과서";
        String book4price = "28000";
        String book4date = "2019-10-30";
        String book4writer = "기타무라 마나미/김은철,유세라";
        String book4page = "456쪽";
        String book4shortDescribe = "이공계를 위한 프로그래밍, 자료구조, 알고리즘";
        String book4description = "[유니티 교과서, 개정 3판]은 유니티를 사용해 2D/3D 게임과 애니메이션을 만들면서 유니티 기초 지식과 함께 게임 제작 흐름을 익히는 것을 목적으로 한다. 유니티를 설치한 후 C# 핵심 문법을 학습하고, 이어서 여섯 가지 2D/3D 게임을 ‘게임 설계하기 → 프로젝트와 씬 만들기 → 씬에 오브젝트 배치하기 → 스크립트 작성하기 → 스크립트 적용하기’ 단계로 만들어 보면서 게임 제작 흐름을 익힌다. ";
        String book4category = "게임";
        book4 = new Book(book4id, book4name, book4price, book4date, book4writer, book4page, book4shortDescribe, book4description, book4category);
    }
    // 책이 많이 없으므로 같은 책을 여러 개 랜덤으로 집어넣는 코드
    private void addRandomBooks(){
        Book[] books={book1, book2, book3, book4};
        for(int i = 0; i < 40; i++){
            int index = (int)(Math.random()*4);
            bookRepository.addBookItems(books[index]);
        }
    }

}