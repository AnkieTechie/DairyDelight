package com.example.dairydelight.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dairydelight.Activity.AddressActivity;
import com.example.dairydelight.Activity.DetailActivity;
import com.example.dairydelight.Activity.MainActivity;
import com.example.dairydelight.Database.CartDatabaseHelper;
import com.example.dairydelight.Models.CartModel;
import com.example.dairydelight.Models.TabModel;
import com.example.dairydelight.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyItemViewHolder> {
    private CartDatabaseHelper databaseHelper;
    List<TabModel> itemList;
    Context context;

    public ItemAdapter(List<TabModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_tab_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {
        TabModel productModel = itemList.get(position);
        holder.tabTitle.setText(productModel.getTitle());
        holder.single_price.setText(String.valueOf(productModel.getPrice()));
        Picasso.get()
                .load(productModel.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.TabImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, DetailActivity.class);
            intent.putExtra("productModel",productModel);
            context.startActivity(intent);
        });

        holder.addtocart.setOnClickListener(v -> {
            // Create a new CartModel to add to the cart
            CartModel newCartItem = new CartModel(productModel.getImage(),productModel.getTitle(),productModel.getDescription(),productModel.getPrice(),1);
            databaseHelper=new CartDatabaseHelper(context);
            long result=databaseHelper.addItemToCart(newCartItem);

            if (result != -1) {
                // Successfully added to cart, you can update UI or show a confirmation toast
                Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the error if any
                Toast.makeText(context, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
            }

        });
        holder.buyNoww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemPrice=String.valueOf(productModel.getPrice());
                context.startActivity(new Intent(context, AddressActivity.class).putExtra("itemPrice",itemPrice));
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyItemViewHolder extends RecyclerView.ViewHolder{
        ImageView TabImage;
        TextView tabTitle,single_price;
        AppCompatButton addtocart,buyNoww;

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            buyNoww=itemView.findViewById(R.id.buyNoww);
            addtocart=itemView.findViewById(R.id.addtocart);
            single_price=itemView.findViewById(R.id.single_price);
            tabTitle=itemView.findViewById(R.id.tabTitle);
            TabImage=itemView.findViewById(R.id.TabImage);

        }
    }
}
