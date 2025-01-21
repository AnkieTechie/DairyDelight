package com.example.dairydelight.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dairydelight.R;
import com.example.dairydelight.Retrofit.OrderResponse;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyChatHolder> {
    List<OrderResponse.Order> orderList;
    Context context;

    public OrderAdapter(List<OrderResponse.Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyChatHolder(LayoutInflater.from(context)
                .inflate(R.layout.single_chat_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyChatHolder holder, int position) {
        OrderResponse.Order model=orderList.get(position);
        holder.OrderId.setText(String.valueOf(model.getId()));
        holder.productsList.setText(String.join(", ", model.getProductTitles()));

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class MyChatHolder extends RecyclerView.ViewHolder{
        ImageView chatImage;
        TextView OrderId,productsList,chatTime;
        public MyChatHolder(@NonNull View itemView) {
            super(itemView);
            chatImage=itemView.findViewById(R.id.chatImage);
            OrderId=itemView.findViewById(R.id.OrderId);
            productsList=itemView.findViewById(R.id.productsList);
            chatTime=itemView.findViewById(R.id.chatTime);
        }
    }
}
