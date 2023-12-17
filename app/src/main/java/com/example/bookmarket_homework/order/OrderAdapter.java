package com.example.bookmarket_homework.order;


import static com.example.bookmarket_homework.MainActivity.cartRepositoryObj;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookmarket_homework.R;
import com.example.bookmarket_homework.model.CartBook;

class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {


        Context context;


        public OrderAdapter(Context context) {
                this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerview_order_item, parent, false);

                return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                CartBook book = cartRepositoryObj.cartBooks.get(position);
                String str;

                if (book.name.length() > 20) str = book.name.substring(20) + "...";
                else str = book.name;
                holder.orderTitle.setText(str);
                holder.orderPrice.setText(Integer.toString(book.price));
                holder.orderQuantity.setText(Integer.toString(book.quantity));
                holder.orderSum.setText(Integer.toString(book.price * book.quantity));
        }

        @Override
        public int getItemCount() {
                return cartRepositoryObj.cartBooks.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
                TextView orderTitle;
                TextView orderPrice;
                TextView orderQuantity;
                TextView orderSum;

                public ViewHolder(@NonNull View itemView) {
                        super(itemView);
                        orderTitle = itemView.findViewById(R.id.title);
                        orderPrice = itemView.findViewById(R.id.price);
                        orderQuantity = itemView.findViewById(R.id.quantity);
                        orderSum = itemView.findViewById(R.id.sum);
                }
        }
}
