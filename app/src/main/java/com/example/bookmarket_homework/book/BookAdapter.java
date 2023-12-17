package com.example.bookmarket_homework.book;

import static com.example.bookmarket_homework.BooksActivity.bookRepository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmarket_homework.BookInfoActivity;
import com.example.bookmarket_homework.BooksActivity;
import com.example.bookmarket_homework.R;

import java.util.ArrayList;
import java.util.List;

//RecyclerView로 상품 목록 보여주기
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    Context context;

    public BookAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_book_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Book book = bookRepository.Books.get(position);

        holder.bookTitle.setText(book.name);
        holder.bookDescribe.setText(book.shortDescribe);
        holder.bookInfo.setText(book.totalInfo());
        // 역시 여기서 Image 리소스 설정
        switch (book.bookid) {
            case "BOOK1234":
                holder.bookPicture.setImageResource(R.drawable.book11);
                break;

            case "BOOK1235":
                holder.bookPicture.setImageResource(R.drawable.book21);
                break;

            case "BOOK1236":
                holder.bookPicture.setImageResource(R.drawable.book31);
                break;

            case "BOOK1237":
                holder.bookPicture.setImageResource(R.drawable.book41);
                break;
        }
        holder.bookPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(),book.name, Toast.LENGTH_LONG).show();
                BookItemIntent(book.bookid, book.name, book.price, book.date, book.writer, book.page, book.description, book.category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookRepository.Books.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookPicture;
        TextView bookTitle;
        TextView bookDescribe;
        TextView bookInfo;
        CardView bookParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookPicture = itemView.findViewById(R.id.book_image);
            bookTitle = itemView.findViewById(R.id.book_name);
            bookDescribe = itemView.findViewById(R.id.book_describe);
            bookInfo = itemView.findViewById(R.id.book_info);
            bookParentLayout = itemView.findViewById(R.id.book_parent_layout);
        }
    }
    // 책 이미지 클릭시 책의 정보화면으로 넘어감
    private void BookItemIntent(String bookid, String name, String price, String date, String writer, String page, String description, String category){
        Intent intent  = new Intent(context, BookInfoActivity.class);
        intent.putExtra("bookid", bookid);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("date", date);
        intent.putExtra("writer", writer);
        intent.putExtra("page", page);
        intent.putExtra("description", description);
        intent.putExtra("category", category);
        intent.putExtra("state", 0);
        context.startActivity(intent);
    }
}
