package com.example.bookmarket_homework.cart;

import static com.example.bookmarket_homework.MainActivity.cartRepositoryObj;
import static com.example.bookmarket_homework.BookInfoActivity.allChooseCheckBoxState;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmarket_homework.BookInfoActivity;
import com.example.bookmarket_homework.R;
import com.example.bookmarket_homework.model.CartBook;
import com.example.bookmarket_homework.order.ShoppingActivity;

import java.util.ArrayList;



public class CartActivity extends AppCompatActivity {

    CartAdapter cartAdapter;

    static CheckBox allChooseCheckBox;
    static TextView cartTotalPrice;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 화살표(<-) 표시하기

        cartTotalPrice = findViewById(R.id.cart_total_price);
        cartTotalPrice.setText(Integer.toString(cartRepositoryObj.grandTotalCartItems()));

        // 장바구니에 RecyclerView 적용
        RecyclerView recyclerviewCart = findViewById(R.id.cart_recyclerview);
        cartAdapter = new CartAdapter(CartActivity.this);
        recyclerviewCart.setAdapter(cartAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewCart.setLayoutManager(linearLayoutManager);

        //
        cartAdapter.setOnSelectChangedListener(new CartAdapter.OnSelectChangedListener() {
            @Override
            public void onSelectChanged(ArrayList<CartBook> item) {
                refreshRecyclerView();
            }
        });




        allChooseCheckBox = findViewById(R.id.cart_selectall_checkbox);
        // 미리 설정한 static 변수로 check 상태 관리
        allChooseCheckBox.setChecked(allChooseCheckBoxState);
        // 하나만 상태를 바꿔도 전부 다 바뀌는 버그 수정
        allChooseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allChooseCheckBoxState){
                    doSelectNone();
                    allChooseCheckBoxState = false;
                }
                else{
                    doSelectAll();
                    allChooseCheckBoxState = true;
                }
            }
        });
//        allChooseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//               if (b) {
//                   doSelectAll();
//               }
//               else {
//                   doSelectNone();
//               }
//            }
//        });


        Button orderButton  = findViewById(R.id.cart_order_button);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ShoppingActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
            case R.id.order_button  :
                // 주문서 작성 화면으로 이동
                Intent intent = new Intent(CartActivity.this, ShoppingActivity.class);
                startActivity(intent);
                break;

            case R.id.delete_button  :
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
                alertDialog.setTitle("도서 상품 삭제");
                alertDialog.setMessage("장바구니에 담은 상품을 모두 삭제하겠습니까?");
                alertDialog.setIcon(R.drawable.dialog_cat);

                alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cartRepositoryObj.cartBooks.clear();
                        setSelectAllCheckBoxState();
                        cartUpdate();

                    }
                });

                alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CartActivity.this, BookInfoActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
    // 전체 선택
    void  doSelectAll()  {
        int num = cartAdapter.getItemCount();
        allChooseCheckBoxState = true;
        if (num > 0) {
            boolean isChanged = false;
            for (int i=0 ; i<=num-1;i++) {
                CartBook item = cartRepositoryObj.cartBooks.get(i);

                if (!item.isChecked) {
                    item.isChecked = true;
                    isChanged = true;
                }
            }
            if (isChanged) {
                refreshRecyclerView();
            }
        }
    }

    // 전체 선택 취소
    void doSelectNone() {
        int num = cartAdapter.getItemCount();
        allChooseCheckBoxState = false;
        if ( num > 0) {
            boolean isChanged = false;
            for (int i = 0; i<=num-1 ;i++) {
                CartBook item  = cartRepositoryObj.cartBooks.get(i);
                if (item.isChecked) {
                    item.isChecked = false;
                    isChanged = true;
                }
            }
            if (isChanged) {
                refreshRecyclerView();
            }
        }
    }

    // 아이템의 상태를 확인하고 갱신하기 위한 코드
    private void refreshRecyclerView() {
        cartAdapter.notifyItemRangeRemoved(0, cartRepositoryObj.cartBooks.size());
        cartTotalPrice.setText(Integer.toString(cartRepositoryObj.grandTotalCartItems()));
        setSelectAllCheckBoxState();
    }
    // 모든 아이템이 체크된 상태인지 아닌지 확인하기 위한 코드
    private void setSelectAllCheckBoxState() {

        if (cartRepositoryObj.cartBooks == null) {
            allChooseCheckBox.setChecked(false);
            return;
        }
        for (int i=0 ; i < cartRepositoryObj.cartBooks.size(); i++) {
            if (!cartRepositoryObj.cartBooks.get(i).isChecked) {
                allChooseCheckBox.setChecked(false);
                allChooseCheckBoxState = false;
                return;
            }
        }
        allChooseCheckBox.setChecked(true);
        allChooseCheckBoxState = true;
    }
    public static void cartUpdate(){
        cartRepositoryObj.countCartItems();
        cartTotalPrice.setText(Integer.toString(cartRepositoryObj.grandTotalCartItems()));
    }
}
