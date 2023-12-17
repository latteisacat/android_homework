package com.example.bookmarket_homework.book;

public class Book {
    public String bookid;
    public String name;
    public String price;
    public String date ;
    public String writer;
    public String page;
    public String shortDescribe;
    public String description;
    public String category;

    public Book(){}

    public Book(String bookid, String name, String price, String date, String writer, String page, String shortDescribe ,String description, String category) {
        this.bookid = bookid;
        this.name = name;
        this.price = price;
        this.date = date;
        this.writer = writer;
        this.page = page;
        this.shortDescribe = shortDescribe;
        this.description = description;
        this.category = category;
    }

    public String totalInfo(){
        String info = "￦" + price + "\n" + page + "\n" + writer + "\n" + date;
        return info;
    }
}
