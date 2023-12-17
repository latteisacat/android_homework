package com.example.bookmarket_homework;

import static com.example.bookmarket_homework.MainActivity.cartRepositoryObj;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookmarket_homework.cart.CartActivity;
import com.example.bookmarket_homework.model.CartBook;

public class BookInfoActivity extends AppCompatActivity {
    ImageView imgObj;
    TextView bookidObj, nameObj,priceObj, dateObj, writerObj, pageObj, descriptionObj, categoryObj, cartCount;
    Integer state;
    Button cartplus;
    ActivityResultLauncher<Intent> launcher;
    // 장바구니의 allChooseCheckBox가 나갔다 들어오면 무조건 true로 바뀌기 때문에
    // 해당 부분을 제어하기 위한 전역 static 변수
    public static Boolean allChooseCheckBoxState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_more_info);
        imgObj = findViewById(R.id.book_iv01);
        bookidObj = findViewById(R.id.book_tv01);
        nameObj = findViewById(R.id.book_tv02);
        priceObj = findViewById(R.id.book_tv03);
        dateObj = findViewById(R.id.book_tv04);
        writerObj = findViewById(R.id.book_tv05);
        pageObj = findViewById(R.id.book_tv06);
        descriptionObj = findViewById(R.id.book_tv07);
        categoryObj = findViewById(R.id.book_tv08);

        Intent intent = getIntent(); // 전송된 인텐트 받기
        String bookId = intent.getStringExtra("bookid");
        String bookName = intent.getStringExtra("name");
        String bookPrice = intent.getStringExtra("price");
        int bookPrice_int = Integer.parseInt(bookPrice);
        String date = intent.getStringExtra("date");
        String writer = intent.getStringExtra("writer");
        String page = intent.getStringExtra("page");
        String description = intent.getStringExtra("description");
        String category = intent.getStringExtra("category");
        bookidObj.setText(bookId);
        nameObj.setText(bookName);
        priceObj.setText(bookPrice);
        dateObj.setText(date);
        writerObj.setText(writer);
        pageObj.setText(page);
        descriptionObj.setText(description);
        categoryObj.setText(category);
        state = intent.getIntExtra("state", 0);

        // 카트에 담길 책 정보
        CartBook book = new CartBook(bookId, bookName, bookPrice_int, date, writer, page, description, category);
        // 장바구니 버튼
        cartplus = findViewById(R.id.book_button);

        cartplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookInfoActivity.this);

                alertDialog.setTitle("도서주문");
                alertDialog.setMessage("상품을 장바구니에 넣으시겠습니까?");
                alertDialog.setIcon(R.drawable.dialog_cat);

                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cartRepositoryObj.addCartItems(book);
                                int count = cartRepositoryObj.countCartItems();
                                cartCount.setText(Integer.toString(count));
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };
                alertDialog.setPositiveButton("예", listener);
                alertDialog.setNegativeButton("아니오", listener);
                alertDialog.show();
            }
        });


        switch(bookidObj.getText().toString()){
            case "BOOK1234" :
                imgObj.setImageResource(R.drawable.book11);
                break;
            case "BOOK1235" :
                imgObj.setImageResource(R.drawable.book21);
                break;
            case "BOOK1236" :
                imgObj.setImageResource(R.drawable.book31);
                break;
            case "BOOK1237" :
                imgObj.setImageResource(R.drawable.book41);
                break;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //장바구니에서 아이템을 삭제해도 바로 아이콘에 반영되지 않는 버그 수정
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int count = cartRepositoryObj.countCartItems();
                        cartCount.setText(Integer.toString(count));
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBookList("도서 목록");
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bookinfo, menu);
        RelativeLayout relativeLayout = (RelativeLayout)menu.findItem(R.id.menu_cart).getActionView();
        cartCount = relativeLayout.findViewById(R.id.cart_count);
        cartCount.setText(Integer.toString(cartRepositoryObj.countCartItems()));
        ImageView cartIcon = relativeLayout.findViewById(R.id.cart_icon);
        // 장바구니 아이콘 클릭 시 장바구니 화면으로 이동
        cartIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(BookInfoActivity.this, CartActivity.class);
                launcher.launch(cartIntent);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
            case R.id.menu_list:
                goBookList("도서 목록");
                break;
            case R.id.menu_home:
                goHome();
                break;
            case R.id.menu_cart:
                Toast.makeText(getApplicationContext(),"장바구니", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 메인 화면으로 복귀
    private void goHome(){
        Toast.makeText(getApplicationContext(), "메인 메뉴", Toast.LENGTH_SHORT).show();
        Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backIntent);
    }
    // 책이 있는 이전 화면으로 복귀
    private void goBookList(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Intent backIntent = new Intent(getApplicationContext(), BooksActivity.class);
        backIntent.putExtra("state", state);
        setResult(RESULT_OK, backIntent);
        finish();
    }
}