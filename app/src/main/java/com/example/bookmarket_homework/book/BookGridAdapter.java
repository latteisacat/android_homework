package com.example.bookmarket_homework.book;

import static com.example.bookmarket_homework.BooksActivity.bookRepository;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookmarket_homework.BookInfoActivity;
import com.example.bookmarket_homework.R;

//GridView를 위한 어댑터, 약간 다르게 구현해보았다.
public class BookGridAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return bookRepository.Books.size();
    }

    @Override
    public Object getItem(int position) {
        return bookRepository.Books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        Book book = bookRepository.Books.get(position);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.gridview_book_item, parent, false);
        ImageView bookImage = convertView.findViewById(R.id.book_grid);
        TextView bookPrice = convertView.findViewById(R.id.book_price_grid);
        // 역시 여기서도 View 생성 시 image를 바인딩해준다.
        switch (book.bookid) {
            case "BOOK1234":
                bookImage.setImageResource(R.drawable.book11);
                break;
            case "BOOK1235":
                bookImage.setImageResource(R.drawable.book21);
                break;
            case "BOOK1236":
                bookImage.setImageResource(R.drawable.book31);
                break;
            case "BOOK1237":
                bookImage.setImageResource(R.drawable.book41);
                break;
        }
        bookPrice.setText("정가 : " + book.price + "원");
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(),book.name, Toast.LENGTH_LONG).show();
                BookItemIntent(context, book.bookid, book.name, book.price, book.date, book.writer, book.page, book.description, book.category);
            }
        });

        return convertView;
    }
    // 책의 이미지를 클릭하면 BookInfoActivity로 간다.
    private void BookItemIntent(Context context, String bookid, String name, String price, String date, String writer, String page, String description, String category){
        Intent intent  = new Intent(context, BookInfoActivity.class);
        intent.putExtra("bookid", bookid);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("date", date);
        intent.putExtra("writer", writer);
        intent.putExtra("page", page);
        intent.putExtra("description", description);
        intent.putExtra("category", category);
        intent.putExtra("state", 1);
        context.startActivity(intent);
    }
}
