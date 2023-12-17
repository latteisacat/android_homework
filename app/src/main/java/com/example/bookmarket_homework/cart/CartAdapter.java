package com.example.bookmarket_homework.cart;

import static com.example.bookmarket_homework.MainActivity.cartRepositoryObj;
import static com.example.bookmarket_homework.cart.CartActivity.cartUpdate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmarket_homework.R;
import com.example.bookmarket_homework.model.CartBook;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;


    public CartAdapter(Context context) {
        this.context = context;
    }

    private OnSelectChangedListener  selectChangedListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_cart_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CartBook book = cartRepositoryObj.cartBooks.get(position);

        holder.cartTitle.setText(book.name);
        holder.cartPrice.setText(Integer.toString(book.price));
        holder.cartCheckBox.setChecked(book.isChecked);
        holder.cartQuantity.setText(Integer.toString(book.quantity));
        holder.cartSum.setText(Integer.toString(book.price * book.quantity));
        // 여기서 ImageResource 설정
        switch (book.bookid) {
            case "BOOK1234":
                holder.cartPicture.setImageResource(R.drawable.book11);
                break;

            case "BOOK1235":
                holder.cartPicture.setImageResource(R.drawable.book21);
                break;

            case "BOOK1236":
                holder.cartPicture.setImageResource(R.drawable.book31);
                break;

            case "BOOK1237":
                holder.cartPicture.setImageResource(R.drawable.book41);
                break;
        }

        // 체크박스 클릭 시 동작 설정
        holder.cartCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.isChecked = holder.cartCheckBox.isChecked();
                selectChangedListener.onSelectChanged(cartRepositoryObj.cartBooks);
            }
        });
        // 장바구니 아이템 제거 기능
        holder.cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("장바구니에서 상품 제거");
                alertDialog.setMessage("선택한 상품을 장바구니에서 제거하겠습니까?");
                alertDialog.setIcon(R.drawable.dialog_cat);

                alertDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CartBook book = cartRepositoryObj.cartBooks.get(position);
                        cartRepositoryObj.cartBooks.remove(book);

                        cartUpdate();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartRepositoryObj.cartBooks.size());

                        dialogInterface.cancel();

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
    }

    @Override
    public int getItemCount() {
        return cartRepositoryObj.cartBooks.size();
    }

    // 해당 코드는 CartActivity 에서 따로구현해야 하기 때문에 인터페이스만 제공
    interface OnSelectChangedListener {
        void onSelectChanged(ArrayList<CartBook> items);
    }

    void setOnSelectChangedListener( OnSelectChangedListener listener) {
        selectChangedListener = listener;
    }

    // RecyclerView를 위한 ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartPicture;
        TextView cartTitle;
        TextView cartPrice;
        CardView cartParentLayout;
        CheckBox cartCheckBox;
        ImageButton cartDelete;
        TextView cartQuantity;
        TextView cartSum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartPicture = itemView.findViewById(R.id.cart_pic);
            cartTitle = itemView.findViewById(R.id.title);
            cartPrice = itemView.findViewById(R.id.price);
            cartParentLayout = itemView.findViewById(R.id.cart_parent_layout);
            cartDelete = itemView.findViewById(R.id.cart_delete);

            cartCheckBox= itemView.findViewById(R.id.cart_item_checkbox);
            cartQuantity = itemView.findViewById(R.id.quantity);
            cartSum = itemView.findViewById(R.id.sum);
        }

    }
}
