package com.example.bookmarket_homework.book;

import com.example.bookmarket_homework.model.CartBook;

import java.util.ArrayList;

public class BookRepository {

    public ArrayList<Book> Books  = new ArrayList<Book>();

    public void addBookItems(Book book ) {

        Book goods = book;
        Books.add(goods);
    }
}
