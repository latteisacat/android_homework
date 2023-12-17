package com.example.bookmarket_homework.model;

import java.util.ArrayList;

public class CartRepository {

    public ArrayList<CartBook> cartBooks  = new ArrayList<CartBook>();

    //카트에 아이템을 추가하는 코드
    public void addCartItems(CartBook book )  {
        int cnt = 0;
        ArrayList<CartBook> goodsList = cartBooks;
        CartBook goods = book;
        CartBook goodsQnt = new CartBook();

        for (int i = 0; i<goodsList.size();i++) {
            goodsQnt = goodsList.get(i);
            if (goodsQnt.bookid.equals(book.bookid)) {
                cnt++;
                goodsQnt.quantity += 1;
            }
        }

        if (cnt == 0) {
            goods.quantity = 1;
            goods.isChecked = true;
            cartBooks.add(goods);
        }
    }

    // 카트에 담긴 책을 세는 코드
    public int countCartItems( ){
        int totalQuantity = 0;
        if (cartBooks != null) {
            for (int i=0; i< cartBooks.size(); i++) {
                totalQuantity += cartBooks.get(i).quantity;
            }
        }
        return totalQuantity;
    }

    // 카트에 있는 책의 총 가격을 합산하는 코드
    public int grandTotalCartItems() {
        int totalPrice = 0;

        for (int i=0 ; i< cartBooks.size(); i++) {
            if (cartBooks.get(i).isChecked)
                totalPrice += cartBooks.get(i).price * cartBooks.get(i).quantity;
        }
        return totalPrice;
    }
}
