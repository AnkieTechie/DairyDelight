package com.example.dairydelight.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dairydelight.Database.CartDatabaseHelper;
import com.example.dairydelight.Models.CartModel;
import com.example.dairydelight.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<CartModel> cartItemList;
    Context context;
    TextView ctotal;
    private CartDatabaseHelper databaseHelper;

    public CartAdapter(Context context,TextView ctotal, List<CartModel> cartItemList) {
        this.context=context;
        this.cartItemList = cartItemList;
        this.ctotal = ctotal;
        this.databaseHelper = new CartDatabaseHelper(context);
        updateTotal();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_cart_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartModel cartItem=cartItemList.get(position);
        Picasso.get()
                .load(cartItem.getcImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.cImage);
        holder.cTitle.setText(cartItem.getcTitle());
        holder.cPrice.setText(String.format("₹%.2f",cartItem.getcPrice()));
        holder.cquantity.setText(String.valueOf(cartItem.getCquantity()));
        holder.del.setOnClickListener(v -> deleteCartItem(position));
        holder.pos.setOnClickListener(v -> {
            cartItem.setCquantity(cartItem.getCquantity() + 1);
            databaseHelper.updateCartItemQuantity(cartItem.getId(), cartItem.getCquantity());
            notifyItemChanged(position);
            updateTotal();
        });

        // Decrease quantity
        holder.neg.setOnClickListener(v -> {
            if (cartItem.getCquantity() > 1) { // Ensure quantity doesn’t go below 1
                cartItem.setCquantity(cartItem.getCquantity() - 1);
                databaseHelper.updateCartItemQuantity(cartItem.getId(), cartItem.getCquantity());
                notifyItemChanged(position);
                updateTotal();
            }
        });
    }

    private void deleteCartItem(int position) {

        CartModel item = cartItemList.get(position);
        // Remove item from database
        databaseHelper.deleteCartItem(item.getId());
        // Remove item from list and notify adapter
        cartItemList.remove(position);
        notifyItemRemoved(position);
        updateTotal();
    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public void updateData(List<CartModel> newCartItems) {
        this.cartItemList = newCartItems;
        updateTotal();
    }

    private void updateTotal() {
        double total = 18;
        for (CartModel item : cartItemList) {
            total += item.getcPrice() * item.getCquantity();
        }
        ctotal.setText(String.valueOf(total));
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView cImage,neg,pos,del;
        TextView cTitle,cPrice,cquantity;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cImage=itemView.findViewById(R.id.cImage);
            cTitle=itemView.findViewById(R.id.cTitle);
            neg=itemView.findViewById(R.id.neg);
            pos=itemView.findViewById(R.id.pos);
            del=itemView.findViewById(R.id.del);
            cPrice=itemView.findViewById(R.id.cPrice);
            cquantity=itemView.findViewById(R.id.cquantity);
        }
    }
}
