package com.example.bookmarket_homework.order;

import static com.example.bookmarket_homework.MainActivity.cartRepositoryObj;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bookmarket_homework.R;
import com.example.bookmarket_homework.cart.CartActivity;


public class ShoppingActivity extends  AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 화살표(<-) 표시하기

        EditText etName = findViewById(R.id.editTextName);
        EditText etDate = findViewById(R.id.editTextDate);
        EditText etPhone = findViewById(R.id.editTextPhone);
        EditText etZipcode = findViewById(R.id.editTextZipcode);
        EditText etAddress= findViewById(R.id.editTextAddress);


        Button btCancel = findViewById(R.id.buttonCancel);
        Button btSubmit = findViewById(R.id.buttonSubmit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingActivity.this, OrderActivity.class);
                intent.putExtra("name", etName.getText().toString());
                intent.putExtra("date", etDate.getText().toString());
                intent.putExtra("phone", etPhone.getText().toString());
                intent.putExtra("zipcode", etZipcode.getText().toString());
                intent.putExtra("address", etAddress.getText().toString());
                intent.putExtra("total", Integer.toString(cartRepositoryObj.grandTotalCartItems()));
                startActivity(intent);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingActivity.this, CartActivity.class);
                startActivity(intent);
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