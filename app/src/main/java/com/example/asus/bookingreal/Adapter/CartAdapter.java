package com.example.asus.bookingreal.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus.bookingreal.Database.ModelDB.Cart;
import com.example.asus.bookingreal.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Picasso.with(context).load(cartList.get(position).link).into(holder.img_product);
        holder.txt_product_name.setText(cartList.get(position).name);
        holder.txt_cust.setText(new StringBuilder("ประชุมกับ : ").append(cartList.get(position).customer));
        holder.txt_time.setText(new StringBuilder()
                .append(cartList.get(position).time).append("\n")
                .append("วันที่ : ")
                .append(cartList.get(position).date).toString());

    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView img_product;
        TextView txt_product_name,txt_time,txt_cust;

        public RelativeLayout view_background;
        public LinearLayout view_foreground;

        public CartViewHolder(View itemView) {
            super(itemView);
            img_product = (ImageView)itemView.findViewById(R.id.img_product);
            txt_product_name = (TextView)itemView.findViewById(R.id.txt_product_name);
            txt_cust = (TextView)itemView.findViewById(R.id.txt_customer);
            txt_time = (TextView)itemView.findViewById(R.id.txt_time);
            view_background = (RelativeLayout)itemView.findViewById(R.id.view_background);
            view_foreground = (LinearLayout)itemView.findViewById(R.id.foreground);
        }
    }

    public void removeItem(int position){
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item, int position){
        cartList.add(position,item);
        notifyItemInserted(position);
    }

}
