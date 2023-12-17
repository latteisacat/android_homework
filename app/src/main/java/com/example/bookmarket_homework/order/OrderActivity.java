package com.example.bookmarket_homework.order;

import static com.example.bookmarket_homework.MainActivity.cartRepositoryObj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmarket_homework.MainActivity;
import com.example.bookmarket_homework.R;

public class OrderActivity extends AppCompatActivity {
    OrderAdapter  orderAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 화살표(<-) 표시하기

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        String phone = intent.getStringExtra("phone");
        String zipcode = intent.getStringExtra("zipcode");
        String address = intent.getStringExtra("address");
        String total = intent.getStringExtra("total");


        TextView tvDate = findViewById(R.id.textViewDate);
        TextView tvName = findViewById(R.id.textViewName);
        TextView tvZipcode = findViewById(R.id.textViewZipcode);
        TextView tvAddress = findViewById(R.id.textViewAddress);
        TextView tvTotal = findViewById(R.id.textViewTotal);

        tvDate.setText("배송일 : " + date);
        tvName.setText("성명 : " + name + "(" + phone + ")");
        tvZipcode.setText("우편번호 : " + zipcode);
        tvAddress.setText("주소 : " + address);
        tvTotal.setText(total);



        RecyclerView recyclerviewCart = findViewById(R.id.order_recyclerview);
        orderAdapter = new OrderAdapter(OrderActivity.this);
        recyclerviewCart.setAdapter(orderAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewCart.setLayoutManager(linearLayoutManager);

        Button orderSubmit = findViewById(R.id.buttonSubmit);
        Button orderCancel = findViewById(R.id.buttonCancel);

        orderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderActivity.this);
                alertDialog.setTitle("상품 주문 완료");
                alertDialog.setMessage("주문해 주셔서 감사합니다.");
                alertDialog.setIcon(R.drawable.dialog_cat);


                alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cartRepositoryObj.cartBooks.clear();
                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertDialog.show();


            }
        });
        orderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderActivity.this);
                alertDialog.setTitle("도서 상품 주문 취소");
                alertDialog.setMessage("주문을 취소합니다.");
                alertDialog.setIcon(R.drawable.dialog_cat);


                alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cartRepositoryObj.cartBooks.clear();
                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                        startActivity(intent);
                       finish();
                    }
                });


            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: // 화살표(<-) 클릭 시 이전 액티비티(화면)로 이동하기
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
