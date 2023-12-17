package com.example.bookmarket_homework.model;

public class CartBook {

    public String bookid;
    public String name;
    public int price = 0;
    public String date ;
    public String writer;
    public String page;

    public CartBook(){}

    public CartBook(String bookid, String name, int price, String date, String writer, String page, String description, String category) {
        this.bookid = bookid;
        this.name = name;
        this.price = price;
        this.date = date;
        this.writer = writer;
        this.page = page;
        this.description = description;
        this.category = category;
    }

    public String description;
    public String category;
    public int quantity =0 ;
    public boolean isChecked = false;

}
